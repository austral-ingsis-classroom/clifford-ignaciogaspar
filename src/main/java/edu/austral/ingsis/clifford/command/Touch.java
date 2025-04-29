package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.File;
import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.Result;

public class Touch implements Command {
  private String fileName;

  public Touch(String fileName) {
    this.fileName = fileName;
  }

  @Override
  public Result execute(FileSystem fileSystem) {
    if (fileName.contains("/") || fileName.contains(" ")) {
      return new Result.Error("Invalid file name: " + fileName);
    }

    File newFile = new File(fileName, fileSystem.getCurrentDirectory());
    fileSystem.getCurrentDirectory().getChildren().add(newFile);
    return new Result.Success<>("'" + fileName + "' file created");
  }
}
