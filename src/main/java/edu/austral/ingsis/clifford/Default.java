package edu.austral.ingsis.clifford;

import java.nio.file.FileVisitor;
import java.util.List;

public class Default implements FileSystemRunner {

    private final FileSystemRunner fs = new Memory();

    @Override
    public List<String> executeCommands(List<String> commands) {
        return fs.executeCommands(commands);
    }
}
