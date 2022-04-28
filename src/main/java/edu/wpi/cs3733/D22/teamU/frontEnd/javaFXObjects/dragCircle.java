package edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects;

import edu.wpi.cs3733.D22.teamU.BackEnd.Equipment.Equipment;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import edu.wpi.cs3733.D22.teamU.frontEnd.controllers.MapController;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class dragCircle extends Circle {
  private Pane pane;
  private double x, tempx;
  private double y, tempy;
  private Equipment equipment;
  private MapController mc;
  //    double scale = Double.min(temp.getPrefHeight(), temp.getPrefWidth());
  //    double x = scale / imageX * loc.getXcoord();
  //    double y = scale / imageY * loc.getYcoord();
  //    ln = new LocationNode(loc, x, y, temp);

  public dragCircle(Pane pane, double x, double y, Equipment e, MapController mc) {
    this.pane = pane;
    this.x = x;
    this.y = y;
    tempx = x;
    tempy = y;
    this.equipment = e;
    this.mc = mc;
    setCenterX(x);
    setCenterY(y);
    setRadius(10);
    setFill(Color.RED);
    enableDrag();
    mc.masterPane.getChildren().add(this);
  }

  private void enableDrag() {
    final Delta dragDelta = new Delta();

    setOnMouseClicked(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent mouseEvent) {
            // record a delta distance for the drag and drop operation.
            // setPaneOnMousePressedEventHandler(null);
            // setPaneOnMouseDraggedEventHandlerEventHandler(null);

            dragDelta.x = getCenterX() - mouseEvent.getSceneX();
            dragDelta.y = getCenterY() - mouseEvent.getSceneY();
            setCursor(Cursor.MOVE);
          }
        });
    setOnMouseDragged(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent mouseEvent) {

            tempx = mouseEvent.getSceneX() + dragDelta.x + getLayoutX();
            tempy = mouseEvent.getSceneY() + dragDelta.y + getLayoutY();
            setCenterX(mouseEvent.getSceneX() + dragDelta.x);
            setCenterY(mouseEvent.getSceneY() + dragDelta.y);
          }
        });

    setOnMouseReleased(
        new EventHandler<MouseEvent>() {
          @Override
          public void handle(MouseEvent mouseEvent) {
            setCursor(Cursor.HAND);
            LocationNode best = null;
            if (getBoundsInParent().intersects(pane.getBoundsInParent())) {

              double x = getBoundsInParent().getCenterX() - pane.getBoundsInParent().getMinX();
              double y = getBoundsInParent().getCenterY() - pane.getBoundsInParent().getMinY();
              System.out.println("X: " + x + "\tY: " + y);
              String s = mc.mapTab.getSelectionModel().getSelectedItem().getText();

              double distance = Integer.MAX_VALUE;
              for (LocationNode ln : mc.locations.values()) {
                if (s.equals(ln.getLocation().getFloor())) {
                  double a = Math.pow(x - ln.tempx, 2);
                  double b = Math.pow(y - ln.tempy, 2);
                  double c = Math.sqrt(a + b);
                  if (distance > c) {
                    distance = c;
                    best = ln;
                  }
                }
              }
              // Updating the equipTable
              LocationNode lnOld = mc.locations.get(equipment.getLocation().getNodeID());
              lnOld.getLocation().getEquipment().remove(equipment);
              equipment.setLocation(best.getLocation());

              mc.equipTable.getItems().clear();
              mc.equipTable.setItems(
                  FXCollections.observableArrayList(lnOld.getLocation().getEquipment()));

              best.getLocation().getEquipment().add(equipment);

              lnOld.getChildren().add(0, setLocationIcon(lnOld));


              try {
                update(best);
              } catch (IOException e) {
                e.printStackTrace();
              }

              // Updating the Location HashMap

            }
            mc.masterPane.getChildren().remove(mc.dc);
            try {
              Udb.getInstance().edit(equipment);
            } catch (IOException e) {
              e.printStackTrace();
            } catch (SQLException e) {
              e.printStackTrace();
            }
          }
        });
  }

  private ImageView setLocationIcon(LocationNode ln) {
    ImageView temp = null;
    switch (ln.getLocation().getNodeType()) {
      case "PATI":
        temp = addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/pati.png");
        break;
      case "STOR":
        temp = addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/stor.png");
        break;
      case "DIRT":
        for (int i = 0; i < ln.getLocation().getEquipment().size(); i++) {
          String currE = ln.getLocation().getEquipment().get(i).getName().trim();
          if (currE.equals("Recliners")) {
            temp = addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/recliner.png");
          }
          if (currE.equals("Beds")) {
            temp = addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/beds.png");
          }
          if (currE.equals("Infusion Pumps")) {
            temp = addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/infuPump.png");
          } else {
            temp = addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/dirt.png");
          }
        }
        break;
      case "HALL":
        temp = addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/hall.png");
        break;
      case "ELEV":
        temp = addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/elev.png");
        break;
      case "REST":
        temp = addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/restroom.png");
        break;
      case "STAI":
        temp = addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/stairs.png");
        break;
      case "DEPT":
        temp = addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/dept.png");
        break;
      case "LABS":
        temp = addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/lab3.png");
        break;
      case "INFO":
        temp = addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/info.png");
        break;
      case "CONF":
        temp = addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/conf.png");
        break;
      case "EXIT":
        temp = addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/exit.png");
        break;
      case "RETL":
        temp = addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/retail.png");
        break;
      case "SERV":
        temp = addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/serv.png");
        break;
      default:
        temp = addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/serv.png");
        break;
    }
    return temp;
  }

  private ImageView addMapIcon(String resource) {
    ImageView aView = new ImageView();
    URL a = Uapp.class.getClassLoader().getResource(resource);
    aView.setImage(new Image(String.valueOf(a)));
    aView.setFitHeight(7 * 5);
    aView.setFitWidth(7 * 5);
    aView.setX(x - (aView.getFitWidth() / 2));
    aView.setY(y - (aView.getFitHeight() / 2));
    aView.setScaleX(.8);
    aView.setScaleY(.8);
    return aView;
  }

  public void update(LocationNode ln) throws IOException {
    LocationNode lnNew = new LocationNode(ln.getLocation(), ln.tempx, ln.tempy, ln.getPane());
    mc.locations.put(lnNew.getLocation().getNodeID(), lnNew);
    ln.getPane().getChildren().remove(ln);
    mc.enableDrag(lnNew);
    lnNew.setOnMouseClicked(mc::popupOpen);
    lnNew.getPane().getChildren().add(lnNew);
  }

  public void setNewNode() {}

  class Delta {
    double x, y;
  }
}
