package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.Command;
import edu.austral.ingsis.clifford.Memory;

public class Touch implements Command {
    @Override
    public String run(Memory fs, String[] args) {
        if (args.length == 0) return "missing file name";
        return fs.touch(args[0]);
    }
}
