package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXHamburger;
import edu.wpi.cs3733.D22.teamU.BackEnd.Equipment.Equipment;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects.ComboBoxAutoComplete;
import edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects.LocationNode;
import edu.wpi.cs3733.D22.teamU.frontEnd.pathFinding.Edge;
import edu.wpi.cs3733.D22.teamU.frontEnd.pathFinding.PathFinding;
import edu.wpi.cs3733.D22.teamU.frontEnd.services.map.MapUI;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import org.assertj.core.util.diff.Delta;

public class MapController extends ServiceController {

  /*Edit Remove Popup*/
  public ComboBox<Location> To;
  public ComboBox<Location> From;
  public TextField popupNodeID;
  public TextField popupXCoord;
  public TextField popupFloor;
  public TextField popupYCoord;
  public TextField popupBuilding;
  public TextField popupNodeType;
  public TextField popupLongName;
  public TextField popupShortName;
  public TextField equipNameTF;
  public TextField equipAmount;
  public TextField equipInUse;
  public TextField equipAvailable;
  public AnchorPane masterPane;
  AnchorPane popupEditPane;
  /* Rectangle Icons */
  @FXML Button Go;

  /* Map Icons State */
  public boolean SRVicon = true;
  public boolean EQPicon = true;
  public boolean ALLicon = true;
  public boolean LOCicon = true;

  @FXML Pane pane;

  /*Add Popup*/
  AnchorPane popupAddPane;
  TextField addNodeID;
  TextField addXcoord;
  TextField addYcoord;
  TextField addLongName;
  TextField addShortName;
  ComboBox addNodeTypeCombo;
  ComboBox addBuildingCombo;
  ComboBox addFloorCombo;
  Button addButton;

  ObservableList<String> nodeTypeList =
      FXCollections.observableArrayList(
          "PATI", "STOR", "DIRT", "HALL", "ELEV", "REST", "STAI", "DEPT", "LABS", "INFO", "CONF",
          "EXIT", "RETL", "SERV");
  ObservableList<String> buildingList = FXCollections.observableArrayList("Tower");
  ObservableList<String> floorList =
      FXCollections.observableArrayList("L1", "L2", "1", "2", "3", "4", "5");
  private final double imageX = 870, imageY = 870;
  // @FXML ScrollPane imagesPane;
  @FXML AnchorPane lowerLevel1Pane;
  @FXML AnchorPane lowerLevel2Pane;
  @FXML AnchorPane floor1Pane;
  @FXML AnchorPane floor2Pane;
  @FXML AnchorPane floor3Pane;
  @FXML AnchorPane floor4Pane;
  @FXML AnchorPane floor5Pane;
  @FXML JFXHamburger hamburger;
  @FXML VBox vBoxPane;
  @FXML TableView<MapUI> mapTable;
  @FXML TableColumn<MapUI, String> nodeID;
  @FXML TableColumn<MapUI, Integer> x;
  @FXML TableColumn<MapUI, Integer> y;
  @FXML TableColumn<MapUI, String> floor;
  @FXML TableColumn<MapUI, String> building;
  @FXML TableColumn<MapUI, String> nodeType;
  @FXML TableColumn<MapUI, String> longName;
  @FXML TableColumn<MapUI, String> shortName;

  @FXML ComboBox<Location> To;
  @FXML ComboBox<Location> From;

  @FXML Pane assistPane;
  ArrayList<Location> nodeIDs;
  @FXML Circle add;
  @FXML Button addBTN;
  ObservableList<MapUI> mapUI = FXCollections.observableArrayList();
  // Udb udb;
  ListView<String> equipmentView, requestView;
  HashMap<String, LocationNode> locations;

  ArrayList<Location> fromLocation;
  ArrayList<Location> toLocation;
  PathFinding pathFinding;

  public MapController() throws IOException, SQLException {}

  public void initialize(URL location, ResourceBundle resources) {
    try {
      pathFinding = new PathFinding(Udb.getInstance().edgeDao.list());
      System.out.println(Udb.getInstance().edgeDao.list().size());
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    addBTN.setDisable(!Udb.admin);
    setScroll(lowerLevel1Pane);
    setScroll(lowerLevel2Pane);
    setScroll(floor1Pane);
    setScroll(floor2Pane);
    setScroll(floor3Pane);
    setScroll(floor4Pane);
    setScroll(floor5Pane);

    toLocation = new ArrayList<>();
    fromLocation = new ArrayList<>();
    try {
      for (Location l : Udb.getInstance().locationImpl.list()) {
        fromLocation.add(l);
        toLocation.add(l);
      }
    } catch (Exception e) {
      System.out.println("here");
    }
    locations = new HashMap<>();
    //    for(LocationNode ln: locations.values())
    //      ln.setV
    nodeIDs = new ArrayList<>();
    try {
      for (Location l : Udb.getInstance().locationImpl.list()) {
        nodeIDs.add(l);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    To.setTooltip(new Tooltip());
    To.getItems().addAll(nodeIDs);
    new ComboBoxAutoComplete<Location>(To, 650, 290);
    From.setTooltip(new Tooltip());
    From.getItems().addAll(nodeIDs);
    new ComboBoxAutoComplete<Location>(From, 650, 290);

    setUpMap();
    mapUI.clear();
    try {
      for (Location loc : Udb.getInstance().locationImpl.locations) {
        mapUI.add(
            new MapUI(
                loc.getNodeID(),
                loc.getXcoord(),
                loc.getYcoord(),
                loc.getFloor(),
                loc.getBuilding(),
                loc.getNodeType(),
                loc.getLongName(),
                loc.getShortName()));

        String s = loc.getFloor();
        LocationNode ln;
        try {
          AnchorPane temp = new AnchorPane();
          switch (s) {
            case "L1":
              temp = lowerLevel1Pane;
              break;
            case "L2":
              temp = lowerLevel2Pane;
              break;
            case "1":
              temp = floor1Pane;
              break;
            case "2":
              temp = floor2Pane;
              break;
            case "3":
              temp = floor3Pane;
              break;
            case "4":
              temp = floor4Pane;
              break;
            case "5":
              temp = floor5Pane;
              break;
          }
          // double x = lnOld.getPane().getPrefWidth() / imageX * (double) l.getXcoord() + 80;
          // double y = lnOld.getPane().getPrefHeight() / imageY * (double) l.getYcoord();
          double scale = Double.min(temp.getPrefHeight(), temp.getPrefWidth());
          double x = scale / imageX * loc.getXcoord();
          double y = scale / imageY * loc.getYcoord();
          ln = new LocationNode(loc, x, y, temp);

          // code to drag node around
          final Delta dragDelta = new Delta();
          ln.setOnMousePressed(
              new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                  // record a delta distance for the drag and drop operation.
                  // setPaneOnMousePressedEventHandler(null);
                  // setPaneOnMouseDraggedEventHandlerEventHandler(null);

                  dragDelta.x = ln.getLayoutX() - mouseEvent.getSceneX();
                  dragDelta.y = ln.getLayoutY() - mouseEvent.getSceneY();
                  ln.setCursor(Cursor.MOVE);
                }
              });
          ln.setOnMouseDragged(
              new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {

                  ln.tempx = mouseEvent.getSceneX() + dragDelta.x + ln.getX();
                  ln.tempy = mouseEvent.getSceneY() + dragDelta.y + ln.getY();
                  ln.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                  ln.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
                }
              });
          ln.setOnMouseReleased(
              new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                  ln.setCursor(Cursor.HAND);

                  ln.getLocation().setXcoord((int) (ln.tempx / scale * imageX));
                  ln.getLocation().setYcoord((int) (ln.tempy / scale * imageY));
                  try {
                    Udb.getInstance().edit(ln.getLocation());
                  } catch (IOException e) {
                    throw new RuntimeException(e);
                  } catch (SQLException e) {
                    throw new RuntimeException(e);
                  }
                  // popupXCoord.setText("ln.getLayoutX()");
                  // popupYCoord.setText("ln.getLayoutY()");

                  // setPaneOnMousePressedEventHandler(paneOnMouseDraggedEventHandler);
                  // setPaneOnMouseDraggedEventHandlerEventHandler(paneOnMouseDraggedEventHandler);
                }
              });

          ln.setOnMouseClicked(this::popupOpen);

          locations.put(loc.getNodeID(), ln);
          temp.getChildren().add(ln);

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    mapTable.setItems(mapUI);
    popupAddPane = new AnchorPane();
    try {
      popupAddPane
          .getChildren()
          .add(
              FXMLLoader.load(
                  getClass()
                      .getClassLoader()
                      .getResource("edu/wpi/cs3733/D22/teamU/views/addLocPopUp.fxml")));
      popupAddPane.setLayoutX(663);
      popupAddPane.setLayoutY(159);

    } catch (IOException e) {
      e.printStackTrace();
    }

    popupEditPane = new AnchorPane();
    try {
      popupEditPane
          .getChildren()
          .add(
              FXMLLoader.load(
                  Objects.requireNonNull(
                      getClass()
                          .getClassLoader()
                          .getResource("edu/wpi/cs3733/D22/teamU/views/popup.fxml"))));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void dispMultiService(MouseEvent mouseEvent) {}

  public void dispMultiServices(MouseEvent mouseEvent) {}

  public void dispElevator(MouseEvent mouseEvent) {}

  public void bestPath(MouseEvent mouseEvent) {
    if (To.getValue() != null && From.getValue() != null) {}
  }

  ArrayList<Edge> edges = new ArrayList<>();

  public void findPath(MouseEvent mouseEvent) throws CloneNotSupportedException {
    for (Edge e : edges) {
      AnchorPane ap = (AnchorPane) e.getParent();
      try {
        ap.getChildren().remove(e);
      } catch (Exception e2) {

      }
    }
    edges = new ArrayList<>();
    if (To.getValue() != null && From.getValue() != null) {
      edges = pathFinding.findPath(From.getValue(), To.getValue());
      System.out.println(edges);
      for (Edge e : edges) {
        LocationNode ln1 = locations.get(e.getLoc1().getNodeID());
        LocationNode ln2 = locations.get(e.getLoc2().getNodeID());
        e.setStartX(ln1.tempx);
        e.setStartY(ln1.tempy);
        e.setEndX(ln2.tempx);
        e.setEndY(ln2.tempy);
        try {
          ln1.getPane().getChildren().add(e);
        } catch (Exception e1) {

        }
      }
    }
  }

  class Delta {
    double x, y;
  }

  private void setScroll(AnchorPane pane) {
    pane.setOnScroll(
        event -> {
          /*
          double zoom_fac = 1.05;
          if (event.getDeltaY() < 0) {
            zoom_fac = 2.0 - zoom_fac;
          }

          Scale newScale = new Scale();
          newScale.setPivotX(event.getX());
          newScale.setPivotY(event.getY());
          newScale.setX(pane.getScaleX() * zoom_fac);
          newScale.setY(pane.getScaleY() * zoom_fac);

          pane.getTransforms().add(newScale);

          event.consume();

           */
        });

    // pane.setOnMousePressed(paneOnMousePressedEventHandler);
    // pane.setOnMouseDragged(paneOnMouseDraggedEventHandler);
  }

  public void setUpMap() {
    nodeID.setCellValueFactory(new PropertyValueFactory<>("nodeID"));
    x.setCellValueFactory(new PropertyValueFactory<>("x"));
    y.setCellValueFactory(new PropertyValueFactory<>("y"));
    floor.setCellValueFactory(new PropertyValueFactory<>("floor"));
    building.setCellValueFactory(new PropertyValueFactory<>("building"));
    nodeType.setCellValueFactory(new PropertyValueFactory<>("nodeType"));
    longName.setCellValueFactory(new PropertyValueFactory<>("longName"));
    shortName.setCellValueFactory(new PropertyValueFactory<>("shortName"));
    // mapTable.setItems(getMapList());
  }

  @Override
  public void addRequest() {}

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}

  public void popUpAdd(MouseEvent mouseEvent) {
    Pane pane = (Pane) addBTN.getParent();
    if (pane.getChildren().contains(popupAddPane)) {
      pane.getChildren().remove(popupAddPane);
    } else {
      pane.getChildren().add(popupAddPane);
      for (Node n : ((AnchorPane) popupAddPane.getChildren().get(0)).getChildren()) {
        if (n instanceof GridPane) {
          GridPane gp = (GridPane) n;
          for (Node n2 : gp.getChildren()) {
            if (n2 instanceof ComboBox) {
              ComboBox cb = (ComboBox) n2;
              switch (cb.getId()) {
                case "addBuildingCombo":
                  addBuildingCombo = cb;
                  addBuildingCombo.setItems(buildingList);
                  break;
                case "addNodeTypeCombo":
                  addNodeTypeCombo = cb;
                  addNodeTypeCombo.setItems(nodeTypeList);
                  break;
                case "addFloorCombo":
                  addFloorCombo = cb;
                  addFloorCombo.setItems(floorList);
                  break;
              }
            } else if (n2 instanceof TextField) {
              TextField tf = (TextField) n2;
              switch (tf.getId()) {
                case "addNodeID":
                  addNodeID = tf;
                  break;
                case "addXcoord":
                  addXcoord = tf;
                  break;
                case "addYcoord":
                  addYcoord = tf;
                  break;
                case "addLongName":
                  addLongName = tf;
                  break;
                case "addShortName":
                  addShortName = tf;
                  break;
              }
            }
          }
        } else if (n instanceof Button && n.getId().equals("addButton")) {
          addButton = (Button) n;
          // addButton.setDisable(!Udb.admin);
          addButton.setOnMouseClicked(this::popupAddLocation);
        }
      }
    }
  }

  private TableView<Equipment> equipTable = new TableView();
  private TableView<Request> reqTable = new TableView();

  private void enableDrag(LocationNode ln) {
    final Delta dragDelta = new Delta();
    AnchorPane temp = ln.getPane();
    Location loc = ln.getLocation();
    double scale = Double.min(temp.getPrefHeight(), temp.getPrefWidth());
    double x = scale / imageX * loc.getXcoord();
    double y = scale / imageY * loc.getYcoord();
    ln.setOnMousePressed(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent mouseEvent) {
            // record a delta distance for the drag and drop operation.
            // setPaneOnMousePressedEventHandler(null);
            // setPaneOnMouseDraggedEventHandlerEventHandler(null);

            dragDelta.x = ln.getLayoutX() - mouseEvent.getSceneX();
            dragDelta.y = ln.getLayoutY() - mouseEvent.getSceneY();
            ln.setCursor(Cursor.MOVE);
          }
        });
    ln.setOnMouseDragged(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent mouseEvent) {

            ln.tempx = mouseEvent.getSceneX() + dragDelta.x + ln.getX();
            ln.tempy = mouseEvent.getSceneY() + dragDelta.y + ln.getY();
            ln.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
            ln.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
          }
        });
    ln.setOnMouseReleased(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent mouseEvent) {
            ln.setCursor(Cursor.HAND);

            ln.getLocation().setXcoord((int) (ln.tempx / scale * imageX));
            ln.getLocation().setYcoord((int) (ln.tempy / scale * imageY));
            try {
              Udb.getInstance().edit(ln.getLocation());
            } catch (IOException e) {
              throw new RuntimeException(e);
            } catch (SQLException e) {
              throw new RuntimeException(e);
            }

            // popupXCoord.setText("ln.getLayoutX()");
            // popupYCoord.setText("ln.getLayoutY()");

            // setPaneOnMousePressedEventHandler(paneOnMouseDraggedEventHandler);
            // setPaneOnMouseDraggedEventHandlerEventHandler(paneOnMouseDraggedEventHandler);
          }
        });
  }

  private Equipment equipment;
  private Request request = null;

  public void popupOpen(MouseEvent mouseEvent) {
    request = null;
    equipment = null;
    equipTable.getItems().clear();
    reqTable.getItems().clear();
    LocationNode locationNode = (LocationNode) mouseEvent.getSource();
    Location location = locationNode.getLocation();
    AnchorPane pane = masterPane;

    popupEditPane.setLayoutX(663);

    popupEditPane.setLayoutY(159);

    Tab locationTab = ((TabPane) popupEditPane.getChildren().get(0)).getTabs().get(0);
    AnchorPane locAnchor = (AnchorPane) locationTab.getContent();
    for (Node n : locAnchor.getChildren()) {
      if (n instanceof Button) {
        Button b2 = (Button) n;
        if (b2.getId().equals("exit")) {
          b2.setOnMouseClicked(this::Exit);
        }
      } else if (n instanceof GridPane) {
        GridPane gp = (GridPane) n;
        for (Node n2 : gp.getChildren()) {
          if (n2 instanceof TextField) {
            TextField tf = (TextField) n2;
            switch (tf.getId()) {
              case "popupNodeID":
                popupNodeID = tf;
                popupNodeID.setText(location.getNodeID());
                break;
              case "popupFloor":
                popupFloor = tf;
                popupFloor.setText(location.getFloor());
                break;
              case "popupYCoord":
                popupYCoord = tf;
                popupYCoord.setText(String.valueOf(location.getYcoord()));
                break;
              case "popupXCoord":
                popupXCoord = tf;
                popupXCoord.setText(String.valueOf(location.getXcoord()));
                break;
              case "popupBuilding":
                popupBuilding = tf;
                popupBuilding.setText(location.getBuilding());
                break;
              case "popupNodeType":
                popupNodeType = tf;
                popupNodeType.setText(location.getNodeType());
                break;
              case "popupLongName":
                popupLongName = tf;
                popupLongName.setText(location.getLongName());
                break;
              case "popupShortName":
                popupShortName = tf;
                popupShortName.setText(location.getShortName());
                break;
              default:
                break;
            }
          } else if (n2 instanceof Button) {
            Button b = (Button) n2;
            try {
              switch (b.getId()) {
                case "edit":
                  b.setDisable(!Udb.getInstance().admin);
                  b.setOnMouseClicked(this::popupEdit);
                  break;
                case "remove":
                  b.setDisable(!Udb.getInstance().admin);
                  b.setOnMouseClicked(this::popupRemove);
                  break;
                default:
                  break;
              }
            } catch (Exception e) {
              System.out.println("map Controller line 400");
            }
          }
        }
      }
    }

    Tab equipTab = ((TabPane) popupEditPane.getChildren().get(0)).getTabs().get(1);
    AnchorPane equipAnchor = (AnchorPane) equipTab.getContent();

    for (Node n : equipAnchor.getChildren()) {
      if (n instanceof Button) {
        Button b2 = (Button) n;
        if (b2.getId().equals("exit1")) {
          b2.setOnMouseClicked(this::Exit);
        } else if (b2.getId().equals("removeEquip")) {
          b2.setOnMouseClicked(this::deleteEquip);
        } else if (b2.getId().equals("editEquip")) {
          b2.setOnMouseClicked(this::editEquipFunc);
        }
      } else if (n instanceof TableView) {
        equipTable = (TableView) n;
        equipTable.setOnMouseClicked(this::selectEquip);
        for (Object tb : equipTable.getColumns()) {
          if (tb instanceof TableColumn) {
            TableColumn tc = (TableColumn) tb;
            switch (tc.getId()) {
              case "equipName":
                tc.setCellValueFactory(new PropertyValueFactory<Equipment, String>("Name"));
                break;
              case "equipmentAmount":
                tc.setCellValueFactory(new PropertyValueFactory<Equipment, Integer>("Amount"));
                break;
              case "equipmentInUse":
                tc.setCellValueFactory(new PropertyValueFactory<Equipment, Integer>("InUse"));
                break;
              case "equipmentAvailable":
                tc.setCellValueFactory(new PropertyValueFactory<Equipment, Integer>("Available"));
                break;
            }
          }
        }
      } else if (n instanceof TextField) {
        TextField tf = (TextField) n;
        switch (tf.getId()) {
          case "equipNameTF":
            equipNameTF = tf;
            break;
          case "equipAmount":
            equipAmount = tf;
            break;
          case "equipInUse":
            equipInUse = tf;
            break;
          case "equipAvailable":
            equipAvailable = tf;
            break;
        }
      }
    }
    equipTable.setItems(
        FXCollections.observableArrayList(locationNode.getLocation().getEquipment()));

    Tab requestTab = ((TabPane) popupEditPane.getChildren().get(0)).getTabs().get(2);
    AnchorPane reqAnchor = (AnchorPane) requestTab.getContent();

    for (Node n : reqAnchor.getChildren()) {
      if (n instanceof Button) {
        Button b2 = (Button) n;
        if (b2.getId().equals("exit2")) {
          b2.setOnMouseClicked(this::Exit);
        } else if (b2.getId().equals("removeReq")) {
          b2.setOnMouseClicked(this::deleteRequest);
        }
      } else if (n instanceof TableView) {
        reqTable = (TableView) n;
        reqTable.setOnMouseClicked(this::selectRequest);
        for (Object tb : reqTable.getColumns()) {
          if (tb instanceof TableColumn) {
            TableColumn tc = (TableColumn) tb;
            switch (tc.getId()) {
              case "serviceName":
                tc.setCellValueFactory(new PropertyValueFactory<Request, String>("name"));
                break;
              case "servicePatient":
                tc.setCellValueFactory(new PropertyValueFactory<Request, String>("patientName"));
                break;
              case "requestDate":
                tc.setCellValueFactory(new PropertyValueFactory<Request, String>("date"));
                break;
              case "requestTime":
                tc.setCellValueFactory(new PropertyValueFactory<Request, String>("time"));
                break;
              case "requestStatus":
                tc.setCellValueFactory(new PropertyValueFactory<Request, String>("status"));
                break;
            }
          }
        }
      }
    }

    reqTable.setItems(FXCollections.observableArrayList(locationNode.getLocation().getRequests()));

    pane.getChildren().add(popupEditPane);
  }

  public void deleteRequest(MouseEvent mouseEvent) {
    try {
      if (request != null) {
        Udb.getInstance().remove(request);
        reqTable.getItems().remove(request);
        request.getLocation().getRequests().remove(request);
        popupEdit(mouseEvent);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void deleteEquip(MouseEvent mouseEvent) {
    try {
      if (equipment != null) {
        Udb.getInstance().remove(equipment);
        equipTable.getItems().remove(equipment);
        equipment.getLocation().getEquipment().remove(equipment);
        popupEdit(mouseEvent);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void editEquipFunc(MouseEvent mouseEvent) {
    try {
      if (equipment != null) {
        Equipment newEquip =
            new Equipment(
                equipNameTF.getText(),
                Integer.parseInt(equipAmount.getText()),
                Integer.parseInt(equipInUse.getText()),
                Integer.parseInt(equipAvailable.getText()),
                equipment.getLocationID());

        Udb.getInstance().edit(newEquip);
        equipTable.getItems().remove(equipment);
        equipTable.getItems().add(newEquip);
        equipment.getLocation().getEquipment().remove(equipment);
        equipment.getLocation().getEquipment().add(newEquip);
        popupEdit(mouseEvent);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  public void selectRequest(MouseEvent mouseEvent) {
    if (reqTable.getSelectionModel().getSelectedItem() instanceof Request) {
      request = (Request) reqTable.getSelectionModel().getSelectedItem();
    }
  }

  public void selectEquip(MouseEvent mouseEvent) {
    if (equipTable.getSelectionModel().getSelectedItem() instanceof Equipment) {
      this.equipment = (Equipment) equipTable.getSelectionModel().getSelectedItem();
      this.equipNameTF.setText(this.equipment.getName());
      this.equipAmount.setText(Integer.toString(this.equipment.getAmount()));
      this.equipInUse.setText(Integer.toString(this.equipment.getInUse()));
      this.equipAvailable.setText(Integer.toString(this.equipment.getAvailable()));
    }
  }

  public void Exit(MouseEvent actionEvent) {
    popupEditPane.relocate(Integer.MIN_VALUE, Integer.MIN_VALUE);
  }

  public void popupEdit(MouseEvent actionEvent) {
    Location l =
        new Location(
            popupNodeID.getText(),
            Integer.parseInt(popupXCoord.getText()),
            Integer.parseInt(popupYCoord.getText()),
            popupFloor.getText(),
            popupBuilding.getText(),
            popupNodeType.getText(),
            popupLongName.getText(),
            popupShortName.getText());

    try {

      Location old =
          Udb.getInstance()
              .locationImpl
              .list()
              .get(Udb.getInstance().locationImpl.list().indexOf(l));
      l.setEquipment(old.getEquipment());
      l.setRequests(old.getRequests());
      Udb.getInstance().locationImpl.edit(l);
      LocationNode lnOld = locations.get(l.getNodeID());
      double scale = Double.min(lnOld.getPane().getPrefHeight(), lnOld.getPane().getPrefWidth());
      double x = scale / imageX * l.getXcoord();
      double y = scale / imageY * l.getYcoord();

      LocationNode lnNew = new LocationNode(l, x, y, lnOld.getPane());
      locations.put(l.getNodeID(), lnNew);
      lnNew.setOnMouseClicked(this::popupOpen);
      enableDrag(lnNew);
      Exit(actionEvent);
      lnOld.getPane().getChildren().remove(lnOld);
      lnNew.getPane().getChildren().add(lnNew);
      // toMap(actionEvent);
    } catch (IOException | SQLException e) {
      e.printStackTrace();
    }
  }

  public void popupRemove(MouseEvent actionEvent) {
    Location l =
        new Location(
            popupNodeID.getText(),
            Integer.parseInt(popupXCoord.getText()),
            Integer.parseInt(popupYCoord.getText()),
            popupFloor.getText(),
            popupBuilding.getText(),
            popupNodeType.getText(),
            popupLongName.getText(),
            popupShortName.getText());

    try {
      Udb.getInstance().locationImpl.remove(l);
      LocationNode lnOld = locations.get(l.getNodeID());
      locations.remove(l.getNodeID());
      Exit(actionEvent);
      lnOld.getPane().getChildren().remove(lnOld);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void popupAddLocation(MouseEvent mouseEvent) {
    System.out.println("test");
    Location l =
        new Location(
            addNodeID.getText(),
            Integer.parseInt(addXcoord.getText()),
            Integer.parseInt(addYcoord.getText()),
            addFloorCombo.getValue().toString(),
            addBuildingCombo.getValue().toString(),
            addNodeTypeCombo.getValue().toString(),
            addLongName.getText(),
            addShortName.getText());
    try {
      Udb.getInstance().locationImpl.add(l);
      String s = l.getFloor();
      AnchorPane temp = new AnchorPane();
      switch (s) {
        case "L1":
          temp = lowerLevel1Pane;
          break;
        case "L2":
          temp = lowerLevel2Pane;
          break;
        case "1":
          temp = floor1Pane;
          break;
        case "2":
          temp = floor2Pane;
          break;
        case "3":
          temp = floor3Pane;
          break;
        case "4":
          temp = floor4Pane;
          break;
        case "5":
          temp = floor5Pane;
          break;
      }
      double scale = Double.min(temp.getPrefHeight(), temp.getPrefWidth());
      double x = scale / imageX * l.getXcoord();
      double y = scale / imageY * l.getYcoord();
      LocationNode ln = new LocationNode(l, x, y, temp);
      ln.setOnMouseClicked(this::popupOpen);
      enableDrag(ln);
      locations.put(l.getNodeID(), ln);
      temp.getChildren().add(ln);
      popUpAdd(mouseEvent);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void test(ZoomEvent zoomEvent) {}

  public void dispALL(MouseEvent mouseevent) {
    if (ALLicon == true) {
      for (LocationNode locationNode : locations.values()) {
        locationNode.setVisible(false);
      }
      EQPicon = false;
      LOCicon = false;
      SRVicon = false;
      ALLicon = false;
    } else {
      for (LocationNode locationNode : locations.values()) {
        locationNode.setVisible(true);
      }
      EQPicon = true;
      LOCicon = true;
      SRVicon = true;
      ALLicon = true;
    }
  }

  public void dispLOC(MouseEvent mouseevent) {
    if (LOCicon == true) {
      for (LocationNode locationNode : locations.values()) {
        String compare = locationNode.getLocation().getNodeType().trim();
        if (compare.equals("ELEV")) {
          locationNode.setVisible(false);
        }
        if (compare.equals("PATI")) {
          locationNode.setVisible(false);
        }
        if (compare.equals("HALL")) {
          locationNode.setVisible(false);
        }
        if (compare.equals("REST")) {
          locationNode.setVisible(false);
        }
        if (compare.equals("LABS")) {
          locationNode.setVisible(false);
        }
        if (compare.equals("DEPT")) {
          locationNode.setVisible(false);
        }
        if (compare.equals("CONF")) {
          locationNode.setVisible(false);
        }
        if (compare.equals("EXIT")) {
          locationNode.setVisible(false);
        }
        if (compare.equals("RETL")) {
          locationNode.setVisible(false);
        }
        if (compare.equals("STAI")) {
          locationNode.setVisible(false);
        }
      }
      LOCicon = false;
    } else {
      for (LocationNode locationNode : locations.values()) {
        String compare = locationNode.getLocation().getNodeType().trim();
        if (compare.equals("ELEV")) {
          locationNode.setVisible(true);
        }
        if (compare.equals("PATI")) {
          locationNode.setVisible(true);
        }
        if (compare.equals("HALL")) {
          locationNode.setVisible(true);
        }
        if (compare.equals("REST")) {
          locationNode.setVisible(true);
        }
        if (compare.equals("LABS")) {
          locationNode.setVisible(true);
        }
        if (compare.equals("DEPT")) {
          locationNode.setVisible(true);
        }
        if (compare.equals("CONF")) {
          locationNode.setVisible(true);
        }
        if (compare.equals("EXIT")) {
          locationNode.setVisible(true);
        }
        if (compare.equals("RETL")) {
          locationNode.setVisible(true);
        }
        if (compare.equals("STAI")) {
          locationNode.setVisible(true);
        }
      }
      LOCicon = true;
    }
  }

  public void dispEQP(MouseEvent mousevent) {
    if (EQPicon == true) {
      for (LocationNode locationNode : locations.values()) {
        String currE = locationNode.getLocation().getNodeType().trim();
        if (currE.equals("RECL")) {
          locationNode.setVisible(false);
        }
        if (currE.equals("BEDS")) {
          locationNode.setVisible(false);
        }
        if (currE.equals("PUMP")) {
          locationNode.setVisible(false);
        }
        if (currE.equals("DIRT")) {
          locationNode.setVisible(false);
        }
        if (currE.equals("EQUP")) {
          locationNode.setVisible(false);
        }
      }
      EQPicon = false;
    } else {
      for (LocationNode locationNode : locations.values()) {
        String currE = locationNode.getLocation().getNodeType().trim();
        if (currE.equals("RECL")) {
          locationNode.setVisible(true);
        }
        if (currE.equals("BEDS")) {
          locationNode.setVisible(true);
        }
        if (currE.equals("PUMP")) {
          locationNode.setVisible(true);
        }
        if (currE.equals("DIRT")) {
          locationNode.setVisible(true);
        }
        if (currE.equals("EQUP")) {
          locationNode.setVisible(true);
        }
      }
      EQPicon = true;
    }
  }

  public void dispSRV(MouseEvent mousevent) {
    if (SRVicon == true) {
      for (LocationNode locationNode : locations.values()) {
        if (locationNode.getLocation().getNodeType().equals("SERV")) {
          locationNode.setVisible(false);
        }
      }
      SRVicon = false;
    } else {
      for (LocationNode locationNode : locations.values()) {
        if (locationNode.getLocation().getNodeType().equals("SERV")) {
          locationNode.setVisible(true);
        }
      }
      SRVicon = true;
    }
  }
}
