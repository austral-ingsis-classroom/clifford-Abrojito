package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;

public final class Directory implements Node {
    private final String name;
    private final Directory parent;
    private final List<Node> children = new ArrayList<>();


    public Directory(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return parent==null ? "/" : parent.getFullPath();
    }

    public Directory getParent() {
        return parent;
    }

    public String getType() {
        return "directory";
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public List<Node> getChildren() { return children; }

    public void removeChild(Node child) {
        children.remove(child);
    }

    public Node findChild(String name) {
        for (Node n : children) {
            if (n.getName().equals(name)) {
                return n;
            }
        }
        return null;
    }
}
