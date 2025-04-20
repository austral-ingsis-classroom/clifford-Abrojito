package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.List;

public final class Directory implements Node {

    private final String name;
    private final Directory parent;
    private final List<Node> children;


    public Directory(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
        this.children = new ArrayList<>();;
    }


    public String getName() {
        return name;
    }

    public String getPath() {
        return parent == null ? "" : parent.getFullPath();
    }

    public String getType() {
        return "directory";
    }

    public void add(Node n){
        children.add(n);
    }

    public void remove(Node n){
        children.remove(n);
    }

    public List<Node> children() {
        return children;
    }

    public Directory getParent() {
        return parent;
    }

    public boolean isEmpty() {
        return children.isEmpty();
    }

    public Node find(String name) {
        return children.stream()
                .filter(c -> c.getName().equals(name))
                .findFirst().orElse(null);
    }

    public String getFullPath() {
        if (parent == null) return "/";
        String parentPath = parent.getFullPath();
        return ("/".equals(parentPath)) ? parentPath + getName() : parentPath + "/" + getName();
    }
}
