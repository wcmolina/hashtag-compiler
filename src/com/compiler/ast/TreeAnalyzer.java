package com.compiler.ast;

import com.compiler.hashtag.Editor;

import java.util.Arrays;
import java.util.Stack;

/**
 * Created by Wilmer Carranza on 01/05/2015.
 */

//todo: work on arithmetic expression while type checking!

public class TreeAnalyzer {

    private static final String ARITH_OPERATORS[] = {"+", "-", "*", "/", "%"};
    private static final String COMPARISON_OPERATORS[] = {">", "<", "==", "<=", ">=", "!="};
    private static final String LOGICAL_OPERATORS[] = {"AND", "OR"};
    private static final String BLOCK_STATEMENTS[] = {"PROG", "MAIN", "IF", "ELSE", "SWITCH", "FOR", "WHILE", "CASE"};
    public static int semanticErrors = 0;
    private Stack<Scope> scopeStack = new Stack<Scope>();

    public TreeAnalyzer(Node root) {
        semanticErrors = 0;
        traverse(root);
    }

    public void traverse(Node node) {
        if (!node.isLeaf()) {
            if (Arrays.asList(BLOCK_STATEMENTS).contains(node.label)) { //new scopes if it finds a block stmts or main or prog or functions.
                setupStack(node.label);
                for (Node block : node.getChildren()) { //prog can have 2 children, as well as IF, WHILE, SWITCH, etc...
                    traverse(block);
                    //block_handler? where it can detect the type of block being sent as a parameter...
                }
            } else {
                if (node.label.equalsIgnoreCase("body")) {
                    for (Node bodyNode : node.getChildren()) {
                        traverse(bodyNode);
                    }
                    scopeStack.pop();
                } else if (node.label.equalsIgnoreCase("init")) {
                    initHandler(node);
                } else if (node.label.equalsIgnoreCase("declare")) {
                    declareHandler(node);
                } else if (node.label.equalsIgnoreCase("assign")) {
                    assignHandler(node);
                } else {
                    System.out.println("no matches on ifs: " + node.label);
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

    //x = 0;
    private void assignHandler(Node assign) {
        Node variable, value;
        value = assign.getChildren().get(0);
        variable = assign.getChildren().get(1);

        //attempting to find this variable in the ST
        try {
            Data varData = getIdentifierData(variable);

            //found it, now compare the types
            //todo: find out if i should update its data once found to the assigned value or what....
            if (!Arrays.asList(ARITH_OPERATORS).contains(value.label)) {
                if (value.getData().getToken().equalsIgnoreCase("identifier")) { //if true, find the id through scopes and get its type.
                    try {
                        if (getIdentifierData(value).getValue() == null) {
                            reportVariableNotInitialized(value);
                        } else {
                            Data value_data = getIdentifierData(value);
                            value.getData().setType(value_data.getType());
                            if (!value_data.getType().equalsIgnoreCase(varData.getType())) {
                                reportTypeMismatch(value, varData.getType());
                            }
                        }
                    } catch (NullPointerException npe) {
                        reportVariableNotDeclared(value);
                    }
                } else if (!value.getData().getType().equalsIgnoreCase(varData.getType())) {
                    //type error
                    reportTypeMismatch(value, varData.getType());
                }
            } else {
                arithmeticHandler(value, varData.getType());
            }
        } catch (NullPointerException npe) {
            reportVariableNotDeclared(variable);
        }
    }

    //int x;
    private void declareHandler(Node declare) {
        Data data;
        for (Node variable : declare.getChildren()) {
            data = variable.getData();
            if (!Scope.isInPrevious(scopeStack.peek(), data.getLexeme())) {
                scopeStack.peek().put(data.getLexeme(), data);
            } else {
                reportVariableAlreadyDeclared(variable);
            }
        }
    }

    //int x = 7;
    private void initHandler(Node init) {
        Node variable, value;
        value = init.getChildren().get(0); //value
        variable = init.getChildren().get(1); //id, which is actually a declare node, but it holds info on the type (as well as its children)
        String variableType = variable.getData().getType();


        //adding all variables of 'declare' to the symbol table
        declareHandler(variable);

        //now checking if the type 'declare' matches the type 'value' has
        if (!Arrays.asList(ARITH_OPERATORS).contains(value.label)) { //if true, send it to arithmeticHandler
            if (value.getData().getToken().equalsIgnoreCase("identifier")) { //if true, find the id through scopes and get its type.
                try {
                    if (getIdentifierData(value).getValue() == null) {
                        reportVariableNotInitialized(value);
                    } else {
                        Data value_data = getIdentifierData(value);
                        value.getData().setType(value_data.getType());
                        if (!value_data.getType().equalsIgnoreCase(variableType)) {
                            reportTypeMismatch(value, variableType);
                        }
                    }
                } catch (NullPointerException npe) {
                    reportVariableNotDeclared(value);
                }
            } else if (!value.getData().getType().equalsIgnoreCase(variable.getData().getType())) {
                //type error
                reportTypeMismatch(value, variableType);
            }
        } else {
            arithmeticHandler(value, variableType);
        }
    }

    //7+5*8/x-y
    private void arithmeticHandler(Node node, String typeRequired) {
        if (node.isLeaf()) {
            if (node.getData().getToken().equalsIgnoreCase("identifier")) {
                try {
                    if (getIdentifierData(node).getValue() == null) {
                        reportVariableNotInitialized(node);
                    } else {
                        Data value_data = getIdentifierData(node);
                        node.getData().setType(value_data.getType());
                        if (!value_data.getType().equalsIgnoreCase(typeRequired)) {
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
            Node right = node.getChildren().get(0);
            Node left = node.getChildren().get(1);
            arithmeticHandler(right, typeRequired);
            arithmeticHandler(left, typeRequired);
        }
    }

    private void blockHandler(Node block) {
        //IF block stmt
        if (block.label.equalsIgnoreCase("if")) {

        }
    }

    private Data getIdentifierData(Node variable) throws NullPointerException {
        Data data = Scope.findInPrevious(scopeStack.peek(), variable.label);
        if (data != null) return data;
        else {
            throw new NullPointerException();
        }
    }

    private static void reportVariableNotInitialized(Node node) {
        Editor.console.setText(Editor.console.getText()
                + "\nError: (line: " + node.getData().getLine() + ", column: " + node.getData().getColumn() + ")\n"
                + "    " + (++semanticErrors) + "==> " + "Variable '" + node.getData().getLexeme() + "' might have not been initialized."
                + "\n");
    }

    private static void reportVariableNotDeclared(Node node) {
        Editor.console.setText(Editor.console.getText()
                + "\nError: (line: " + node.getData().getLine() + ", column: " + node.getData().getColumn() + ")\n"
                + "    " + (++semanticErrors) + "==> " + "Variable '" + node.getData().getLexeme() + "' has not been declared."
                + "\n");
    }

    private static void reportVariableAlreadyDeclared(Node node) {
        Editor.console.setText(Editor.console.getText()
                + "\nError: (line: " + node.getData().getLine() + ", column: " + node.getData().getColumn() + ")\n"
                + "    " + (++semanticErrors) + "==> " + "Variable '" + node.getData().getLexeme() + "' has already been declared."
                + "\n");
    }

    private static void reportTypeMismatch(Node found, String required) {
        Editor.console.setText(Editor.console.getText()
                + "\nError: (line: " + found.getData().getLine() + ", column: " + found.getData().getColumn() + ")\n"
                + "    " + (++semanticErrors) + "==> " + "INCOMPATIBLE types"
                + "\n" + "        " + " found: " + found.getData().getType()
                + "\n" + "        " + " required: " + required
                + "\n");
    }
}
