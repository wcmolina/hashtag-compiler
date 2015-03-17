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

    public Node(String name, Node child) {
        if (child == null) {
            child = new Node("null child");
            children = new ArrayList();
            children.add(child);
        } else {
            label = name;
            children = new ArrayList();
            children.add(child);
        }
    }

    public Node(String name, Node child, Object obj) {
        if (child == null) {
            child = new Node("null child");
        }
        label = name;
        children = new ArrayList();
        children.add(child);
        type = obj;
    }

    public Node(String name, Node left, Node right) { //mostly for binary operators like +,-,*,/,%....
        label = name;
        children = new ArrayList();
        if (left == null) {
            left = new Node();
        }
        if (right == null) {
            right = new Node();
        }
        children.add(left);
        children.add(right);
    }

    public Node(String name) {
        label = name;
        children = new ArrayList();
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

    public Node addChild(Node child) {
        getChildren().add(child);
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
