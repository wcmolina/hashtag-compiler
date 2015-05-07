package com.compiler.hashtag;

import java.util.ArrayList;
import java.util.Collections;

public class Node {

    private ArrayList<Node> children;
    public String label;
    private Data data;
    public String print = "";

    //constructor with no data recieved
    public Node(String name, Node... nodes) {
        label = name;
        children = new ArrayList();
        data = null;
        for (Node node : nodes) {
            if (node == null) {
                children.add(new Node("null child"));
            } else {
                children.add(node);
            }
        }
    }

    //constructor with data (lexeme, token, line, column, etc.)
    public Node(Data dat, String name, Node... nodes) {
        label = name;
        children = new ArrayList();
        for (Node node : nodes) {
            if (node == null) {
                children.add(new Node("null child"));
            } else {
                children.add(node);
            }
        }
        data = dat;
    }

    public ArrayList<Node> getChildren() {
        if (children == null) {
            children = new ArrayList();
        }
        return children;
    }

    public Data getData() {
        return this.data;
    }

    public void inverse() {
        Collections.reverse(children);
    }

    public Node add(Node... nodes) {
        //Collections.reverse(Arrays.asList(children));
        for (Node child : nodes) {
            children.add(child);
        }
        return this;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public String treeToString(String prefix, boolean isTail) {
        //if (!this.label.equalsIgnoreCase("prog")) inverse();
        inverse();
        print = print.concat(prefix + (isTail ? "└── " : "├── ") + label) + "\n";
        if (!children.isEmpty()) {
            for (int i = 0; i < children.size() - 1; i++) {
                print = print.concat(children.get(i).treeToString(prefix + (isTail ? "    " : "│   "), false));
            }
        }
        if (children.size() > 0) {
            print = print.concat(children.get(children.size() - 1).treeToString(prefix + (isTail ? "    " : "│   "), true));
        }
        return print;
    }
}
