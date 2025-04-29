package edu.austral.ingsis.clifford;

import edu.austral.ingsis.clifford.command.Command;
import java.util.ArrayList;
import java.util.List;

public class FileSystemRunner{

    private final CommandParser parser;
    private final FileSystem fileSystem;

    public FileSystemRunner(CommandParser parser, FileSystem fileSystem) {
        this.parser = parser;
        this.fileSystem = fileSystem;
    }

    public List<String> executeCommands(List<String> commands) {
        List<String> results = new ArrayList<>();
        for (String command : commands) {
            Result result = parser.parse(command);
            if (result instanceof Result.Success) {
                Command cmd = (Command) ((Result.Success<?>) result).getValue();
                Result executionResult = cmd.execute(fileSystem);
                if (executionResult instanceof Result.Success) {
                    results.add(((Result.Success<?>) executionResult).getValue().toString());
                } else if (executionResult instanceof Result.Error) {
                    results.add(((Result.Error) executionResult).getMessage());
                }
            } else if (result instanceof Result.Error) {
                results.add(((Result.Error) result).getMessage());
            }
        }
        return results;
    }
}