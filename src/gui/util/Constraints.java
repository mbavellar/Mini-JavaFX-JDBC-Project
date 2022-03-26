package gui.util;

import javafx.scene.control.TextField;

public class Constraints {

  public static void setTextFieldInteger(TextField txtField) {
	txtField.textProperty().addListener((obs, oldValue, newValue) -> {
	  if (newValue != null && !newValue.matches("\\d*")) {
		txtField.setText(oldValue);
	  }
	});		
  }
	
  public static void setTextFieldMaxLenght(TextField txtField, int max) {
	txtField.textProperty().addListener((obs, oldValue, newValue) -> {
	  if (newValue != null && newValue.length() > max) {
		txtField.setText(oldValue);
	  }
    });		
  }
	
  public static void setTextFieldDouble(TextField txtField) {
	txtField.textProperty().addListener((obs, oldValue, newValue) -> {
	  if (newValue != null && !newValue.matches("\\d*([\\.]\\d*)?")) {
		txtField.setText(oldValue);
	  }
    });
  }
}
