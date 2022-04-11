package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.SellerService;

public class SellerFormController implements Initializable{
  
  private SellerService service;
  
  private List<DataChangeListener> dataChangeListeners;
  
  @FXML
  private TextField textFieldId;
  
  @FXML
  private TextField textFieldName;
  
  @FXML
  private TextField textFieldEmail;
  
  @FXML
  private DatePicker datePickerBirthDate;
  
  @FXML
  private TextField textFieldBaseSalary;
  
  @FXML
  private Label lblError;
  
  @FXML
  private Button btnSave;
  
  @FXML
  private Button btnCancel;
  
  public void subscribeDataChangeListener(DataChangeListener listener) {
    dataChangeListeners.add(listener);
  }
  
  @FXML
  public void onBtnSaveAction(ActionEvent e) {
    try {
      service.saveOrUpdate(getFormData());
      notifyDataChangeListener();
      close(e);
    }
    catch(ValidationException ve) {
      setErrorMessage(ve.getErrors(), textFieldName.getId());
    }
    catch (DBException dbe) {
      Alerts.showAlert("ERROR", "Error saving Seller!", dbe.getMessage(), AlertType.ERROR);
    }
  }

  private void notifyDataChangeListener() {
    for (var listener : dataChangeListeners)
      listener.onDataChange();
  }

  @SuppressWarnings("static-method")
  private void close(ActionEvent e) {
    Utils.currentStage(e).close();
  }
  
  @FXML
  public void onBtnCancelAction(ActionEvent e) {
    close(e);
  }
  
  
  private Seller getFormData() {
    String departmentName = textFieldName.getText();
    ValidationException exception = new ValidationException();
    if (departmentName == null || departmentName.trim().equals(""))
      exception.addError(textFieldName.getId(), "Field 'Seller' cannot be empty!");
    if (exception.getErrors().size() > 0)
      throw exception;
    return new Seller(Utils.tryParseToInt(textFieldId.getText()), textFieldName.getText(),
      textFieldEmail.getText(), new Date(), Double.parseDouble(textFieldBaseSalary.getText()), new Department());
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    service = new SellerService();
    Constraints.setTextFieldInteger(textFieldId);
    Constraints.setTextFieldMaxLenght(textFieldName, 30);
    //textFieldId.setText(String.valueOf(service.findAutoIncrement()));
    dataChangeListeners = new ArrayList<>();
  }
  
  public void updateFormData(Seller entity) {
    if (entity == null) throw new IllegalStateException("Entity is null");
    textFieldId.setText(String.valueOf(entity.getId()));
    textFieldName.setText(entity.getName());
  }
  
  private void setErrorMessage(Map<String, String> errors, String textField) {
    
    Set<String> controls = errors.keySet();
    if (controls.contains(textField))
      lblError.setText(errors.get(textField));
  }
}
