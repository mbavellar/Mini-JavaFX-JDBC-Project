package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.URI;
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
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController extends ListController {
  
  private SellerService service;
  
  @FXML
  private TableView<Seller> tableViewSeller;

  @FXML
  private TableColumn<Seller, Integer> tableColumnId;

  @FXML
  private TableColumn<Seller, String> tableColumnName;
  
  @FXML
  private TableColumn<Seller, String> tableColumnEmail;
  
  @FXML
  private TableColumn<Seller, Date> tableColumnBirthDate;
  
  @FXML
  private TableColumn<Seller, Double> tableColumnBaseSalary;
  
  @FXML
  private TableColumn<Seller, Seller> tableColumnEdit;
  
  @FXML
  private TableColumn<Seller, Seller> tableColumnRemove;

  @FXML
  private Button btnNewSeller;

  @FXML
  public void onBtnNewSellerAction(ActionEvent e) {
    Seller entity = new Seller();
    createDialogForm(entity, URI.SELLER_FORM, Utils.currentStage(e));
  }

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    service = new SellerService();
    initializaNodes();
  }

  private void initializaNodes() {
    tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
    tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
    Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
    tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
    Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);
    
    Stage stage = (Stage) Main.getMainScene().getWindow();
    tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
    tableViewSeller.prefWidthProperty().bind(stage.maxWidthProperty());
  }
  
  public void updateTableView() {
    tableViewSeller.setItems(FXCollections.observableArrayList(service.findAll()));
    initButtons(tableColumnEdit, "Edit");
    initButtons(tableColumnRemove, "Delete");
  }
  
  private void createDialogForm(Seller entity, String absoluteName, Stage parentStage) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
      Pane pane = loader.load();
      
      SellerFormController controller = loader.getController();
      controller.subscribeDataChangeListener(this);
      controller.updateFormData(entity);
      
      Stage departmentFormStage = new Stage();      
      departmentFormStage.setTitle("Enter Seller Data:");
      departmentFormStage.setScene(new Scene(pane));
      departmentFormStage.setResizable(false);
      departmentFormStage.initOwner(parentStage);
      departmentFormStage.initModality(Modality.WINDOW_MODAL);
      departmentFormStage.showAndWait();
    } catch (IOException e) {
      Alerts.showAlert("ERROR", "Could NOT load Add Seller!", e.getMessage(), AlertType.ERROR);
    }
  }

  @Override
  public void onDataChange() {
    updateTableView();    
  }
  
  private synchronized void initButtons(TableColumn<Seller, Seller> tableColumn, String buttonText) {
    tableColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
    tableColumn.setCellFactory(generateButtons(buttonText));
  }

  private Callback<TableColumn<Seller, Seller>, TableCell<Seller, Seller>> generateButtons(String buttonText) {
    return param -> new TableCell<Seller, Seller>() {

      private final Button button = new Button(buttonText);
      
      @Override
      protected void updateItem(Seller obj, boolean empty) {
        super.updateItem(obj, empty);
        if (obj == null) {
          setGraphic(null);
          return;
        }
        setGraphic(button);
        
        if (buttonText == "Edit")
          button.setOnAction(
            event -> createDialogForm(obj, URI.SELLER_FORM, Utils.currentStage(event)));
        if (buttonText == "Delete")
          button.setOnAction(
              event -> removeEntity(obj));
      }
    };
  }
  
  private void removeEntity(Seller obj) {
    Optional<ButtonType> result =  Alerts.showConfirmationDialog("Confirmation!", "Are you sure?");
    if (result.get() == ButtonType.OK) {
      service.remove(obj);
      updateTableView();
    }
  }
}