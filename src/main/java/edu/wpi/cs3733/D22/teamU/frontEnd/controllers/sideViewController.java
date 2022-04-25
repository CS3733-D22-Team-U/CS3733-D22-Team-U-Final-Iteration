package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextArea;
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
import javafx.scene.chart.PieChart;
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

  @FXML TableView<EquipmentUI> alertTable;
  @FXML TableColumn<EquipmentUI, String> dirtyItem;
  @FXML TableColumn<EquipmentUI, Integer> numDirty;
  @FXML TableColumn<EquipmentUI, String> dirtyLoc;
  @FXML TableColumn<EquipmentUI, String> dirtyFloor;

  @FXML Button alertInfo;
  @FXML JFXTextArea alertInfoTextA;

  @FXML PieChart pumpPie;
  @FXML PieChart bedPie;
  @FXML PieChart reclinerPie;

  AnchorPane popupAlert;

  String[] floors = new String[] {"L2", "L1", "1", "2", "3", "4", "5"};
  // Udb udb = DBController.udb;
  ArrayList<String> nodeIDs;

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    chooseFloor.setItems(FXCollections.observableArrayList(floors));
    chooseFloor.setValue("Choose A Floor");

    setUpAllEquipment();
    setUpPieChart();
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

    popupAlert = new AnchorPane();
    try {
      popupAlert
          .getChildren()
          .add(
              FXMLLoader.load(
                  Objects.requireNonNull(
                      getClass()
                          .getClassLoader()
                          .getResource("edu/wpi/cs3733/D22/teamU/views/alertPopUp.fxml"))));
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (tooManyDirtyThings() == true) {
      masterPane.getChildren().add(popupAlert);
      popupAlert.setLayoutX(0);
      popupAlert.setLayoutY(0);
    }
    setUpDirtyEquipment();
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

  ArrayList<EquipmentUI> dirtyEquip = new ArrayList<>();

  private boolean tooManyDirtyThings() throws SQLException, IOException {
    boolean temp = false;
    for (Equipment equipment : Udb.getInstance().EquipmentImpl.EquipmentList) {
      if ((equipment.getName().equals("Beds") && equipment.getInUse() >= 6)
          || equipment.getName().equals("Infusion Pumps")
              && (equipment.getInUse() >= 10 || equipment.getAvailable() < 5)) {
        dirtyEquip.add(
            new EquipmentUI(
                equipment.getName(),
                equipment.getInUse(),
                equipment.getAvailable(),
                equipment.getLocationID(),
                equipment.getLocation().getFloor(),
                equipment.getLocation().getNodeType()));
        AnchorPane bedAP = (AnchorPane) popupAlert.getChildren().get(0);
        System.out.println(bedAP.getChildren().size());
        for (Node n : bedAP.getChildren()) {
          if (n instanceof Button) {
            Button b1 = (Button) n;
            if (b1.getId().equals("okWarn")) {
              b1.setOnMouseClicked(this::clearWarning);
            }
          }
        }
        temp = true;
      }
    }
    return temp;
  }

  private boolean tooManyDirtyPumps() throws SQLException, IOException {
    boolean temp = false;
    for (Equipment equipment : Udb.getInstance().EquipmentImpl.EquipmentList) {
      if (equipment.getName().equals("Infusion Pumps")
          && (equipment.getInUse() >= 10 || equipment.getAvailable() < 5)) {
        dirtyEquip.add(
            new EquipmentUI(
                equipment.getName(),
                equipment.getInUse(),
                equipment.getAvailable(),
                equipment.getLocationID(),
                equipment.getLocation().getFloor(),
                equipment.getLocation().getNodeType()));
        AnchorPane pumpAP = (AnchorPane) popupAlert.getChildren().get(0);
        System.out.println(pumpAP.getChildren().size());
        for (Node n : pumpAP.getChildren()) {
          if (n instanceof Button) {
            Button b1 = (Button) n;
            if (b1.getId().equals("okWarn")) {
              b1.setOnMouseClicked(this::clearWarning);
            }
          }
        }
        temp = true;
      }
    }
    return temp;
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

  private void setUpDirtyEquipment() {
    dirtyItem.setCellValueFactory(new PropertyValueFactory<EquipmentUI, String>("equipmentName"));
    numDirty.setCellValueFactory(new PropertyValueFactory<EquipmentUI, Integer>("amountInUse"));
    dirtyLoc.setCellValueFactory(new PropertyValueFactory<EquipmentUI, String>("location"));
    dirtyFloor.setCellValueFactory(new PropertyValueFactory<EquipmentUI, String>("floor"));

    try {
      alertTable.setItems(FXCollections.observableArrayList(dirtyEquip));
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
    }
  }

  private void clearWarning(MouseEvent mouseEvent) {
    popupAlert.relocate(Integer.MIN_VALUE, Integer.MIN_VALUE);
  }

  public void clickInfo(ActionEvent actionEvent) {
    if (alertInfoTextA.isVisible() == false) {
      alertInfoTextA.setVisible(true);
    } else {
      alertInfoTextA.setVisible(false);
    }
  }

  public void setUpPieChart() {
    ObservableList<PieChart.Data> pieChartData1 =
        FXCollections.observableArrayList(
            new PieChart.Data("Clean", 20), new PieChart.Data("Dirty", 80));
    ObservableList<PieChart.Data> pieChartData2 =
        FXCollections.observableArrayList(
            new PieChart.Data("Clean", 60), new PieChart.Data("Dirty", 40));
    ObservableList<PieChart.Data> pieChartData3 =
        FXCollections.observableArrayList(
            new PieChart.Data("Clean", 50), new PieChart.Data("Dirty", 50));

    pumpPie.setData(pieChartData1);
    pumpPie.setLegendVisible(false);
    pumpPie.setLabelsVisible(false);

    bedPie.setData(pieChartData2);
    bedPie.setLegendVisible(false);
    bedPie.setLabelsVisible(false);

    reclinerPie.setData(pieChartData3);
    reclinerPie.setLegendVisible(false);
    reclinerPie.setLabelsVisible(false);
  }
}
