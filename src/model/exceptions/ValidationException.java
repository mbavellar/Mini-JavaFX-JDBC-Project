package model.exceptions;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.Control;

public class ValidationException extends RuntimeException {
  
  private static final long serialVersionUID = 1L;
  
  private Map<String, String> errors = new HashMap<>();
  
  public ValidationException() {
    super("Validation Exception!");
  }
  
  public ValidationException(String errorMessage) {
    super(errorMessage);
  }
  
  public Map<String, String> getErrors() {
    return errors;
  }
  
  public void addError(String textField, String errorMessage) {
    errors.put(textField, errorMessage);
  }
}
