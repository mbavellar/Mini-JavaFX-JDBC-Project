package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

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
  public void onBtnNewDepartment() {

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
  
  public void updateTableView(DepartmentService departmentService) {
    if (departmentService == null)
      throw new IllegalStateException("Service CANNOT be null");
    
    tableViewDepartment.setItems(FXCollections.observableArrayList(departmentService.findAll()));
  }
}