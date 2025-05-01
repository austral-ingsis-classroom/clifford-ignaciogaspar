package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.Result;
import java.util.ArrayList;

public class Mkdir implements Command {
  private final String directoryName;

  public Mkdir(String directoryName) {
    this.directoryName = directoryName;
  }

  @Override
  public Result<FileSystem> execute(FileSystem fileSystem) {
    if (directoryName.contains("/") || directoryName.contains(" ")) {
      return new Result.Error<>("invalid directory name: " + directoryName);
    }
    Directory newDirectory = new Directory(directoryName, fileSystem.getCurrentDirectory().getPath().equals("/") ?
            "/" + directoryName : fileSystem.getCurrentDirectory().getPath() + "/" + directoryName, new ArrayList<>());
    Directory updatedCurrentDirectory = fileSystem.getCurrentDirectory().withChild(newDirectory);
    return new Result.Success<>(new FileSystem(fileSystem.getRoot(), updatedCurrentDirectory), "'" + directoryName + "' directory created");
  }
}
