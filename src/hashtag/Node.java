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
    public String print;

    public Node() {
        print = "";
        label = "null node";
        children = new ArrayList();
    }

    public Node(String name, Node... nodes) {
        print = "";
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
        print = "";
        label = name;
        children = new ArrayList();
        type = obj;
    }

    public Node(String name, ArrayList<Node> c) {
        print = "";
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

    public String print(String prefix, boolean isTail) {
        print = print.concat(prefix + (isTail ? "└── " : "├── ") + label) + "\n";
        if (!children.isEmpty()) {
            for (int i = 0; i < children.size() - 1; i++) {
                print = print.concat(children.get(i).print(prefix + (isTail ? "    " : "│   "), false));
            }
        }
        if (children.size() > 0) {
            print = print.concat(children.get(children.size() - 1).print(prefix + (isTail ? "    " : "│   "), true));
        }
        return print;
    }
}
