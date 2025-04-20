package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.Command;
import edu.austral.ingsis.clifford.Memory;

public class Mkdir implements Command {

    @Override
    public String run(Memory fs, String[] args) {
        if (args.length == 0) return "missing directory name";
        return fs.mkdir(args[0]);
    }
}
