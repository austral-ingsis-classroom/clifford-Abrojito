package edu.austral.ingsis.clifford;


public final class File implements Node {

    private final String name;
    private final Directory parent;

    public File(String name, Directory parent) {
        this.name = name;
        this.parent = parent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPath() {
        return parent.getFullPath();
    }

    @Override
    public String getType() {
        return "file";
    }

    public Directory getParent() {
        return parent;
    }
}