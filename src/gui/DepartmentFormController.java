package gui;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import db.DBException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable{

  private DepartmentService service;
  
  @FXML
  private TextField textFieldId;
  
  @FXML
  private TextField textFieldName;
  
  @FXML
  private Label lblError;
  
  @FXML
  private Button btnSave;
  
  @FXML
  private Button btnCancel;
  
  @FXML
  public void onBtnSaveAction(ActionEvent e) {
    try {
      service.saveOrUpdate(getFormData());
      close(e);
    }
    catch (DBException dbe) {
      Alerts.showAlert("ERROR", "Error saving Department!", dbe.getMessage(), AlertType.ERROR);
    }
  }

  @SuppressWarnings("static-method")
  private void close(ActionEvent e) {
    Utils.currentStage(e).close();
  }
  
  @FXML
  public void onBtnCancelAction(ActionEvent e) {
    close(e);
  }
  
  
  private Department getFormData() {
    return new Department(Utils.tryParseToInt(textFieldId.getText()), textFieldName.getText());
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    service = new DepartmentService();
    Constraints.setTextFieldInteger(textFieldId);
    Constraints.setTextFieldMaxLenght(textFieldName, 30);
    //textFieldId.setText(String.valueOf(service.findAutoIncrement()));
  }
  
  public void updateFormData(Department entity) {
    if (entity == null) throw new IllegalStateException("Entity is null");
    textFieldId.setText(String.valueOf(entity.getId()));
    textFieldName.setText(entity.getName());
  }
}
