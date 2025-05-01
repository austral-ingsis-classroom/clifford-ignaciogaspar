package edu.austral.ingsis.clifford;

import edu.austral.ingsis.clifford.command.Command;
import java.util.ArrayList;
import java.util.List;

public class FileSystemRunner {

  private final CommandParser parser;
  private final FileSystem fileSystem;

  public FileSystemRunner(CommandParser parser, FileSystem fileSystem) {
    this.parser = parser;
    this.fileSystem = fileSystem;
  }

  public List<String> executeCommands(List<String> commands) {
    List<String> results = new ArrayList<>();
    for (String command : commands) {
      Result<Command> result = parser.parse(command);

      switch (result) {
        case Result.Success<Command> success -> {
          Command cmd = success.getValue();
          Result<?> executionResult = cmd.execute(fileSystem);

          switch (executionResult) {
            case Result.Success<?> execSuccess ->
                    results.add(execSuccess.getMessage());
            case Result.Error<?> execError ->
                    results.add(execError.getMessage());
          }
        }
        case Result.Error<?> error ->
                results.add(error.getMessage());
      }
    }
    return results;
  }
}
