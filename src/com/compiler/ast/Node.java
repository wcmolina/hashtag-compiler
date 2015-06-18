package com.compiler.ast;

import com.compiler.hashtag.SemanticAnalyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Node {

    private Node parent;
    private ArrayList<Node> children;
    public String label;
    private Data data;
    public String print = "";

    //constructor with no data recieved
    public Node(String name, Node... nodes) {
        label = name;
        children = new ArrayList();
        data = new Data();
        for (Node node : nodes) {
            if (node == null) {
                children.add(new Node("null"));
            } else {
                children.add(node);
            }
        }
    }

    //constructor with data
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
        if (data == null) {
            data = new Data();
        }
        return this.data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void inverse() {
        Collections.reverse(children);
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public static Node findInPrevious(Node parent, String... args) {
        if (Arrays.asList(args).contains(parent.getData().getType().toLowerCase())) {
            return parent;
        } else {
            if (parent.getParent() == null) {
                return null;
            } else
                return findInPrevious(parent.getParent(), args);
        }
    }

    /**
     * @return Returns the parent <code>Node</code> of the referenced <code>Node</code>
     */

    public Node getParent() {
        return parent;
    }

    /**
     * <p>
     * This method is used to set connections between parent nodes and their children.
     * When parsing (with Cup) is done, each node of the AST can access only to their children,
     * but not to its parent (this is because of the way the AST is generated: from the grammar
     * itself). <code>setConnection</code> fixes this problem by recursively setting the parent
     * for each node of the AST.
     * </p>
     *
     * @param parent The parent of the node. <code>null</code> means the node is root.
     * @param child  The child of the parent node. <code>null</code> may result in a <code>NullPointerException</code>
     */

    public static void setConnection(Node parent, Node child) {
        child.setParent(parent);
        if (!child.isLeaf()) {
            for (Node node : child.getChildren()) {
                setConnection(child, node);
            }
        }
    }

    /**
     * Add nodes to the <code>ArrayList</code> of children of a specific node.
     *
     * @param nodes An array of <code>Node</code>.
     * @return Returns the node with its updated children.
     */

    public Node add(Node... nodes) {
        for (Node child : nodes) {
            children.add(child);
        }
        return this;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public String toString(String prefix, boolean isTail) {
        if (this.label.equalsIgnoreCase("body")
                || SemanticAnalyzer.ARITHMETIC_OPERATORS.contains(this.label)
                || SemanticAnalyzer.LOGICAL_OPERATORS.contains(this.label)
                || SemanticAnalyzer.COMPARISON_OPERATORS.contains(this.label))
            inverse();
        print = print.concat(prefix + (isTail ? "└── " : "├── ") + label) + "\n";
        if (!children.isEmpty()) {
            for (int i = 0; i < children.size() - 1; i++) {
                print = print.concat(children.get(i).toString(prefix + (isTail ? "    " : "│   "), false));
            }
        }
        if (children.size() > 0) {
            print = print.concat(children.get(children.size() - 1).toString(prefix + (isTail ? "    " : "│   "), true));
        }
        return print;
    }

    @Override
    public String toString() {
        return this.label;
    }
}
