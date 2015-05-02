package com.compiler.tools;

import com.compiler.hashtag.Node;

import java.util.ArrayList;

/**
 * Created by Wilmer Carranza on 01/05/2015.
 */
public class TreeAnalyzer {
    private ArrayList<Node> tree = null;

    public TreeAnalyzer(ArrayList<Node> AST) {
        tree = AST;
    }
}
