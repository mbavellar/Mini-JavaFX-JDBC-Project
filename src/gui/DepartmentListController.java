package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable, DataChangeListener {
  
  private DepartmentService service;
  
  @FXML
  private TableView<Department> tableViewDepartment;

  @FXML
  private TableColumn<Department, Integer> tableColumnId;

  @FXML
  private TableColumn<Department, String> tableColumnName;
  
  @FXML
  private TableColumn<Department, Department> tableColumnEdit;
  
  @FXML
  private TableColumn<Department, Department> tableColumnRemove;

  @FXML
  private Button btnNewDepartment;

  @FXML
  public void onBtnNewDepartmentAction(ActionEvent e) {
    Department entity = new Department();
    createDialogForm(entity, "/gui/DepartmentForm.fxml", Utils.currentStage(e));
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    service = new DepartmentService();
    initializaNodes();
  }

  private void initializaNodes() {
    tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
    tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    
    Stage stage = (Stage) Main.getMainScene().getWindow();
    tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
    tableViewDepartment.prefWidthProperty().bind(stage.maxWidthProperty());
  }
  
  public void updateTableView() {
    tableViewDepartment.setItems(FXCollections.observableArrayList(service.findAll()));
    initButtons(tableColumnEdit, "Edit");
    initButtons(tableColumnRemove, "Delete");
  }
  
  private void createDialogForm(Department entity, String absoluteName, Stage parentStage) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
      Pane pane = loader.load();
      
      DepartmentFormController controller = loader.getController();
      controller.subscribeDataChangeListener(this);
      controller.updateFormData(entity);
      
      Stage departmentFormStage = new Stage();      
      departmentFormStage.setTitle("Enter Department Data:");
      departmentFormStage.setScene(new Scene(pane));
      departmentFormStage.setResizable(false);
      departmentFormStage.initOwner(parentStage);
      departmentFormStage.initModality(Modality.WINDOW_MODAL);
      departmentFormStage.showAndWait();
    } catch (IOException e) {
      Alerts.showAlert("ERROR", "Could NOT load Add Department!", e.getMessage(), AlertType.ERROR);
    }
  }

  @Override
  public void onDataChange() {
    updateTableView();    
  }
  
  private synchronized void initButtons(TableColumn<Department, Department> tableColumn, String buttonText) {
    tableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
    tableColumn.setCellFactory(generateButtons(buttonText));
  }

  private Callback<TableColumn<Department, Department>, TableCell<Department, Department>> generateButtons(
      String buttonText) {
    return param -> new TableCell<Department, Department>() {

      private final Button button = new Button(buttonText);
      
      @Override
      protected void updateItem(Department obj, boolean empty) {
        super.updateItem(obj, empty);
        if (obj == null) {
          setGraphic(null);
          return;
        }
        setGraphic(button);
        
        if (buttonText == "Edit")
          button.setOnAction(
            event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
        if (buttonText == "Delete")
          button.setOnAction(
              event -> removeEntity(obj));
      }
    };
  }
  
  private void removeEntity(Department obj) {
    Optional<ButtonType> result =  Alerts.showConfirmationDialog("Confirmation!", "Are you sure?");
    if (result.get() == ButtonType.OK) {
      service.remove(obj);
      updateTableView();
    }
  }
}