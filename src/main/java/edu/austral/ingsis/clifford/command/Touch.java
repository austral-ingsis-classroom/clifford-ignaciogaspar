package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.File;
import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.Result;

public class Touch implements Command {
  private final String fileName;

  public Touch(String fileName) {
    this.fileName = fileName;
  }

  @Override
  public Result<FileSystem> execute(FileSystem fileSystem) {
    if (fileName.contains("/") || fileName.contains(" ")) {
      return new Result.Error<>("invalid file name: " + fileName);
    }

    File newFile = new File(fileName, fileSystem.getCurrentDirectory().getPath().equals("/") ? "/" + fileName : fileSystem.getCurrentDirectory().getPath() + "/" + fileName);
    Directory updatedCurrentDirectory = fileSystem.getCurrentDirectory().withChild(newFile);
    return new Result.Success<>(new FileSystem(fileSystem.getRoot(), updatedCurrentDirectory), "'" + fileName + "' file created");
  }
}
