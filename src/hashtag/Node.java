/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hashtag;

import java.util.ArrayList;
import java.util.List;

public class Node {

    private List<Node> children;
    private String label;

    public Node(String name, Object pValue) {
        label = name;
    }

    public Node(String name) {
        label = name;
        children = new ArrayList<Node>();
    }

    public List<Node> getChildren() {
        if (children == null) {
            children = new ArrayList<Node>();
        }
        return children;
    }

    public void addChild(Node pNode) {
        getChildren().add(pNode);
    }

    public void setChildren(ArrayList<Node> c) {
        this.children.clear();
        this.children.addAll(c);
    }

    public String getLabel() {
        return label;
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
