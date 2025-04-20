package edu.austral.ingsis.clifford.commands;

import edu.austral.ingsis.clifford.Command;
import edu.austral.ingsis.clifford.Memory;

public class Pwd implements Command {
    @Override
    public String run(Memory fs, String[] args) {
        return fs.pwd();
    }
}
