package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.Command;
import edu.austral.ingsis.clifford.Memory;

import java.util.Arrays;

public class Rm implements Command {
    @Override
    public String run(Memory fs, String[] args) {
        boolean recursive = Arrays.stream(args).anyMatch("--recursive"::equals);
        String target = Arrays.stream(args)
                .filter(a -> !a.startsWith("--"))
                .findFirst().orElse("");
        if (target.isEmpty()) return "missing file";
        return fs.rm(target, recursive);
    }
}
