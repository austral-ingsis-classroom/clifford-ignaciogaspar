package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.Result;
import java.util.ArrayList;

public class Mkdir implements Command {
  private String directoryName;

  public Mkdir(String directoryName) {
    this.directoryName = directoryName;
  }

  @Override
  public Result execute(FileSystem fileSystem) {
    if (directoryName.contains("/") || directoryName.contains(" ")) {
      return new Result.Error("Invalid directory name: " + directoryName);
    }

    Directory newDirectory =
        new Directory(directoryName, fileSystem.getCurrentDirectory(), new ArrayList<>());
    fileSystem.getCurrentDirectory().getChildren().add(newDirectory);
    return new Result.Success<>("'" + directoryName + "' directory created");
  }
}
