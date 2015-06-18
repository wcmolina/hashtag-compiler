package com.compiler.hashtag;

import com.compiler.ast.Data;
import com.compiler.ast.FunctionType;
import com.compiler.ast.Node;
import com.compiler.ast.Scope;
import com.compiler.codegeneration.IntermediateCode;
import com.compiler.util.RandomUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/**
 * Created by Wilmer Carranza on 01/05/2015.
 */

public class SemanticAnalyzer {

    public static final ArrayList<String> ARITHMETIC_OPERATORS = new ArrayList<String>(Arrays.asList("+", "-", "*", "/", "%"));
    public static final ArrayList<String> COMPARISON_OPERATORS = new ArrayList<String>(Arrays.asList(">", "<", "==", "<=", ">=", "!="));
    public static final ArrayList<String> LOGICAL_OPERATORS = new ArrayList<String>(Arrays.asList("AND", "OR"));
    public static final ArrayList<String> BLOCK_STATEMENTS = new ArrayList<String>(Arrays.asList("PROG", "MAIN", "IF", "ELSE", "SWITCH", "FOR", "WHILE"));

    public static int semanticErrors = 0;

    //used for my Editor class to highlight in red the lines contained here.
    private static ArrayList<Integer> errorsList;

    //while traversing the tree, I lose track of which scope is which, or which one is the current. This stack controls that.
    private Stack<Scope> scopeStack;

    public SemanticAnalyzer() {
        semanticErrors = 0;
        errorsList = new ArrayList<Integer>();
        scopeStack = new Stack<Scope>();
    }

    public void traverse(Node node) {
        if (!node.isLeaf()) {
            if (BLOCK_STATEMENTS.contains(node.label)) {
                //since a block has been found, it should have its own scope, so I need to setup my stack
                setupScopeStack(node.label);
                for (Node block : node.getChildren()) {
                    //most of the time there are only two children, 'body' (always) and 'conditions', or 'structure', etc
                    traverse(block);
                }
            } else {
                if (node.label.equalsIgnoreCase("body")) {
                    for (Node bodyNode : node.getChildren()) {
                        traverse(bodyNode);
                    }
                    scopeStack.pop();
                } else if (node.label.equalsIgnoreCase("init")) {
                    checkInitialization(node);
                } else if (node.label.equalsIgnoreCase("declare")) {
                    checkDeclaration(node);
                } else if (node.label.equalsIgnoreCase("assign")) {
                    checkAssignment(node);
                } else if (node.label.equalsIgnoreCase("conditions")) {
                    checkConditions(node.getChildren().get(0));
                } else if (node.label.equalsIgnoreCase("functions")) {
                    checkFunction(node);
                } else if (node.label.equalsIgnoreCase("structure")) {
                    checkForStructure(node);
                } else if (node.label.equalsIgnoreCase("return")) {
                    checkReturn(node);
                } else if (node.label.equalsIgnoreCase("case")) {
                    checkCaseStatement(node);
                } else if (node.label.equalsIgnoreCase("case_arg") || node.label.equalsIgnoreCase("switch_arg")) {
                    checkSwitchParameter(node.getChildren().get(0));
                } else if (node.label.equalsIgnoreCase("parameters")) {
                    checkDeclaration(node);
                } else if (node.label.equalsIgnoreCase("function_call")) {
                    System.out.println("function_call handler missing!");
                } else {
                    System.out.println("can't recognize node: " + node.label);
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

    private void setupScopeStack(String label) {
        Scope current;

        //create a new Scope, which should be the current one with the previous one as its parent
        current = (scopeStack.empty()) ? new Scope(null) : new Scope(scopeStack.peek());
        current.setLabel(label);

        //now add the newly created scope as a child to the previous one.
        if (!scopeStack.empty()) {
            scopeStack.peek().addScope(current);
        }

        //now push the new scope to the stack.
        scopeStack.push(current);
    }

    private void checkDeclaration(Node declare) {
        Data data;
        for (Node variable : declare.getChildren()) {
            data = variable.getData();
            if (!Scope.isInPrevious(scopeStack.peek(), data.getLexeme())) {
                variable.getData().setScope(scopeStack.peek());
                data = variable.getData();
                scopeStack.peek().put(data.getLexeme(), data);
            } else {
                reportVariableAlreadyDeclared(variable);
            }
        }
    }

    private void checkInitialization(Node init) {
        Node variable, value;
        value = init.getChildren().get(1); //value
        variable = init.getChildren().get(0); //id, which is actually a declare node, but it holds info on the type (as well as its children)

        //adds all variables of 'declare' to the symbol table, if possible
        checkDeclaration(variable);

        //check what kind of expression is this value
        if (value.label.equalsIgnoreCase("function_call")) {
            System.out.println("type check function call!");
        } else {
            analyzeValue(value, variable);
        }
    }

    private void checkAssignment(Node assign) {
        Node variable, value;
        value = assign.getChildren().get(1);
        variable = assign.getChildren().get(0);
        Data variableData = new Data();

        try {
            //attempting to find this variable in the ST
            variableData = getIdentifierData(variable);

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
        setupScopeStack(node.label);
        Node body = node.getChildren().get(0); //el unico hijo que deberia de tener
        for (Node function : body.getChildren()) {
            Data data = function.getData();
            if (!Scope.isInPrevious(scopeStack.peek(), data.getValue().toString())) {
                function.getData().setScope(scopeStack.peek());
                data = function.getData();
                scopeStack.peek().put(data.getValue().toString(), data);
            } else {
                reportVariableAlreadyDeclared(function);
            }
            setupScopeStack(function.label);

            for (Node child : function.getChildren()) {
                traverse(child);
            }
        }
        scopeStack.pop();
    }

    private void checkCaseStatement(Node node) {
        setupScopeStack(node.label);
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
        Node init = struct.getChildren().get(0);
        Node decl = init.getChildren().get(0);
        if (decl.getChildren().size() > 1) {
            reportError(decl.getChildren().get(0), "Too many declarations in this for statement.");
        } else {
            Node temp;
            if (!(temp = decl.getChildren().get(0)).getData().getType().equalsIgnoreCase("int")) {
                reportError(temp, "For loop can only by applied to type 'int'.");
            } else {
                checkInitialization(init);
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
                    checkArithmetic(value);
                }
            } else {
                String message = "The expression can't be applied to type: '" + variableType + "'. Expected type: 'int' or 'double'.";
                if (variable.label.equalsIgnoreCase("declare")) {
                    reportError(variable.getChildren().get(0), message);
                } else {
                    reportError(variable, message);
                }
            }
        } else if (COMPARISON_OPERATORS.contains(value.label) || LOGICAL_OPERATORS.contains(value.label)) { //boolean expr
            checkConditions(value);
        } else {
            if (value.getData().getToken().equalsIgnoreCase("identifier")) { //if true, find the id through scopes and get its type.
                int temp = semanticErrors;
                checkIdentifier(value, variableType);
                if (temp == semanticErrors) {
                    if (variable.label.equalsIgnoreCase("declare")) {
                        for (Node node : variable.getChildren()) {
                            node.getData().setValue(value.getData().getValue());
                        }
                    } else {
                        variable.getData().setValue(value.getData().getValue());
                    }
                }
            } else if (!value.getData().getType().equalsIgnoreCase(variableType)) { //literal
                reportTypeMismatch(value, variableType);
            } else {
                variable.getData().setValue(value.getData().getValue());
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

    private void checkIdentifier(Node identifier, String variableType) {
        try {
            Data valueData;
            if ((valueData = getIdentifierData(identifier)).getValue() == null) {
                reportVariableNotInitialized(identifier);
            } else {
                identifier.getData().setValue(valueData.getValue());
                if (!identifier.getData().getType().equalsIgnoreCase(variableType)) {
                    reportTypeMismatch(identifier, variableType);
                }
            }
        } catch (NullPointerException npe) {
            reportVariableNotDeclared(identifier);
        }
    }

    private Object checkArithmetic(Node node) {
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

    //similar code checkExpression has, but with a different approach
    private void checkSwitchParameter(Node arg) {
        if (arg.isLeaf()) {
            if (arg.getData().getToken().equalsIgnoreCase("identifier")) {
                try {
                    Data valueData;
                    if ((valueData = getIdentifierData(arg)).getValue() == null) {
                        reportVariableNotInitialized(arg);
                    } else {
                        arg.getData().setValue(valueData.getValue());
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
        Data data = Scope.findInPrevious(scopeStack.peek(), variable.label);
        if (data != null) return data;
        else {
            throw new NullPointerException();
        }
    }

    public ArrayList<Integer> getErrorLines() {
        return this.errorsList;
    }

    //testing how function_call nodes are going to work...
    public void getRootScope() {
        Scope root = scopeStack.firstElement();
        Scope functions = null;
        System.out.println("first stack element: " + root.getID());
        for (Scope scope : root.getChildren()) {
            if (scope.getLabel().equalsIgnoreCase("functions")) {
                //this is the scope where function_call nodes should look for their functions...
                functions = scope;
            }
        }
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