package com.compiler.tools;

import com.compiler.hashtag.Editor;
import com.compiler.hashtag.Node;

import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Wilmer Carranza on 01/05/2015.
 */

public class TreeAnalyzer {
    private Node root = null;
    Writer writer = null;
    private final Map<String, Class> mapTypes = new HashMap<String, Class>();
    private static final String OPERATORS[] = {"+", "-", "*", "/", "%"};
    public int semantic_errors = 0;

    public TreeAnalyzer(Node root) {
        mapTypes.put("int", Integer.TYPE);
        this.root = root;
        walk(root);
    }

    public void walk(Node node) {
        if (!node.isLeaf()) {
            for (Node child : node.getChildren()) {
                if (child.label.equalsIgnoreCase("init")) {
                    //declarations(child);
                    init(child);
                } else {
                    walk(child);
                }
            }
        }
    }

    public void declarations(Node declare) {
        for (Node child : declare.getChildren()) {
            System.out.println(child.getData().tabularData());
        }
    }

    public void init(Node init) {
        Node left, right;
        right = init.getChildren().get(0); //value
        left = init.getChildren().get(1); //id
        if (!Arrays.asList(OPERATORS).contains(right.label)) { //if true, then the assign value is an arithmetic expression
            if (!right.getData().getType().equalsIgnoreCase(left.getData().getType())) {
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
