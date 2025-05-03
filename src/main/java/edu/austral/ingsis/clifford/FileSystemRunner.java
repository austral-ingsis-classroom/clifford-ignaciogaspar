package edu.austral.ingsis.clifford;

import edu.austral.ingsis.clifford.command.Command;
import java.util.ArrayList;
import java.util.List;

public class FileSystemRunner {

  private final CommandParser parser;

  public FileSystemRunner(CommandParser parser) {
    this.parser = parser;
  }

  public List<String> executeCommands(List<String> commands) {
    FileSystem fileSystem = new FileSystem();
    List<String> results = new ArrayList<>();
    for (String command : commands) {
      Result<Command> result = parser.parse(command);
      switch (result) {
        case Result.Success<Command> success -> {
          Command cmd = success.getValue();
          Result<FileSystem> executionResult = cmd.execute(fileSystem);

          switch (executionResult) {
            case Result.Success<FileSystem> execSuccess -> {
              results.add(execSuccess.getMessage());
              fileSystem = execSuccess.getValue();
            }
            case Result.Error<?> execError -> results.add(execError.getMessage());
          }
        }
        case Result.Error<?> error -> results.add(error.getMessage());
      }
    }
    return results;
  }
}
