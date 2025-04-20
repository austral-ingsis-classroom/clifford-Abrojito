package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.Command;
import edu.austral.ingsis.clifford.Memory;

import java.util.Optional;

public class Ls implements Command {

    @Override
    public String run(Memory fs, String[] args) {
        Optional<String> ord = Optional.empty();
        if (args.length == 1 && args[0].startsWith("--ord="))
            ord = Optional.of(args[0].substring(6));    // "asc" | "desc"
        return fs.ls(ord);
    }
}