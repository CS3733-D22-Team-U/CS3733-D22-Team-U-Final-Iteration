package edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects;

import edu.wpi.cs3733.D22.teamU.BackEnd.Equipment.Equipment;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.controllers.MapController;
import java.io.IOException;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
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

              lnOld.getChildren().remove(1);
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

  public void update(LocationNode ln) throws IOException {
    LocationNode lnNew = new LocationNode(ln.getLocation(), ln.tempx, ln.tempy, ln.getPane());
    mc.locations.put(lnNew.getLocation().getNodeID(), lnNew);
    ln.getPane().getChildren().remove(ln);
    mc.enableDrag(lnNew);
    lnNew.setOnMouseClicked(mc::popupOpen);
    lnNew.getPane().getChildren().add(lnNew);
  }

  class Delta {
    double x, y;
  }
}
