package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import edu.wpi.cs3733.D22.teamU.BackEnd.Equipment.Equipment;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.EquipRequest.EquipRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.DBController;
import edu.wpi.cs3733.D22.teamU.frontEnd.services.equipmentDelivery.EquipmentUI;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
  @FXML CheckBox roomCheck;
  @FXML TableView<EquipmentUI> equipFloor;
  @FXML TableColumn<EquipmentUI, String> location;
  @FXML TableColumn<EquipmentUI, String> locationType;
  @FXML TableColumn<EquipmentUI, String> equipmentName;

  ObservableList<EquipmentUI> equipment = FXCollections.observableArrayList();
  Udb udb = DBController.udb;
  ArrayList<String> nodeIDs;

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

  public void showRooms(ActionEvent actionEvent) {
    CheckBox ck = (CheckBox) actionEvent.getSource();
    switch (ck.getId()) {
      case "checked":
        recLower2.setVisible(true);
        recLower1.setVisible(true);
        recLevel1.setVisible(true);
        recLevel2.setVisible(true);
        recLevel3.setVisible(true);
        recLevel4.setVisible(true);
        recLevel5.setVisible(true);
        break;
    }
    disableRooms();
  }

  public void disableRooms() {
    room1.setVisible(false);
    room2.setVisible(false);
    room3.setVisible(false);
    room4.setVisible(false);
    room5.setVisible(false);
    room6.setVisible(false);
    room7.setVisible(false);
    room8.setVisible(false);
    room9.setVisible(false);
    room10.setVisible(false);
    room11.setVisible(false);
    room12.setVisible(false);
    room13.setVisible(false);
  }

  ObservableList<EquipmentUI> equipmentUI = FXCollections.observableArrayList();

  private ObservableList<EquipmentUI> getEquipmentList() {
    equipmentUI.clear();
    for (Equipment equipment : udb.EquipmentImpl.EquipmentList) {
      equipmentUI.add(
          new EquipmentUI(
              equipment.getName(),
              equipment.getInUse(),
              equipment.getAvailable(),
              equipment.getAmount(),
              equipment.getLocationID()));
    }

    return equipmentUI;
  }

  ObservableList<EquipmentUI> equipmentUIRequests = FXCollections.observableArrayList();

  private ObservableList<EquipmentUI> getActiveRequestList() {
    for (EquipRequest equipRequest : udb.equipRequestImpl.hList().values()) {
      equipmentUIRequests.add(
          new edu.wpi.cs3733.D22.teamU.frontEnd.services.equipmentDelivery.EquipmentUI(
              equipRequest.getID(),
              equipRequest.getName(),
              equipRequest.getAmount(),
              equipRequest.getDestination(),
              equipRequest.getDate(),
              equipRequest.getTime(),
              equipRequest.getPri()));
    }
    return equipmentUIRequests;
  }

  private void setUpAllEquipment() {
    equipmentName.setCellValueFactory(
        new PropertyValueFactory<EquipmentUI, String>("equipmentName"));
    location.setCellValueFactory(new PropertyValueFactory<EquipmentUI, String>("location"));
    locationType.setCellValueFactory(new PropertyValueFactory<EquipmentUI, String>("amountInUse"));
    equipFloor.setItems(getEquipmentList());
  }

  @Override
  public void addRequest() {}

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}
}
