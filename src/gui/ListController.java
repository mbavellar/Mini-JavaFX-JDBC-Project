package gui;

import gui.listeners.DataChangeListener;
import javafx.fxml.Initializable;

public abstract class ListController implements Initializable, DataChangeListener {
  abstract void updateTableView();
}
