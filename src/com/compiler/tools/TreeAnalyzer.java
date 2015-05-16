package com.compiler.tools;

import com.compiler.hashtag.Data;
import com.compiler.hashtag.Editor;
import com.compiler.hashtag.Node;
import com.compiler.hashtag.Scope;

import java.util.Arrays;
import java.util.Stack;

/**
 * Created by Wilmer Carranza on 01/05/2015.
 */
//todo: work on arithmetic expression while type checking!

public class TreeAnalyzer {

    private static final String OPERATORS[] = {"+", "-", "*", "/", "%"};
    private static final String BLOCK_STATEMENTS[] = {"PROG", "MAIN", "IF", "ELSE", "SWITCH", "FOR", "WHILE"};
    public int semantic_errors = 0;
    private Stack<Scope> scope_stack = new Stack<Scope>();

    public TreeAnalyzer(Node root) {
        traverse(root);
    }

    public void traverse(Node node) {
        if (!node.isLeaf()) {
            if (Arrays.asList(BLOCK_STATEMENTS).contains(node.label)) { //new scopes if it finds a block stmts or main or prog or functions.
                setup_stack(node.label);
                for (Node child : node.getChildren()) { //prog can have 2 children, as well as IF, WHILE, SWITCH, etc...
                    traverse(child);
                    //block_handler? where it can detect the type of block being sent as a parameter...
                }
            } else {
                if (node.label.equalsIgnoreCase("body")) {
                    for (Node body_node : node.getChildren()) {
                        traverse(body_node);
                    }
                    scope_stack.pop();
                } else if (node.label.equalsIgnoreCase("init")) {
                    init_handler(node);
                } else if (node.label.equalsIgnoreCase("declare")) {
                    declare_handler(node);
                } else if (node.label.equalsIgnoreCase("assign")) {
                    assign_handler(node);
                } else {
                    System.out.println("no matches on ifs: " + node.label);
                }
            }
        }
    }

    private void setup_stack(String label) {
        Scope current;
        //create a new Scope, which should be the current one with the previous one as its parent
        current = (scope_stack.empty()) ? new Scope(null) : new Scope(scope_stack.peek());
        current.setLabel(label);
        //Now add the newly created Scope as a child to the previous Scope.
        if (!scope_stack.empty()) {
            scope_stack.peek().add_scope(current);
        }
        //now push the new Scope to the stack.
        scope_stack.push(current);
    }

    public void assign_handler(Node assign) {
        Node var, value;
        value = assign.getChildren().get(0);
        var = assign.getChildren().get(1);
        //gotta find this ID in the ST
        Data var_data = Scope.find_in_previous(scope_stack.peek(), var.label);
        if (var_data != null) {
            //found it, now compare the types
            //todo: find out if i should update its data once found to the assigned value or what....
            if (!Arrays.asList(OPERATORS).contains(value.label)) { //if its not an arithmetic expression.
                if (!value.getData().getType().equalsIgnoreCase(var_data.getType())) {
                    Editor.console.setText(Editor.console.getText()
                            + "\nError: (line: " + value.getData().getLine() + ", column: " + value.getData().getColumn() + ")\n"
                            + "    " + (++semantic_errors) + "==> " + "INCOMPATIBLE types"
                            + "\n" + "        " + " found: " + value.getData().getType()
                            + "\n" + "        " + " required: " + var_data.getType()
                            + "\n");
                }
            }
        } else {
            Editor.console.setText(Editor.console.getText()
                    + "\nError: (line: " + var.getData().getLine() + ", column: " + var.getData().getColumn() + ")\n"
                    + "    " + (++semantic_errors) + "==> " + "Variable '" + var.getData().getLexeme() + "' has not been declared."
                    + "\n");
        }
    }

    private void declare_handler(Node declare) {
        Data data;
        for (Node child : declare.getChildren()) {
            data = child.getData();
            if (!Scope.is_in_previous(scope_stack.peek(), data.getLexeme())) {
                scope_stack.peek().put(data.getLexeme(), data);
            } else {
                Editor.console.setText(Editor.console.getText()
                        + "\nError: (line: " + data.getLine() + ", column: " + data.getColumn() + ")\n"
                        + "    " + (++semantic_errors) + "==> " + "Variable '" + data.getLexeme() + "' has already been declared."
                        + "\n");
            }
        }
    }

    private void init_handler(Node init) {
        Node var, value;
        value = init.getChildren().get(0); //value
        var = init.getChildren().get(1); //id, which is actually a declare node, but it holds info on the type (as well as its children)
        //adding info to the table
        Data data;
        for (Node child : var.getChildren()) {
            data = child.getData();
            if (!Scope.is_in_previous(scope_stack.peek(), data.getLexeme())) {
                scope_stack.peek().put(data.getLexeme(), data);
            } else {
                Editor.console.setText(Editor.console.getText()
                        + "\nError: (line: " + data.getLine() + ", column: " + data.getColumn() + ")\n"
                        + "    " + (++semantic_errors) + "==> " + "Variable '" + data.getLexeme() + "' has already been declared."
                        + "\n");
            }
        }
        if (!Arrays.asList(OPERATORS).contains(value.label)) { //if true, then the assign value is an arithmetic expression
            if (!value.getData().getType().equalsIgnoreCase(var.getData().getType())) {
                //type error
                Editor.console.setText(Editor.console.getText()
                        + "\nError: (line: " + value.getData().getLine() + ", column: " + value.getData().getColumn() + ")\n"
                        + "    " + (++semantic_errors) + "==> " + "INCOMPATIBLE types"
                        + "\n" + "        " + " found: " + value.getData().getType()
                        + "\n" + "        " + " required: " + var.getData().getType()
                        + "\n");
            }
        }
    }
}
