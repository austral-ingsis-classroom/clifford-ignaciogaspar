package edu.austral.ingsis.clifford.factories;

import edu.austral.ingsis.clifford.Result;

public interface CommandFactory {
  Result create(String input);
}
