// File: java/edu/austral/ingsis/clifford/Memory.java
package edu.austral.ingsis.clifford;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;

public class Memory implements FileSystemRunner {

    private static class Node {
        String name;
        boolean isDirectory;
        Map<String, Node> children;
        Node parent;

        Node(String name, boolean isDirectory, Node parent) {
            this.name = name;
            this.isDirectory = isDirectory;
            this.parent = parent;
            if (isDirectory) {
                // Mantener orden de inserción
                this.children = new LinkedHashMap<>();
            }
        }
    }

    private Node root;
    private Node current;

    public Memory() {
        root = new Node("/", true, null);
        current = root;
    }

    @Override
    public List<String> executeCommands(List<String> commands) {
        List<String> results = new ArrayList<>();
        for (String command : commands) {
            results.add(executeCommand(command));
        }
        return results;
    }

    // Método para el comando pwd
    public String pwd() {
        return handlePwd();
    }

    // Método adicional para ls que permite recibir un Optional para el orden
    public String ls(Optional<String> ord) {
        String command = "ls";
        if (ord.isPresent()) {
            command += " --ord=" + ord.get();
        }
        return handleLs(command);
    }

    // Métodos públicos agregados para los comandos individuales

    public String rm(String target, boolean recursive) {
        if (recursive) {
            return handleRm("--recursive " + target);
        } else {
            return handleRm(target);
        }
    }

    public String mkdir(String name) {
        return handleMkdir(name);
    }

    public String touch(String name) {
        return handleTouch(name);
    }

    public String cd(String path) {
        return handleCd(path);
    }

    private String executeCommand(String command) {
        if (command.startsWith("ls")) {
            return handleLs(command);
        } else if (command.startsWith("mkdir ")) {
            return handleMkdir(command.substring(6).trim());
        } else if (command.startsWith("cd ")) {
            return handleCd(command.substring(3).trim());
        } else if (command.equals("pwd")) {
            return handlePwd();
        } else if (command.startsWith("touch ")) {
            return handleTouch(command.substring(6).trim());
        } else if (command.startsWith("rm ")) {
            return handleRm(command.substring(3).trim());
        }
        return "";
    }

    private String handleLs(String command) {
        List<String> names = new ArrayList<>(current.children.keySet());
        if (command.contains("--ord=")) {
            String ord = command.substring(command.indexOf("--ord=") + 6).trim();
            Collections.sort(names);
            if (ord.equals("desc")) {
                Collections.reverse(names);
            }
        }
        StringJoiner joiner = new StringJoiner(" ");
        for (String name : names) {
            joiner.add(name);
        }
        return joiner.toString();
    }

    private String handleMkdir(String name) {
        if (!current.children.containsKey(name)) {
            Node dir = new Node(name, true, current);
            current.children.put(name, dir);
        }
        return "\\" + name + "\\ directory created";
    }

    private String handleCd(String path) {
        Node target;
        if (path.equals("/")) {
            current = root;
            return "moved to directory '/'";
        } else if (path.equals("..")) {
            if (current.parent != null) {
                current = current.parent;
            }
            return "moved to directory '" + (current == root ? "/" : current.name) + "'";
        } else {
            target = traversePath(path);
            if (target == null || !target.isDirectory) {
                String[] parts = path.split("/");
                String errorName = parts[parts.length - 1];
                return "\\" + errorName + "\\ directory does not exist";
            } else {
                current = target;
                return "moved to directory '" + current.name + "'";
            }
        }
    }

    private Node traversePath(String path) {
        Node temp = (path.startsWith("/")) ? root : current;
        String[] parts = path.split("/");
        for (String part : parts) {
            if (part.isEmpty() || part.equals(".")) continue;
            if (part.equals("..")) {
                temp = (temp.parent != null) ? temp.parent : temp;
            } else {
                if (temp.children.containsKey(part) && temp.children.get(part).isDirectory) {
                    temp = temp.children.get(part);
                } else {
                    return null;
                }
            }
        }
        return temp;
    }

    private String handlePwd() {
        if (current == root) return "/";
        List<String> parts = new ArrayList<>();
        Node temp = current;
        while (temp != null && temp.parent != null) {
            parts.add(temp.name);
            temp = temp.parent;
        }
        Collections.reverse(parts);
        StringJoiner joiner = new StringJoiner("/", "/", "");
        for (String part : parts) {
            joiner.add(part);
        }
        return joiner.toString();
    }

    private String handleTouch(String name) {
        if (!current.children.containsKey(name)) {
            Node file = new Node(name, false, current);
            current.children.put(name, file);
        }
        return "\\" + name + "\\ file created";
    }

    private String handleRm(String params) {
        if (params.startsWith("--recursive ")) {
            String name = params.substring(12).trim();
            Node node = current.children.get(name);
            if (node != null && node.isDirectory) {
                current.children.remove(name);
                return "\\" + name + "\\ removed";
            }
        } else {
            String name = params.trim();
            Node node = current.children.get(name);
            if (node != null) {
                if (node.isDirectory) {
                    return "cannot remove \\" + name + "\\, is a directory";
                } else {
                    current.children.remove(name);
                    return "\\" + name + "\\ removed";
                }
            }
        }
        return "";
    }
}