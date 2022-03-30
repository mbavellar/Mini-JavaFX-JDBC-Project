package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable {
  
  @FXML
  private TableView<Department> tableViewDepartment;

  @FXML
  private TableColumn<Department, Integer> tableColumnId;

  @FXML
  private TableColumn<Department, String> tableColumnName;

  @FXML
  private Button btnNewDepartment;

  @FXML
  public void onBtnNewDepartment(ActionEvent e) {
    createDialogForm("/gui/DepartmentForm.fxml", Utils.currentStage(e));
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    initializaNodes();
  }

  private void initializaNodes() {
    tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
    tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    
    Stage stage = (Stage) Main.getMainScene().getWindow();
    tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
    tableViewDepartment.prefWidthProperty().bind(stage.maxWidthProperty());
  }
  
  public void updateTableView(List<Department> departments) {
    tableViewDepartment.setItems(FXCollections.observableArrayList(departments));
  }
  
  private void createDialogForm(String absoluteName, Stage parentStage) {
    try {
      Pane pane = FXMLLoader.load(getClass().getResource(absoluteName));
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
}