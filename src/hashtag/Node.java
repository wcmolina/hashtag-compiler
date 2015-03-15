/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hashtag;

import java.util.ArrayList;
import java.util.List;
/*
falta:
*/
public class Node {

    private List<Node> children;
    public String label;
    private Object type;

    public Node(String name, Node child) {
        label = name;
        children = new ArrayList();
        children.add(child);
    }
    
    public Node(String name, Node left, Node right) { //mostly for binary operators +,-,*,/
        label = name;
        children = new ArrayList();
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

    public List<Node> getChildren() {
        if (children == null) {
            children = new ArrayList();
        }
        return children;
    }

    public void addChild(Node child) {
        getChildren().add(child);
    }

    public void setChildren(ArrayList<Node> c) {
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
