package com.compiler.tools;

import com.compiler.hashtag.Node;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Wilmer Carranza on 01/05/2015.
 */

/*
* todo: find solution for adding the type to each child in node 'declare', the type is only added to the last one.
* */
public class TreeAnalyzer {
    private Node root = null;
    Writer writer = null;

    public TreeAnalyzer(Node root) {
        this.root = root;
        walk(root);
    }

    public void walk(Node node) {
        if (!node.isLeaf()) {
            for (Node child : node.getChildren()) {
                if (child.label.equalsIgnoreCase("declare")) {
                    declarations(child);
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
}
