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
import javafx.animation.TranslateTransition;
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
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.SneakyThrows;

public class sideViewController extends ServiceController {

  public MenuItem lower2;
  public AnchorPane anchor;
  @FXML JFXHamburger hamburger;
  @FXML VBox vBoxPane;
  @FXML Pane backgroundPane;
  @FXML Pane assistPane;
  @FXML Rectangle recLowerL2;
  @FXML Rectangle recLowerL1;
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
  @FXML Text dirtyPumpTXT;
  @FXML Text pumpTotalTXT;
  @FXML Text dirtyBedTXT;
  @FXML Text bedTotalTXT;
  @FXML Text dirtyRecTXT;
  @FXML Text recTotalTXT;
  @FXML AnchorPane sideBarAnchor;
  @FXML Button sideBarButton;

  int pumpTotal;
  int reclTotal;
  int bedsTotal;
  int pumpInUse;
  int reclInUse;
  int bedsInUse;

  AnchorPane popupAlert;

  String[] floors = new String[] {"L2", "L1", "1", "2", "3", "4", "5"};
  // Udb udb = DBController.udb;
  ArrayList<String> nodeIDs;

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    setUpAllEquipment();
    setUpPieChart("4");

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

    try {
      if (tooManyDirtyThings() == true) {
        anchor.getChildren().add(popupAlert);
        popupAlert.setLayoutX(0);
        popupAlert.setLayoutY(0);
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    setUpDirtyEquipment();
    handleBar();
  }

  private void handleBar() {
    TranslateTransition openNav = new TranslateTransition(new Duration(350), sideBarAnchor);
    openNav.setToY(670);
    TranslateTransition closeNav = new TranslateTransition(new Duration(350), sideBarAnchor);
    sideBarButton.setOnAction(
        (ActionEvent evt) -> {
          if (sideBarAnchor.getTranslateY() != 670) {
            openNav.play();
          } else {
            closeNav.setToY(0);
            closeNav.play();
          }
        });
  }

  ObservableList<EquipmentUI> equipmentUI = FXCollections.observableArrayList();

  private ObservableList<EquipmentUI> getEquipmentList() throws SQLException, IOException {
    equipmentUI.clear();
    for (Equipment equipment : Udb.getInstance().EquipmentImpl.EquipmentList) {
      // String floor = chooseFloor.getValue();
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
        equipment.gettingTheLocation();
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

  private void retrieveEquipmentInfo(String floor) {
    reclTotal = 0;
    pumpTotal = 0;
    bedsTotal = 0;
    reclInUse = 0;
    pumpInUse = 0;
    bedsInUse = 0;

    try {
      for (Equipment equipment : Udb.getInstance().EquipmentImpl.list()) {
        /* pumps */
        System.out.println(equipment.getName());
        if (equipment.getName().trim().equals("Infusion Pumps")) {
          if (equipment.getLocation().getFloor().trim().equals(floor)) {
            pumpTotal += equipment.getAmount();
            pumpInUse += equipment.getInUse();
          }
        }
        /* recliners */
        if (equipment.getName().trim().equals("Recliners")) {
          if (equipment.getLocation().getFloor().equals(floor)) {
            reclTotal += equipment.getAmount();
            reclInUse += equipment.getInUse();
          }
        }
        /* beds */
        if (equipment.getName().trim().equals("Beds")) {
          if (equipment.getLocation().getFloor().equals(floor)) {
            bedsTotal += equipment.getAmount();
            bedsInUse += equipment.getInUse();
          }
        }
      }
    } catch (Exception e) {
      System.out.println("Error in sideViewController.retrieveEquipmentInfo");
    }
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

  public void updateList(String floor) throws SQLException, IOException {
    equipmentUI.clear();
    for (Equipment equipment : Udb.getInstance().EquipmentImpl.EquipmentList) {
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

  private void applyCustomColorSequence(
      ObservableList<PieChart.Data> pieChartData, String... pieColors) {
    int i = 0;
    for (PieChart.Data data : pieChartData) {
      data.getNode().setStyle("-fx-pie-color: " + pieColors[i % pieColors.length] + ";");
      i++;
    }
  }

  public void setUpPieChart(String floor) {
    retrieveEquipmentInfo(floor);
    ObservableList<PieChart.Data> pieChartData1 =
        FXCollections.observableArrayList(
            new PieChart.Data("Clean", pumpTotal - pumpInUse),
            new PieChart.Data("Dirty", pumpInUse));
    ObservableList<PieChart.Data> pieChartData2 =
        FXCollections.observableArrayList(
            new PieChart.Data("Clean", bedsTotal - bedsInUse),
            new PieChart.Data("Dirty", bedsInUse));
    ObservableList<PieChart.Data> pieChartData3 =
        FXCollections.observableArrayList(
            new PieChart.Data("Clean", reclTotal - reclInUse),
            new PieChart.Data("Dirty", reclInUse));

    pumpPie.setData(pieChartData1);
    pumpPie.setLegendVisible(false);
    pumpPie.setLabelsVisible(false);

    bedPie.setData(pieChartData2);
    bedPie.setLegendVisible(false);
    bedPie.setLabelsVisible(false);

    reclinerPie.setData(pieChartData3);
    reclinerPie.setLegendVisible(false);
    reclinerPie.setLabelsVisible(false);

    applyCustomColorSequence(pieChartData1, "#5CBD6F", "#FC5368"); // old: #53FC7D #FC5368

    applyCustomColorSequence(pieChartData2, "#5CBD6F", "#FC5368");

    applyCustomColorSequence(pieChartData3, "#5CBD6F", "#FC5368");

    dirtyPumpTXT.setText(Integer.toString(pumpInUse));
    pumpTotalTXT.setText(Integer.toString(pumpTotal));
    dirtyBedTXT.setText(Integer.toString(bedsInUse));
    bedTotalTXT.setText(Integer.toString(bedsTotal));
    dirtyRecTXT.setText(Integer.toString(reclInUse));
    recTotalTXT.setText(Integer.toString(reclTotal));
  }

  public void mouseHovered(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-background-color: transparent");
    button.setStyle("-fx-border-color:  #029ca6");
    button.setStyle("-fx-border-width: 2");
  }

  public void setFloorLL1(MouseEvent mouseEvent) throws SQLException, IOException {
    setUpPieChart("L1");
    updateList("L1");
    recLowerL1.setOpacity(1.0);
    recLowerL2.setOpacity(0);
    recLevel1.setOpacity(0);
    recLevel2.setOpacity(0);
    recLevel3.setOpacity(0);
    recLevel4.setOpacity(0);
    recLevel5.setOpacity(0);
  }

  public void setFloorLL2(MouseEvent mouseEvent) throws SQLException, IOException {
    setUpPieChart("L2");
    updateList("L2");
    recLowerL1.setOpacity(0);
    recLowerL2.setOpacity(1.0);
    recLevel1.setOpacity(0);
    recLevel2.setOpacity(0);
    recLevel3.setOpacity(0);
    recLevel4.setOpacity(0);
    recLevel5.setOpacity(0);
  }

  public void setFloorL1(MouseEvent mouseEvent) throws SQLException, IOException {
    setUpPieChart("1");
    updateList("1");
    recLowerL1.setOpacity(0);
    recLowerL2.setOpacity(0);
    recLevel1.setOpacity(1.0);
    recLevel2.setOpacity(0);
    recLevel3.setOpacity(0);
    recLevel4.setOpacity(0);
    recLevel5.setOpacity(0);
  }

  public void setFloorL2(MouseEvent mouseEvent) throws SQLException, IOException {
    setUpPieChart("2");
    updateList("2");
    recLowerL1.setOpacity(0);
    recLowerL2.setOpacity(0);
    recLevel1.setOpacity(0);
    recLevel2.setOpacity(1.0);
    recLevel3.setOpacity(0);
    recLevel4.setOpacity(0);
    recLevel5.setOpacity(0);
  }

  public void setFloorL3(MouseEvent mouseEvent) throws SQLException, IOException {
    setUpPieChart("3");
    updateList("3");
    recLowerL1.setOpacity(0);
    recLowerL2.setOpacity(0);
    recLevel1.setOpacity(0);
    recLevel2.setOpacity(0);
    recLevel3.setOpacity(1.0);
    recLevel4.setOpacity(0);
    recLevel5.setOpacity(0);
  }

  public void setFloorL4(MouseEvent mouseEvent) throws SQLException, IOException {
    setUpPieChart("4");
    updateList("4");
    recLowerL1.setOpacity(0);
    recLowerL2.setOpacity(0);
    recLevel1.setOpacity(0);
    recLevel2.setOpacity(0);
    recLevel3.setOpacity(0);
    recLevel4.setOpacity(1.0);
    recLevel5.setOpacity(0);
  }

  public void setFloorL5(MouseEvent mouseEvent) throws SQLException, IOException {
    setUpPieChart("5");
    updateList("5");
    recLowerL1.setOpacity(0);
    recLowerL2.setOpacity(0);
    recLevel1.setOpacity(0);
    recLevel2.setOpacity(0);
    recLevel3.setOpacity(0);
    recLevel4.setOpacity(0);
    recLevel5.setOpacity(1.0);
  }
}
