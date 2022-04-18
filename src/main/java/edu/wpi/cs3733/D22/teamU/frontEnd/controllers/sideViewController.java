package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import edu.wpi.cs3733.D22.teamU.BackEnd.Equipment.Equipment;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.services.equipmentDelivery.EquipmentUI;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import lombok.SneakyThrows;

public class sideViewController extends ServiceController {

  public MenuItem lower2;
  @FXML JFXHamburger hamburger;
  @FXML VBox vBoxPane;
  @FXML Pane backgroundPane;
  @FXML Pane assistPane;
  @FXML SplitMenuButton chooseFloor;
  @FXML Rectangle recLower2;
  @FXML Rectangle recLower1;
  @FXML Rectangle recLevel1;
  @FXML Rectangle recLevel2;
  @FXML Rectangle recLevel3;
  @FXML Rectangle recLevel4;
  @FXML Rectangle recLevel5;
  @FXML Rectangle room1;
  @FXML Rectangle room2;
  @FXML Rectangle room3;
  @FXML Rectangle room4;
  @FXML Rectangle room5;
  @FXML Rectangle room6;
  @FXML Rectangle room7;
  @FXML Rectangle room8;
  @FXML Rectangle room9;
  @FXML Rectangle room10;
  @FXML Rectangle room11;
  @FXML Rectangle room12;
  @FXML Rectangle room13;
  @FXML TableView<EquipmentUI> equipFloor;
  @FXML TableColumn<EquipmentUI, String> location;
  @FXML TableColumn<EquipmentUI, String> locationType;
  @FXML TableColumn<EquipmentUI, String> floor;
  @FXML TableColumn<EquipmentUI, String> equipmentName;
  @FXML TextField filterField;

  // Udb udb = DBController.udb;
  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // super.initialize(location, resources);
    setUpAllEquipment();
    HamburgerBasicCloseTransition closeTransition = new HamburgerBasicCloseTransition(hamburger);

    closeTransition.setRate(-1);
    hamburger.addEventHandler(
        MouseEvent.MOUSE_CLICKED,
        e -> {
          closeTransition.setRate(closeTransition.getRate() * -1);
          closeTransition.play();
          vBoxPane.setVisible(!vBoxPane.isVisible());
          backgroundPane.setDisable(!backgroundPane.isDisable());
          if (backgroundPane.isDisable()) {
            hamburger.setPrefWidth(200);
            backgroundPane.setEffect(new GaussianBlur(10));
            assistPane.setDisable(true);
          } else {
            backgroundPane.setEffect(null);
            hamburger.setPrefWidth(77);
            assistPane.setDisable(false);
          }
        });
  }

  public void lower(ActionEvent actionEvent) {
    disable();
    MenuItem mi = (MenuItem) actionEvent.getSource();
    switch (mi.getId()) {
      case "lower2":
        recLower2.setVisible(true);
        break;
    }
    switch (mi.getId()) {
      case "lower1":
        recLower1.setVisible(true);
        break;
    }
    switch (mi.getId()) {
      case "level1":
        recLevel1.setVisible(true);
        break;
    }
    switch (mi.getId()) {
      case "level2":
        recLevel2.setVisible(true);
        break;
    }
    switch (mi.getId()) {
      case "level3":
        recLevel3.setVisible(true);
        break;
    }
    switch (mi.getId()) {
      case "level4":
        recLevel4.setVisible(true);
        break;
    }
    switch (mi.getId()) {
      case "level5":
        recLevel5.setVisible(true);
        break;
    }
  }

  public void disable() {
    recLower2.setVisible(false);
    recLower1.setVisible(false);
    recLevel1.setVisible(false);
    recLevel2.setVisible(false);
    recLevel3.setVisible(false);
    recLevel4.setVisible(false);
    recLevel5.setVisible(false);
  }

  ObservableList<EquipmentUI> equipmentUI = FXCollections.observableArrayList();

  private ObservableList<EquipmentUI> getEquipmentList() throws SQLException, IOException {
    equipmentUI.clear();

    for (Equipment equipment : Udb.getInstance().EquipmentImpl.EquipmentList) {
      if (equipment.getLocation() != null)
        equipmentUI.add(
            new EquipmentUI(
                equipment.getLocationID(),
                equipment.getName(),
                equipment.getAmount(),
                equipment.getLocation().getShortName(),
                equipment.getLocation().getFloor(),
                equipment.getLocation().getNodeType()));
    }
    return equipmentUI;
  }

  private void setUpAllEquipment() {
    equipmentName.setCellValueFactory(
        new PropertyValueFactory<EquipmentUI, String>("equipmentName"));
    location.setCellValueFactory(new PropertyValueFactory<EquipmentUI, String>("id"));
    locationType.setCellValueFactory(new PropertyValueFactory<EquipmentUI, String>("nodeType"));
    floor.setCellValueFactory(new PropertyValueFactory<EquipmentUI, String>("floor"));
    FilteredList<EquipmentUI> filteredData = new FilteredList<>(equipmentUI, p -> true);
    filterField
        .textProperty()
        .addListener(
            (observable, oldValue, newValue) -> {
              filteredData.setPredicate(
                  equipment -> {
                    if (newValue == null || newValue.isEmpty()) {
                      return true;
                    }
                    return false;
                  });
            });
    SortedList<EquipmentUI> sortedData = new SortedList<>(filteredData);
    sortedData.comparatorProperty().bind(equipFloor.comparatorProperty());
    equipFloor.setItems(sortedData);
    try {
      equipFloor.setItems(getEquipmentList());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void addRequest() {}

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}
}
