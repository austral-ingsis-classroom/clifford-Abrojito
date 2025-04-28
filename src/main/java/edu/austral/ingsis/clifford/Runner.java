package edu.austral.ingsis.clifford;

import java.util.List;

public final class Runner implements FileSystemRunner {
    private final InMemoryFileSystem fs = new InMemoryFileSystem();

    @Override
    public List<String> executeCommands(List<String> commands) {
        return commands.stream()
                .map(fs::execute)
                .toList();
    }
}
