/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hashtag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*
 falta:
 */

public class Node {

    private ArrayList<Node> children;
    public String label;
    private Object type;

    public Node() {
        label = "null node";
        children = new ArrayList();
    }

    public Node(String name, Node... nodes) {
        label = name;
        children = new ArrayList();
        for (Node node : nodes) {
            if (node == null) {
                children.add(new Node("null child"));
            } else {
                children.add(node);
            }
        }
    }

    public Node(Object obj, String name) {
        label = name;
        children = new ArrayList();
        type = obj;
    }

    public Node(String name, ArrayList<Node> c) {
        label = name;
        children = new ArrayList();
        children.addAll(c);
    }

    public ArrayList<Node> getChildren() {
        if (children == null) {
            children = new ArrayList();
        }
        return children;
    }

    public Node add(Node... children) {
        for (Node child : children) {
            getChildren().add(child);
        }
        return this;
    }

    public void setChildren(ArrayList<Node> c) {
        if (children == null) {
            children = new ArrayList();
        }
        this.children.clear();
        this.children.addAll(c);
    }

    public void print(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + label);
        if (!children.isEmpty()) {
            for (int i = 0; i < children.size() - 1; i++) {
                children.get(i).print(prefix + (isTail ? "    " : "│   "), false);
            }
        }
        if (children.size() > 0) {
            children.get(children.size() - 1).print(prefix + (isTail ? "    " : "│   "), true);
        }
    }
}
