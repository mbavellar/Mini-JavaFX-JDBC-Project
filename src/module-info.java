module javafx_jdbc {
	requires javafx.controls;
	requires javafx.fxml;
  requires javafx.base;
  requires java.sql;
		
	opens application to javafx.graphics, javafx.fxml;
	opens gui to javafx.graphics, javafx.fxml;
	opens model.entities to javafx.base; // So setCellValueFactory(new PropertyValueFactory<> can work.
}
