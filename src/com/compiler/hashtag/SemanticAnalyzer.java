package com.compiler.hashtag;

import com.compiler.ast.Data;
import com.compiler.ast.FunctionType;
import com.compiler.ast.Node;
import com.compiler.ast.SymbolTable;
import com.compiler.util.RandomUtils;
import jdk.nashorn.internal.ir.Symbol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/**
 * Created by Wilmer Carranza on 01/05/2015.
 */

public class SemanticAnalyzer {

    public static final ArrayList<String> ARITHMETIC_OPERATORS = new ArrayList<>(Arrays.asList("+", "-", "*", "/", "%"));
    public static final ArrayList<String> COMPARISON_OPERATORS = new ArrayList<>(Arrays.asList(">", "<", "==", "<=", ">=", "!="));
    public static final ArrayList<String> LOGICAL_OPERATORS = new ArrayList<>(Arrays.asList("AND", "OR"));
    public static final ArrayList<String> BLOCK_STATEMENTS = new ArrayList<>(Arrays.asList("PROG", "MAIN", "IF", "ELSE", "SWITCH", "FOR", "WHILE"));
    private final int DECLARED = 0;
    private final int INITIALIZED = 1;
    private final int ASSIGNED = 2;

    public static int semanticErrors = 0;

    //used for my Editor class to highlight in red the lines contained here.
    private static ArrayList<Integer> errorsList;

    //while traversing the tree, I lose track of which scope is which, or which one is the current. This stack controls that.
    private Stack<SymbolTable> tableStack;
    private int offset;

    public SemanticAnalyzer() {
        semanticErrors = 0;
        offset = 0;
        errorsList = new ArrayList<Integer>();
        tableStack = new Stack<SymbolTable>();
    }

    public void traverse(Node node) {
        if (!node.isLeaf()) {
            if (BLOCK_STATEMENTS.contains(node.label)) {
                //since a block has been found, it should have its own scope, so I need to setup my stack
                setupTableStack(node.label);
                for (Node block : node.getChildren()) {
                    //most of the time there are only two children, 'body' (always) and 'conditions', or 'structure', etc
                    traverse(block);
                }
            } else {
                switch (node.label.toLowerCase()) {
                    case "body":
                        for (Node bodyNode : node.getChildren()) {
                            traverse(bodyNode);
                        }
                        tableStack.pop();
                        break;
                    case "declarations":
                        for (Node declare : node.getChildren()) {
                            traverse(declare);
                        }
                        break;
                    case "init":
                        checkInitialization(node, INITIALIZED);
                        break;
                    case "declare":
                        checkDeclaration(node, DECLARED);
                        break;
                    case "parameters":
                        //initialized para que type check no llame a varNotInit error por tener null value
                        for (Node param : node.getChildren())
                            checkDeclaration(param, INITIALIZED);
                        break;
                    case "assign":
                        checkAssignment(node, ASSIGNED);
                        break;
                    case "conditions":
                        checkConditions(node.getChildren().get(0));
                        break;
                    case "functions":
                        checkFunction(node);
                        break;
                    case "structure":
                        checkForStructure(node);
                        break;
                    case "return":
                        checkReturn(node);
                        break;
                    case "case": //lol
                        checkCaseStatement(node);
                        break;
                    case "case_arg":
                    case "switch_arg":
                        checkSwitchParameter(node.getChildren().get(0));
                        break;
                    case "function_call":
                        checkFunctionCall("", node);
                        break;
                    default:
                        System.out.println("can't recognize node: " + node.label);
                        break;
                }
            }
        } else {
            if (node.label.equalsIgnoreCase("break")) {
                if (Node.findInPrevious(node, "while_statement", "for_statement", "switch_statement") == null) {
                    String message = "Break out of switch statement or loop.";
                    reportError(node, message);
                }
            } else {
                System.out.println("can't recognize node: " + node.label);
            }
        }
    }

    private void setupTableStack(String label) {
        SymbolTable current;

        //create a new Scope, which should be the current one with the previous one as its parent
        current = (tableStack.empty()) ? new SymbolTable(null) : new SymbolTable(tableStack.peek());
        current.setLabel(label);

        //now add the newly created scope as a child to the previous one.
        if (!tableStack.empty()) {
            tableStack.peek().addTable(current);
        }

        //now push the new scope to the stack.
        tableStack.push(current);
        offset = 0;
    }

    private void checkDeclaration(Node declare, int context) {
        Data data;
        Node variable;
        if (declare.label.equalsIgnoreCase("declare"))
            variable = declare.getChildren().get(0);
        else variable = declare;

        data = variable.getData();
        if (!SymbolTable.isInPrevious(tableStack.peek(), data.getLexeme())) {
            variable.getData().setTable(tableStack.peek());
            variable.getData().setContext(context);
            variable.getData().setDirection(offset);
            prepareOffset(variable.getData().getType());
            data = variable.getData(); //updated data
            tableStack.peek().put(data.getLexeme(), data);
        } else {
            reportVariableAlreadyDeclared(variable);
        }
    }

    private void checkInitialization(Node init, int context) {
        Node variable, value;
        value = init.getChildren().get(1);
        variable = init.getChildren().get(0);

        checkDeclaration(variable, context);

        //check what kind of expression is this value
        analyzeValue(value, variable);
    }

    private void checkAssignment(Node assign, int context) {
        Node variable, value;
        value = assign.getChildren().get(1);
        variable = assign.getChildren().get(0);
        Data variableData = new Data();

        try {
            //attempting to find this variable in the ST
            variableData = getIdentifierData(variable).setContext(context);

            //found it, now update its data (for error reporting) and analyze the value being assigned.
            variable.setData(variableData);
            analyzeValue(value, variable);
        } catch (NullPointerException npe) {
            reportVariableNotDeclared(variable);
        }
    }

    private void checkConditions(Node node) {
        if (node.isLeaf()) {
            if (node.getData().getToken().equalsIgnoreCase("identifier")) {
                checkIdentifier(node, "boolean");
            } else {
                if (!node.getData().getType().equalsIgnoreCase("boolean")) {
                    reportTypeMismatch(node, "boolean");
                }
            }
        } else {
            String label = node.label;
            if (COMPARISON_OPERATORS.contains(label)) {
                if (label.equalsIgnoreCase("==") || label.equalsIgnoreCase("!=")) {
                    Node right = node.getChildren().get(1);
                    Node left = node.getChildren().get(0);
                    if (left.getData().getType().equalsIgnoreCase("boolean") || right.getData().getType().equalsIgnoreCase("boolean")) {
                        checkConditions(left);
                        checkConditions(right);
                    } else {
                        checkExpression(node, "int");
                    }
                } else {
                    checkExpression(node, "int");
                }
            } else if (LOGICAL_OPERATORS.contains(label)) {
                Node left = node.getChildren().get(1);
                Node right = node.getChildren().get(0);
                checkConditions(left);
                checkConditions(right);
            } else if (ARITHMETIC_OPERATORS.contains(node.label)) {
                reportError(node, "Boolean expression expected.");
            }
        }
    }

    private void checkFunction(Node node) {
        setupTableStack(node.label);
        Node body = node.getChildren().get(0); //el unico hijo que deberia de tener
        for (Node function : body.getChildren()) {
            Data data = function.getData();
            if (!SymbolTable.isInPrevious(tableStack.peek(), data.getValue().toString())) {
                function.getData().setTable(tableStack.peek());
                data = function.getData();
                tableStack.peek().put(data.getValue().toString(), data);
            } else {
                reportVariableAlreadyDeclared(function);
            }
            setupTableStack(function.label);

            for (Node child : function.getChildren()) {
                traverse(child);
            }
        }
        tableStack.pop();
    }

    private void checkCaseStatement(Node node) {
        setupTableStack(node.label);
        Node parent = node.getParent();
        if (parent.label.equalsIgnoreCase("body")) {
            parent = parent.getParent();
        }
        if (!parent.label.equalsIgnoreCase("switch")) {
            reportError(node, "Case out of switch statement.");
        }
        for (Node child : node.getChildren()) {
            traverse(child);
        }
    }

    private void checkForStructure(Node struct) {
        //declarations node
        Node init = struct.getChildren().get(0);
        Node temp = init.getChildren().get(0);
        if (init.getChildren().size() > 1) {
            reportError(temp.getChildren().get(0), "Too many declarations in this for statement.");
        } else {
            Node var;
            if (!(var = temp.getChildren().get(0)).getData().getType().equalsIgnoreCase("int")) {
                reportError(var, "For loop can only by applied to type 'int'.");
            } else {
                if (temp.label.equalsIgnoreCase("init"))
                    checkInitialization(temp, INITIALIZED);
                else checkDeclaration(var, DECLARED);
            }
        }
        Node cond = struct.getChildren().get(1);
        checkConditions(cond);
        Node update = struct.getChildren().get(2);
        Node variable = update.getChildren().get(0);
        Data varData = null;
        try {
            varData = getIdentifierData(variable);
            variable.setData(varData);
            if (!varData.getType().equalsIgnoreCase("int")) {
                reportTypeMismatch(variable, "int");
            }
        } catch (NullPointerException npe) {
            reportVariableNotDeclared(variable);
        }
    }

    private void checkReturn(Node returnNode) {
        Node function;
        if ((function = (Node.findInPrevious(returnNode, "function"))) == null) {
            String message = "Return out of function.";
            reportError(returnNode, message);
        } else {
            FunctionType functionType = (FunctionType) function.getData().getValue();
            Node rtrn = returnNode.getChildren().get(0);
            if (COMPARISON_OPERATORS.contains(rtrn.label) || LOGICAL_OPERATORS.contains(rtrn.label)) {
                if (functionType.getRange().equalsIgnoreCase("boolean") && rtrn.getData().getType().equalsIgnoreCase("boolean")) {
                    checkConditions(rtrn);
                } else {
                    reportTypeMismatch(rtrn, functionType.getRange());
                }
            } else {
                checkExpression(rtrn, functionType.getRange());
            }
        }
    }

    private void analyzeValue(Node value, Node variable) {
        String variableType = variable.getData().getType();
        if (ARITHMETIC_OPERATORS.contains(value.label)) { //arithmetic expr
            if (variableType.equalsIgnoreCase("int") || variableType.equalsIgnoreCase("double")) { //double or int only for this kind of expr
                int temporal = semanticErrors;
                checkExpression(value, variableType);
                if (temporal == semanticErrors) {
                    try {
                        checkArithmetic(value);
                    } catch (ClassCastException cce) {
                        System.err.println("Warning: error caught during arithmetic check (debugging)");
                    } catch (Exception e) {
                        System.err.println("Warning: error caught during arithmetic check (debugging)");
                    }
                }
            } else {
                String message = "The expression can't be applied to type: '" + variableType + "'. Expected type: 'int' or 'double'.";
                reportError(variable, message);
            }
        } else if (COMPARISON_OPERATORS.contains(value.label) || LOGICAL_OPERATORS.contains(value.label)) { //boolean expr
            checkConditions(value);
        } else if (value.label.equalsIgnoreCase("function_call")) {
            checkFunctionCall(variableType, value);
        } else {
            if (value.getData().getToken().equalsIgnoreCase("identifier")) { //if true, find the id through scopes and get its type.
                checkIdentifier(value, variableType);
            } else if (!value.getData().getType().equalsIgnoreCase(variableType)) { //literal
                reportTypeMismatch(value, variableType);
            }
        }
    }

    private void checkExpression(Node node, String typeRequired) {
        if (node.isLeaf()) {
            if (node.getData().getToken().equalsIgnoreCase("identifier")) {
                checkIdentifier(node, typeRequired);
            } else if (!node.getData().getType().equalsIgnoreCase(typeRequired)) {
                reportTypeMismatch(node, typeRequired);
            }
        } else {
            Node right = node.getChildren().get(0);
            Node left = node.getChildren().get(1);
            checkExpression(right, typeRequired);
            checkExpression(left, typeRequired);
        }
    }

    private void checkFunctionCall(String typeRequired, Node functionCall) {
        SymbolTable root = getRootTable();
        SymbolTable functions = root.getChildren().get(0);

        Node functionToCall = functionCall.getChildren().get(0);
        Node parameters = functionCall.getChildren().get(1);

        Data functionData;

        if ((functionData = functions.getFunctionValue(functionToCall.label)) != null) {
            FunctionType functionType = (FunctionType) functionData.getValue();
            String type = functionType.getRange();
            if (!type.equalsIgnoreCase(typeRequired) && !typeRequired.isEmpty()) {
                functionToCall.getData().setType(type);
                reportTypeMismatch(functionToCall, typeRequired);
            } else {
                //check parameters
                String[] domain = functionType.getDomain();
                if (domain.length == parameters.getChildren().size()) {
                    Node current;
                    for (int i = 0; i < domain.length; i++) {
                        current = parameters.getChildren().get(i);
                        if (current.label.equalsIgnoreCase("function_call")) {
                            checkFunctionCall(domain[i], current);
                        } else if (current.getData().getToken().equalsIgnoreCase("identifier")) {
                            checkIdentifier(current, domain[i]);
                        } else if (ARITHMETIC_OPERATORS.contains(current.label)) {
                            checkExpression(current, domain[i]);
                        } else if (LOGICAL_OPERATORS.contains(current.label) || COMPARISON_OPERATORS.contains(current.label)) {
                            checkConditions(current);
                        } else if (!current.getData().getType().equalsIgnoreCase(domain[i])) {
                            reportTypeMismatch(current, domain[i]);
                        }
                    }
                } else {
                    String message = "Wrong number of parameters. The function you are calling needs " + domain.length + ".";
                    reportError(functionToCall, message);
                }
            }
        } else {
            String message = "Function '" + functionToCall.label + "' has not been declared.";
            reportError(functionToCall, message);
        }
    }

    private void checkIdentifier(Node identifier, String variableType) {
        try {
            Data valueData;
            if ((valueData = getIdentifierData(identifier)).getContext() == DECLARED) {
                reportVariableNotInitialized(identifier);
            } else {
                identifier.getData().setType(valueData.getType());
                if (!valueData.getType().equalsIgnoreCase(variableType)) {
                    reportTypeMismatch(identifier, variableType);
                }
            }
        } catch (NullPointerException npe) {
            reportVariableNotDeclared(identifier);
        }
    }

    private Object checkArithmetic(Node node) throws ClassCastException {
        if (node.isLeaf()) {
            if (node.getData().getToken().equalsIgnoreCase("identifier")) {
                return getIdentifierData(node).getValue();
            } else return node.getData().getValue();
        } else {
            Node right = node.getChildren().get(0);
            Node left = node.getChildren().get(1);
            Object val1 = checkArithmetic(left);
            Object val2 = checkArithmetic(right);

            if (val1 == null || val2 == null) {
                return null;
            } else {
                try {
                    if (val2 instanceof Integer && (Integer) val2 == 0 && (node.label.equalsIgnoreCase("/") || node.label.equalsIgnoreCase("%"))) {
                        reportError(right, "This expression results in a division by zero.");
                        return null;
                    } else if (val2 instanceof Double && (Double) val2 == 0.0 && (node.label.equalsIgnoreCase("/") || node.label.equalsIgnoreCase("%"))) {
                        reportError(right, "This expression results in a division by zero.");
                        return null;
                    }
                    Object result = RandomUtils.operate(node.label, val1, val2);
                    node.getData().setValue(result);
                    return result;
                } catch (ArithmeticException ae) {
                    return null;
                }
            }
        }
    }

    private void checkSwitchParameter(Node arg) {
        if (arg.isLeaf()) {
            if (arg.getData().getToken().equalsIgnoreCase("identifier")) {
                try {
                    Data valueData;
                    if ((valueData = getIdentifierData(arg)).getContext() == DECLARED) {
                        reportVariableNotInitialized(arg);
                    } else {
                        arg.getData().setType(valueData.getType());
                        if (!valueData.getType().equalsIgnoreCase("char") && !valueData.getType().equalsIgnoreCase("int")) {
                            reportTypeMismatch(arg, "'int' or 'char'");
                        }
                    }
                } catch (NullPointerException npe) {
                    reportVariableNotDeclared(arg);
                }
            } else {
                if (!arg.getData().getType().equalsIgnoreCase("int") && !arg.getData().getType().equalsIgnoreCase("char")) {
                    reportTypeMismatch(arg, "'int' or 'char'");
                }
            }
        } else {
            if (ARITHMETIC_OPERATORS.contains(arg.label)) {
                checkExpression(arg, "int");
            } else {
                reportError(arg, "Invalid expression. Expected 'int' or 'char'.");
            }
        }
    }

    public Data getIdentifierData(Node variable) throws NullPointerException {
        Data data = SymbolTable.findInPrevious(tableStack.peek(), variable.label);
        if (data != null) return data;
        else {
            throw new NullPointerException();
        }
    }

    private void prepareOffset(String type) {
        switch (type) {
            case "int":
                offset += 4;
                break;
            case "char":
            case "boolean":
                offset++;
                break;
        }
    }

    public ArrayList<Integer> getErrorLines() {
        return this.errorsList;
    }

    public SymbolTable getRootTable() {
        return tableStack.firstElement();
    }

    //<editor-fold desc="Error reporting">
    private static void reportVariableNotInitialized(Node node) {
        int line = node.getData().getLine();
        int column = node.getData().getColumn();
        Editor.console.setText(Editor.console.getText()
                + "\nError: (line: " + line + ", column: " + column + ")\n"
                + "    " + (++semanticErrors) + "==> " + "Variable '" + node.getData().getLexeme() + "' might have not been initialized."
                + "\n");
        if (!errorsList.contains(line)) {
            errorsList.add(line);
        }
    }

    private static void reportVariableNotDeclared(Node node) {
        int line = node.getData().getLine();
        int column = node.getData().getColumn();
        Editor.console.setText(Editor.console.getText()
                + "\nError: (line: " + line + ", column: " + column + ")\n"
                + "    " + (++semanticErrors) + "==> " + "Variable '" + node.getData().getLexeme() + "' has not been declared."
                + "\n");
        if (!errorsList.contains(line)) {
            errorsList.add(line);
        }

    }

    private static void reportVariableAlreadyDeclared(Node node) {
        int line = node.getData().getLine();
        int column = node.getData().getColumn();
        Editor.console.setText(Editor.console.getText()
                + "\nError: (line: " + line + ", column: " + column + ")\n"
                + "    " + (++semanticErrors) + "==> " + "Variable '" + node.getData().getLexeme() + "' has already been declared in the scope."
                + "\n");
        if (!errorsList.contains(line)) {
            errorsList.add(line);
        }
    }

    private static void reportTypeMismatch(Node found, String required) {
        if (!required.equalsIgnoreCase("null")) {
            int line = found.getData().getLine();
            int column = found.getData().getColumn();
            Editor.console.setText(Editor.console.getText()
                    + "\nError: (line: " + line + ", column: " + column + ")\n"
                    + "    " + (++semanticErrors) + "==> " + "Incompatible types"
                    + "\n" + "        " + " found: '" + found.getData().getType() + "'"
                    + "\n" + "        " + " required: '" + required + "'"
                    + "\n");
            if (!errorsList.contains(line)) {
                errorsList.add(line);
            }
        }
    }

    private static void reportError(Node node, String message) {
        int line = node.getData().getLine();
        int column = node.getData().getColumn();
        Editor.console.setText(Editor.console.getText()
                + "\nError: (line: " + line + ", column: " + column + ")\n"
                + "    " + (++semanticErrors) + "==> " + message
                + "\n");
        if (!errorsList.contains(line)) {
            errorsList.add(line);
        }
    }
    //</editor-fold>
}