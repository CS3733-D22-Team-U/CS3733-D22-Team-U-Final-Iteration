package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXHamburger;
import edu.wpi.cs3733.D22.teamU.BackEnd.Equipment.Equipment;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.services.equipmentDelivery.EquipmentUI;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import lombok.SneakyThrows;

public class sideViewController extends ServiceController {

  public MenuItem lower2;
  public AnchorPane masterPane;
  @FXML JFXHamburger hamburger;
  @FXML VBox vBoxPane;
  @FXML Pane backgroundPane;
  @FXML Pane assistPane;
  @FXML ComboBox<String> chooseFloor;
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
  @FXML TableColumn<EquipmentUI, String> floor;
  @FXML TableColumn<EquipmentUI, Integer> dirty;
  @FXML TableColumn<EquipmentUI, Integer> clean;
  AnchorPane popupBedAlert;
  AnchorPane popupPumpAlert;

  String[] floors = new String[] {"L2", "L1", "1", "2", "3", "4", "5"};
  // Udb udb = DBController.udb;
  ArrayList<String> nodeIDs;

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    chooseFloor.setItems(FXCollections.observableArrayList(floors));
    chooseFloor.setValue("Choose A Floor");

    setUpAllEquipment();
    /*
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

     */

    popupBedAlert = new AnchorPane();
    try {
      popupBedAlert
          .getChildren()
          .add(
              FXMLLoader.load(
                  Objects.requireNonNull(
                      getClass()
                          .getClassLoader()
                          .getResource("edu/wpi/cs3733/D22/teamU/views/alertBedPopUp.fxml"))));
    } catch (IOException e) {
      e.printStackTrace();
    }

    popupPumpAlert = new AnchorPane();
    try {
      popupPumpAlert
          .getChildren()
          .add(
              FXMLLoader.load(
                  Objects.requireNonNull(
                      getClass()
                          .getClassLoader()
                          .getResource("edu/wpi/cs3733/D22/teamU/views/alertPumpPopUp.fxml"))));
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (tooManyDirtyBeds() == true) {
      masterPane.getChildren().add(popupBedAlert);
      popupBedAlert.setLayoutX(20);
      popupBedAlert.setLayoutY(90);
    }
  }

  ObservableList<EquipmentUI> equipmentUI = FXCollections.observableArrayList();

  private ObservableList<EquipmentUI> getEquipmentList() throws SQLException, IOException {
    equipmentUI.clear();
    for (Equipment equipment : Udb.getInstance().EquipmentImpl.EquipmentList) {
      String floor = chooseFloor.getValue();
      try {
        equipmentUI.add(
            new EquipmentUI(
                equipment.getName(),
                equipment.getInUse(),
                equipment.getAvailable(),
                equipment.getLocationID(),
                equipment.getLocation().getFloor(),
                equipment.getLocation().getNodeType()));
      } catch (Exception e) {
      }
      /*
      if (equipment.getLocation().getFloor().equals(floors))
        equipmentUI.add(
            new EquipmentUI(
                equipment.getLocationID(),
                equipment.getName(),
                equipment.getAmount(),
                equipment.getLocation().getShortName(),
                equipment.getLocation().getFloor(),
                equipment.getLocation().getNodeType()));

       */
    }
    return equipmentUI;
  }

  private boolean tooManyDirtyBeds() throws SQLException, IOException {

    for (Equipment equipment : Udb.getInstance().EquipmentImpl.EquipmentList) {
      if (equipment.getName().equals("Beds") && equipment.getInUse() > 6) {
        AnchorPane bedAP = (AnchorPane) popupBedAlert.getChildren().get(0);
        System.out.println(bedAP.getChildren().size());
        for (Node n : bedAP.getChildren()) {
          if (n instanceof TextField) {
            TextField t1 = (TextField) n;
            t1.setText(equipment.getLocationID());
          } else if (n instanceof Button) {
            Button b1 = (Button) n;
            if (b1.getId().equals("okWarn")) {
              b1.setOnMouseClicked(this::clearWarning);
            }
          }
        }
        return true;
      }
    }
    return false;
  }

  private boolean tooManyDirtyPumps() throws SQLException, IOException {

    for (Equipment equipment : Udb.getInstance().EquipmentImpl.EquipmentList) {
      if (equipment.getName().equals("Infusion Pumps")
          && (equipment.getInUse() > 10 || equipment.getAvailable() < 5)) {
        AnchorPane pumpAP = (AnchorPane) popupPumpAlert.getChildren().get(0);
        System.out.println(pumpAP.getChildren().size());
        for (Node n : pumpAP.getChildren()) {
          if (n instanceof TextField) {
            TextField t1 = (TextField) n;
            t1.setText(equipment.getLocationID());
          } else if (n instanceof Button) {
            Button b1 = (Button) n;
            if (b1.getId().equals("okWarn")) {
              b1.setOnMouseClicked(this::clearWarningP);
            }
          }
        }
        return true;
      }
    }
    return false;
  }

  //  public void lower(ActionEvent actionEvent) {
  //    disable();
  //    MenuItem mi = (MenuItem) actionEvent.getSource();
  //    switch (mi.getId()) {
  //      case "lower2":
  //        recLower2.setVisible(true);
  //        break;
  //    }
  //    switch (mi.getId()) {
  //      case "lower1":
  //        recLower1.setVisible(true);
  //        break;
  //    }
  //    switch (mi.getId()) {
  //      case "level1":
  //        recLevel1.setVisible(true);
  //        break;
  //    }
  //    switch (mi.getId()) {
  //      case "level2":
  //        recLevel2.setVisible(true);
  //        break;
  //    }
  //    switch (mi.getId()) {
  //      case "level3":
  //        recLevel3.setVisible(true);
  //        break;
  //    }
  //    switch (mi.getId()) {
  //      case "level4":
  //        recLevel4.setVisible(true);
  //        break;
  //    }
  //    switch (mi.getId()) {
  //      case "level5":
  //        recLevel5.setVisible(true);
  //        break;
  //    }
  //  }
  //
  //  public void disable() {
  //    recLower2.setVisible(false);
  //    recLower1.setVisible(false);
  //    recLevel1.setVisible(false);
  //    recLevel2.setVisible(false);
  //    recLevel3.setVisible(false);
  //    recLevel4.setVisible(false);
  //    recLevel5.setVisible(false);
  //  }

  private void setUpAllEquipment() {
    equipmentName.setCellValueFactory(
        new PropertyValueFactory<EquipmentUI, String>("equipmentName"));
    dirty.setCellValueFactory(new PropertyValueFactory<EquipmentUI, Integer>("amountInUse"));
    clean.setCellValueFactory(new PropertyValueFactory<EquipmentUI, Integer>("amountAvailable"));
    location.setCellValueFactory(new PropertyValueFactory<EquipmentUI, String>("location"));
    floor.setCellValueFactory(new PropertyValueFactory<EquipmentUI, String>("floor"));
    locationType.setCellValueFactory(new PropertyValueFactory<EquipmentUI, String>("nodeType"));

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

  public void updateList(ActionEvent actionEvent) throws SQLException, IOException {
    equipmentUI.clear();
    for (Equipment equipment : Udb.getInstance().EquipmentImpl.EquipmentList) {
      String floor = chooseFloor.getValue();
      try {
        if (equipment.getLocation().getFloor().equals(floor))
          equipmentUI.add(
              new EquipmentUI(
                  equipment.getName(),
                  equipment.getInUse(),
                  equipment.getAvailable(),
                  equipment.getLocationID(),
                  equipment.getLocation().getFloor(),
                  equipment.getLocation().getNodeType()));
      } catch (Exception e) {
      }
      equipFloor.setItems(equipmentUI);
      //      if (tooManyDirtyPumps() == true) {
      //        masterPane.getChildren().add(popupPumpAlert);
      //        popupBedAlert.setLayoutX(20);
      //        popupBedAlert.setLayoutY(90);
      //      }
    }
  }

  private void clearWarning(MouseEvent mouseEvent) {
    popupBedAlert.relocate(Integer.MIN_VALUE, Integer.MIN_VALUE);
  }

  private void clearWarningP(MouseEvent mouseEvent) {
    popupPumpAlert.relocate(Integer.MIN_VALUE, Integer.MIN_VALUE);
  }
}
