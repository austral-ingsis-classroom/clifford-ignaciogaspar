package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.FileSystemObjects;
import edu.austral.ingsis.clifford.Result;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Ls implements Command {
  private final String order;

  public Ls(String order) {
    this.order = order;
  }

  @Override
  public Result<String> execute(FileSystem fileSystem) {
    Directory currentDirectory = fileSystem.getCurrentDirectory();
    List<FileSystemObjects> children = currentDirectory.getChildren();

    if (children.isEmpty()) {
      return new Result.Success<>("","");
    }
    List<String> names =
            children.stream().map(FileSystemObjects::getName).collect(Collectors.toList());

    if (order != null) {
      if (order.equalsIgnoreCase("asc")) {
        Collections.sort(names);
      } else if (order.equalsIgnoreCase("desc")) {
        names.sort(Collections.reverseOrder());
      } else {
        return new Result.Error<>("invalid order parameter: " + order);
      }
    }

    return new Result.Success<>(String.join(" ", names), String.join(" ", names));
  }
}
