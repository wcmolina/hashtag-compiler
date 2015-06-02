package com.compiler.ast;

import com.compiler.hashtag.Editor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/**
 * Created by Wilmer Carranza on 01/05/2015.
 * <p>
 * This class basically manages all my Semantic Analysis
 * </p>
 */

public class TreeAnalyzer {

    private static final String[] ARITHMETIC_OPERATORS = {"+", "-", "*", "/", "%"};
    private static final String[] COMPARISON_OPERATORS = {">", "<", "==", "<=", ">=", "!="};
    private static final String[] LOGICAL_OPERATORS = {"AND", "OR"};
    private static final String[] BLOCK_STATEMENTS = {"PROG", "MAIN", "IF", "ELSE", "SWITCH", "FOR", "WHILE", "CASE"};

    /**
     * Serves only as an error counter. Increases is time an error is reported.
     */
    public static int semanticErrors = 0;

    //used for my Editor class to highlight in red the lines contained here.
    private static ArrayList<Integer> errors;

    //while traversing the tree, I lose track of which scope is which, or which one is the current. This stack controls that.
    private Stack<Scope> scopeStack;

    //main constructor, where root is the root of the AST.
    public TreeAnalyzer() {
        semanticErrors = 0;
        errors = new ArrayList<Integer>();
        scopeStack = new Stack<Scope>();
    }

    /**
     * <code>traverse</code> 'runs' through the AST. It divides its work by calling functions it needs when a certain node is reached.
     * @param node the starting <code>Node</code>.
     */
    public void traverse(Node node) {
        if (!node.isLeaf()) {
            if (Arrays.asList(BLOCK_STATEMENTS).contains(node.label)) {
                //since a block has been found, it should have its own scope, so I need to setup my stack
                setupStack(node.label);
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

                    //here is where the function divides its work
                } else if (node.label.equalsIgnoreCase("init")) {
                    initHandler(node);
                } else if (node.label.equalsIgnoreCase("declare")) {
                    declareHandler(node);
                } else if (node.label.equalsIgnoreCase("assign")) {
                    assignHandler(node);
                } else if (node.label.equalsIgnoreCase("conditions")) {
                    conditionHandler(node.getChildren().get(0));
                } else if (node.label.equalsIgnoreCase("functions")) {
                    functionHandler(node);
                } else if (node.label.equalsIgnoreCase("return")) {

                } else {
                    System.out.println("can't recognize node: " + node.label);
                }
            }
        }
    }

    private void setupStack(String label) {
        Scope current;
        //create a new Scope, which should be the current one with the previous one as its parent
        current = (scopeStack.empty()) ? new Scope(null) : new Scope(scopeStack.peek());
        current.setLabel(label);
        //Now add the newly created Scope as a child to the previous Scope.
        if (!scopeStack.empty()) {
            scopeStack.peek().addScope(current);
        }
        //now push the new Scope to the stack.
        scopeStack.push(current);
    }

    //example: int x;
    private void declareHandler(Node declare) {
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

    //example: int x = 7;
    private void initHandler(Node init) {
        Node variable, value;
        value = init.getChildren().get(1); //value
        variable = init.getChildren().get(0); //id, which is actually a declare node, but it holds info on the type (as well as its children)

        //adding all variables of 'declare' to the symbol table
        declareHandler(variable);

        //analyze what kind of expression is this value
        analyzeValue(value, variable);
    }

    //example: x = 0;
    private void assignHandler(Node assign) {
        Node variable, value;
        value = assign.getChildren().get(1);
        variable = assign.getChildren().get(0);
        Data variableData = new Data();

        //attempting to find this variable in the ST
        try {
            variableData = getIdentifierData(variable);

            //found it, now update its data and analyze the value being assigned.
            variable.setData(variableData);
            analyzeValue(value, variable);
        } catch (NullPointerException npe) {
            reportVariableNotDeclared(variable);
        }

    }

    //example: 7+5*8/x-y
    private void arithmeticHandler(Node node, String typeRequired) {
        if (node.isLeaf()) {
            if (node.getData().getToken().equalsIgnoreCase("identifier")) {
                try {
                    if (getIdentifierData(node).getValue() == null) {
                        reportVariableNotInitialized(node);
                    } else {
                        Data valueData = getIdentifierData(node);
                        node.getData().setType(valueData.getType());
                        if (!valueData.getType().equalsIgnoreCase(typeRequired)) {
                            reportTypeMismatch(node, typeRequired);
                        }
                    }
                } catch (NullPointerException npe) {
                    reportVariableNotDeclared(node);
                }
            } else if (!node.getData().getType().equalsIgnoreCase(typeRequired)) {
                reportTypeMismatch(node, typeRequired);
            }
        } else {
            Node right = node.getChildren().get(1);
            Node left = node.getChildren().get(0);

            if (node.label.equalsIgnoreCase("/")) {
                checkDivision(right);
                arithmeticHandler(left, typeRequired);
            } else {
                arithmeticHandler(right, typeRequired);
                arithmeticHandler(left, typeRequired);
            }
        }
    }

    private void comparisonHandler(Node operator) {
        Node left = operator.getChildren().get(0);
        Node right = operator.getChildren().get(1);

        //here, i use arithmeticHandler just because it has the same code comparisonHandler needs
        arithmeticHandler(left, "int");
        arithmeticHandler(right, "int");
    }

    private void conditionHandler(Node node) {
        if (node.isLeaf()) {
            if (node.getData().getToken().equalsIgnoreCase("identifier")) {
                try {
                    if (getIdentifierData(node).getValue() == null) {
                        reportVariableNotInitialized(node);
                    } else {
                        Data valueData = getIdentifierData(node);
                        node.getData().setType(valueData.getType());
                        if (!valueData.getType().equalsIgnoreCase("boolean")) {
                            reportTypeMismatch(node, "boolean");
                        }
                    }
                } catch (NullPointerException npe) {
                    reportVariableNotDeclared(node);
                }
            } else {
                if (!node.getData().getType().equalsIgnoreCase("boolean")) {
                    reportTypeMismatch(node, "boolean");
                }
            }
        } else {
            String label = node.label;
            if (Arrays.asList(COMPARISON_OPERATORS).contains(label)) {
                comparisonHandler(node);
            } else if (Arrays.asList(LOGICAL_OPERATORS).contains(label)) {
                Node left = node.getChildren().get(0);
                Node right = node.getChildren().get(1);
                conditionHandler(left);
                conditionHandler(right);
            } else if (Arrays.asList(ARITHMETIC_OPERATORS).contains(node.label)) {
                String message = "Boolean expression expected.";
                reportError(node, message);
            }
        }
    }

    private void functionHandler(Node node) {
        Node body = node.getChildren().get(0); //el unico hijo qe deberia de tener
        for (Node function : body.getChildren()) {
            //cada hijo tendria su propio scope.
            System.out.println(function.getData().getValue());
        }
    }

    private void analyzeValue(Node value, Node variable) {
        String variableType = variable.getData().getType();
        if (Arrays.asList(ARITHMETIC_OPERATORS).contains(value.label)) { //arithmetic expr
            if (variableType.equalsIgnoreCase("int") || variableType.equalsIgnoreCase("double")) { //Hashtag only supports int or double
                arithmeticHandler(value, variableType);
            } else {
                String message = "The expression can't be applied to type: " + variableType + ". Expected type: 'int' or 'double'.";
                reportError(variable, message);
            }
        } else if (Arrays.asList(COMPARISON_OPERATORS).contains(value.label) || Arrays.asList(LOGICAL_OPERATORS).contains(value.label)) { //boolean expr
            conditionHandler(value);
        } else {
            if (value.getData().getToken().equalsIgnoreCase("identifier")) { //if true, find the id through scopes and get its type.
                try {
                    if (getIdentifierData(value).getValue() == null) {
                        reportVariableNotInitialized(value);
                    } else {
                        Data valueData = getIdentifierData(value);
                        value.getData().setType(valueData.getType());
                        if (!valueData.getType().equalsIgnoreCase(variableType)) {
                            reportTypeMismatch(value, variableType);
                        }
                    }
                } catch (NullPointerException npe) {
                    reportVariableNotDeclared(value);
                }
            } else if (!value.getData().getType().equalsIgnoreCase(variableType)) { //literal
                reportTypeMismatch(value, variableType);
            }
        }
    }

    private void checkDivision(Node node) {
        Data data = node.getData();
        if (data.getType().equalsIgnoreCase("int")) {
            if ((Integer) data.getValue() == 0) {
                reportError(node, "Can't divide by 0.");
            }
        } else if (data.getToken().equalsIgnoreCase("identifier")) {
            Data idData = getIdentifierData(node);
            node.getData().setType(idData.getType());
            if (idData.getValue() == null) {
                reportVariableNotInitialized(node);
            } else {
                if (idData.getType().equalsIgnoreCase("int")) {
                    if ((Integer) idData.getValue() == 0) {
                        reportError(node, "Can't divide by 0.");
                    }
                } else {
                    reportTypeMismatch(node, "int");
                }
            }
        } else {
            reportTypeMismatch(node, "int");
        }
    }

    private Data getIdentifierData(Node variable) throws NullPointerException {
        Data data = Scope.findInPrevious(scopeStack.peek(), variable.label);
        if (data != null) return data;
        else {
            throw new NullPointerException();
        }
    }

    public ArrayList<Integer> getErrorLines() {
        return this.errors;
    }

    //different types of error reports
    private static void reportVariableNotInitialized(Node node) {
        int line = node.getData().getLine();
        int column = node.getData().getColumn();
        Editor.console.setText(Editor.console.getText()
                + "\nError: (line: " + line + ", column: " + column + ")\n"
                + "    " + (++semanticErrors) + "==> " + "Variable '" + node.getData().getLexeme() + "' might have not been initialized."
                + "\n");
        if (!errors.contains(line)) {
            errors.add(line);
        }
    }

    private static void reportVariableNotDeclared(Node node) {
        int line = node.getData().getLine();
        int column = node.getData().getColumn();
        Editor.console.setText(Editor.console.getText()
                + "\nError: (line: " + line + ", column: " + column + ")\n"
                + "    " + (++semanticErrors) + "==> " + "Variable '" + node.getData().getLexeme() + "' has not been declared."
                + "\n");
        if (!errors.contains(line)) {
            errors.add(line);
        }

    }

    private static void reportVariableAlreadyDeclared(Node node) {
        int line = node.getData().getLine();
        int column = node.getData().getColumn();
        Editor.console.setText(Editor.console.getText()
                + "\nError: (line: " + line + ", column: " + column + ")\n"
                + "    " + (++semanticErrors) + "==> " + "Variable '" + node.getData().getLexeme() + "' has already been declared."
                + "\n");
        if (!errors.contains(line)) {
            errors.add(line);
        }
    }

    private static void reportTypeMismatch(Node found, String required) {
        if (!required.equalsIgnoreCase("null")) {
            int line = found.getData().getLine();
            int column = found.getData().getColumn();
            Editor.console.setText(Editor.console.getText()
                    + "\nError: (line: " + line + ", column: " + column + ")\n"
                    + "    " + (++semanticErrors) + "==> " + "INCOMPATIBLE types"
                    + "\n" + "        " + " found: " + found.getData().getType()
                    + "\n" + "        " + " required: " + required
                    + "\n");
            if (!errors.contains(line)) {
                errors.add(line);
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
        if (!errors.contains(line)) {
            errors.add(line);
        }
    }
}
