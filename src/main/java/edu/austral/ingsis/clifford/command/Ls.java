package edu.austral.ingsis.clifford.command;

import edu.austral.ingsis.clifford.Directory;
import edu.austral.ingsis.clifford.FileSystem;
import edu.austral.ingsis.clifford.FileSystemObjects;
import edu.austral.ingsis.clifford.Result;

import java.util.Collections;
import java.util.List;

public class Ls implements Command {
    private final String order;

    public Ls(String order) {
        this.order = order;
    }

    @Override
    public Result execute(FileSystem fileSystem) {
        Directory currentDirectory = fileSystem.getCurrentDirectory();
        List<FileSystemObjects> children = currentDirectory.getChildren();

        if (children.isEmpty()) {
            return new Result.Success<>(""); // Directorio vacío
        }

        List<String> names = children.stream().map(FileSystemObjects::getName).toList();

        if (order != null) {
            if (order.equalsIgnoreCase("asc")) {
                Collections.sort(names);
            } else if (order.equalsIgnoreCase("desc")) {
                Collections.sort(names, Collections.reverseOrder());
            } else {
                return new Result.Error("Invalid order parameter: " + order);
            }
        }

        return new Result.Success<>(String.join(" ", names));
    }
}
