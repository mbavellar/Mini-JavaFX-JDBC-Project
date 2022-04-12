package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import javafx.scene.control.Alert.AlertType;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
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
  private ComboBox<Department> comboBoxDepartments;
  
  @FXML
  private Label lblErrorName;
  
  @FXML
  private Label lblErrorEmail;
  
  @FXML
  private Label lblErrorBirthDate;
  
  @FXML
  private Label lblErrorBaseSalary;
  
  @FXML
  private Label lblErrorDepartment;
  
  @FXML
  private Button btnSave;
  
  @FXML
  private Button btnCancel;
  
  private ObservableList<Department> obsDepartmentList;
  
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
    Constraints.setTextFieldMaxLenght(textFieldName, 40);
    Constraints.setTextFieldDouble(textFieldBaseSalary);
    Constraints.setTextFieldMaxLenght(textFieldEmail, 40);
    Utils.formatDatePicker(datePickerBirthDate, "dd/MM/yyyy");
    
    //textFieldId.setText(String.valueOf(service.findAutoIncrement()));
    dataChangeListeners = new ArrayList<>();
    initializeComboBoxDepartment();
  }
  
  public void updateFormData(Seller entity) {
    if (entity == null) throw new IllegalStateException("Entity is null");
    textFieldId.setText(String.valueOf(entity.getId()));
    textFieldName.setText(entity.getName());
    textFieldEmail.setText(entity.getEmail());
    Locale.setDefault(Locale.US);
    textFieldBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
    if (entity.getBirthDate() != null)
      datePickerBirthDate.setValue(
        LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
    if (entity.getDepartment() == null)
      comboBoxDepartments.getSelectionModel().selectFirst();
    else
      comboBoxDepartments.setValue(entity.getDepartment());
  }
  
  public void loadAssociatedDependancies(DepartmentService depService) {
    obsDepartmentList = FXCollections.observableArrayList(depService.findAll());
    comboBoxDepartments.setItems(obsDepartmentList);
  }
  
  private void setErrorMessage(Map<String, String> errors, String textField) {
    
    Set<String> controls = errors.keySet();
    if (controls.contains(textField))
      lblErrorName.setText(errors.get(textField));
  }
  
  private void initializeComboBoxDepartment() {
    Callback<ListView<Department>, ListCell<Department>> factory = listView -> new ListCell<Department>() {
        @Override
        protected void updateItem(Department item, boolean empty) {
          super.updateItem(item, empty);
          setText(empty ? "" : item.getName());
        }
      };
      comboBoxDepartments.setCellFactory(factory);
      comboBoxDepartments.setButtonCell(factory.call(null));
  }
}
