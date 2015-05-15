package com.compiler.tools;

import com.compiler.hashtag.Editor;
import com.compiler.hashtag.Node;
import com.compiler.hashtag.Scope;

import java.io.Writer;
import java.util.Arrays;
import java.util.Stack;

/**
 * Created by Wilmer Carranza on 01/05/2015.
 */
//todo: work on arithmetic expression while type checking!

public class TreeAnalyzer {
    private Node root = null;
    Writer writer = null;
    private static final String OPERATORS[] = {"+", "-", "*", "/", "%"};
    public int semantic_errors = 0;
    private Stack<Scope> scopeStack = new Stack<>();

    public TreeAnalyzer(Node root) {

        this.root = root;
        traverse(root);
    }

    public void traverse(Node node) {
        if (!node.isLeaf()) {
            for (Node child : node.getChildren()) {
                if (child.label.equalsIgnoreCase("init")) {
                    //declarations(child);
                    init_handler(child);
                } else if (child.label.equalsIgnoreCase("body")) {
                    //
                } else if (child.label.equalsIgnoreCase("")) {
                    //
                } else if (child.label.equalsIgnoreCase("")) {
                    //
                } else traverse(child);
            }
        }
    }

    private void declarations(Node declare) {
        for (Node child : declare.getChildren()) {
            System.out.println(child.getData().tabularData());
        }
    }

    private void init_handler(Node init) {
        Node left, right;
        right = init.getChildren().get(0); //value, which is actually a declare node, but it holds info on the type (as well as its children)
        left = init.getChildren().get(1); //id
        if (!Arrays.asList(OPERATORS).contains(right.label)) { //if true, then the assign value is an arithmetic expression
            if (!right.getData().getType().equalsIgnoreCase(left.getData().getType())) {
                //show an error
                Editor.console.setText(Editor.console.getText()
                        + "\nError: (line: " + right.getData().getLine() + ", column: " + right.getData().getColumn() + ")\n"
                        + "    " + (++semantic_errors) + "==> " + "INCOMPATIBLE types"
                        + "\n" + "        " + " found: " + right.getData().getType()
                        + "\n" + "        " + " required: " + left.getData().getType()
                        + "\n");
            }
        }
    }
}
