package edu.austral.ingsis.clifford;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class InMemoryFileSystem {

    private final Directory root = new Directory("", null);
    private Directory cwd = root;

    public String execute(String line) {
        String[] tokens = line.trim().split("\\s+");
        String cmd = tokens[0];

        switch (cmd) {
            case "pwd":
                return pwd();

            case "ls": {
                Optional<String> ord = Arrays.stream(tokens)
                        .filter(t -> t.startsWith("--ord="))
                        .map(t -> t.substring(6))
                        .findFirst();
                return ls(ord);
            }

            case "mkdir": {
                String name = tokens.length > 1 ? tokens[1] : "";
                return mkdir(name);
            }

            case "touch": {
                String name = tokens.length > 1 ? tokens[1] : "";
                return touch(name);
            }

            case "cd": {
                String path = tokens.length > 1 ? tokens[1] : "";
                return cd(path);
            }

            case "rm": {
                boolean recursive = false;
                String name = "";
                for (int i = 1; i < tokens.length; i++) {
                    if (tokens[i].equals("--recursive")) {
                        recursive = true;
                    } else {
                        name = tokens[i];
                        break;
                    }
                }
                return rm(name, recursive);
            }

            default:
                return "unknown command";
        }
    }

    private String pwd() {
        return cwd.getFullPath();
    }

    private String ls(Optional<String> ord) {
        Stream<Node> stream;
        if (ord.isPresent()) {
            Comparator<Node> cmp = Comparator.comparing(Node::getName);
            if (ord.get().equals("desc")) {
                cmp = cmp.reversed();
            }
            stream = cwd.getChildren().stream().sorted(cmp);
        } else {
            stream = cwd.getChildren().stream();
        }
        return stream
                .map(Node::getName)
                .collect(Collectors.joining(" "));
    }

    private String mkdir(String name) {
        if (!validName(name)) {
            return "invalid name";
        }
        if (cwd.findChild(name) != null) {
            return "'" + name + "' already exists";
        }
        cwd.addChild(new Directory(name, cwd));
        return "'" + name + "' directory created";
    }

    private String touch(String name) {
        if (!validName(name)) {
            return "invalid name";
        }
        if (cwd.findChild(name) != null) {
            return "'" + name + "' already exists";
        }
        cwd.addChild(new File(name, cwd));
        return "'" + name + "' file created";
    }

    private String cd(String rawPath) {
        try {
            Directory target = resolveDir(rawPath);
            cwd = target;
            String dest;
            if (cwd == root) {
                dest = "/";
            } else {
                dest = cwd.getName();
            }
            return "moved to directory '" + dest + "'";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    private String rm(String name, boolean recursive) {
        Node node = cwd.findChild(name);
        if (node == null) {
            return "'" + name + "' does not exist";
        }
        if (node instanceof Directory && !recursive) {
            return "cannot remove '" + name + "', is a directory";
        }
        cwd.removeChild(node);
        return "'" + name + "' removed";
    }

    private boolean validName(String n) {
        return !n.contains("/") && !n.contains(" ");
    }

    private Directory resolveDir(String raw) {
        if (raw.equals(".")) {
            return cwd;
        }
        if (raw.equals("..")) {
            if (cwd.getParent() == null) {
                return root;
            }
            return cwd.getParent();
        }
        if (raw.equals("/")) {
            return root;
        }

        Directory d = raw.startsWith("/") ? root : cwd;
        for (String seg : raw.split("/")) {
            if (seg.isEmpty() || seg.equals(".")) {
                continue;
            }
            if (seg.equals("..")) {
                if (d.getParent() == null) {
                    d = root;
                } else {
                    d = d.getParent();
                }
            } else {
                Node child = d.findChild(seg);
                if (child == null || !child.getType().equals("directory")) {
                    throw new IllegalArgumentException("'" + raw + "' directory does not exist");
                }
                d = (Directory) child;
            }
        }
        return d;
    }
}