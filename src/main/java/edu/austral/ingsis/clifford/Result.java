package edu.austral.ingsis.clifford;

public sealed interface Result<T> permits Result.Error, Result.Success {
  record Success<T>(T value, String message) implements Result<T> {
    public Success(T value, String message) {
      this.value = value;
      this.message = message;
    }

    public T getValue() {
      return value;
    }

    public String getMessage() {
      return message;
    }
  }

  record Error<T>(String message) implements Result<T> {
    public String getMessage() {
      return message;
    }
  }
}
