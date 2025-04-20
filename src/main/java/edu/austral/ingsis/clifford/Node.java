package edu.austral.ingsis.clifford;

public interface Node extends Comparable<Node> {
    String getName();
    String getPath();
    String getType();

    @Override
    default int compareTo(Node other) {
        return this.getName().compareTo(other.getName());
    }

    default String getFullPath() {
        return (getPath().equals("/") ? "" : getPath()) + "/" + getName();
    }
}
