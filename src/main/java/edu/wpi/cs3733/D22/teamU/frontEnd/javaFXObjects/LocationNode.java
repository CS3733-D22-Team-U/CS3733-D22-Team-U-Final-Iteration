package edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects;

import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.EquipRequest.EquipRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.LabRequest.LabRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MedicineRequest.MedicineRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import edu.wpi.cs3733.D22.teamU.frontEnd.controllers.DraggableMaker;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class LocationNode extends Group {
  private Location location;
  private AnchorPane pane;
  private double x, y;
  private final double scale = 7;

  public double tempx, tempy;

  public LocationNode(Location location, double x, double y, AnchorPane pane) throws IOException {

    super();
    this.location = location;
    this.pane = pane;
    this.x = x;
    this.y = y;
    tempx = x;
    tempy = y;
    Color color; // .setonMouseDrag();

    Circle c = new Circle();
    c.setRadius(scale);
    setLocationIcon(c);
    DraggableMaker draggableMaker = new DraggableMaker();
    draggableMaker.makeDraggable(c);

    if (location.getEquipment().size() > 0) {
      Rectangle r = new Rectangle();
      setEquip(r);
    }

    if (location.getRequests().size() > 0) {
      Rectangle r = new Rectangle();
      setRequest(r);
      DraggableMaker draggableMaker2 = new DraggableMaker();
      draggableMaker.makeDraggable(r);
    }
  }

  private void addMapIcon(String resource) {
    ImageView aView = new ImageView();
    URL a = Uapp.class.getClassLoader().getResource(resource);
    aView.setImage(new Image(String.valueOf(a)));
    aView.setFitHeight(scale * 5);
    aView.setFitWidth(scale * 5);
    aView.setX(x - (aView.getFitWidth() / 2));
    aView.setY(y - (aView.getFitHeight() / 2));
    aView.setScaleX(.8);
    aView.setScaleY(.8);
    getChildren().add(aView);
  }

  private void setRequest(Shape s) {
    int dupes = 0;
    boolean equipCheck = true;
    boolean labCheck = true;
    boolean medicineCheck = true;
    for (Request request : location.getRequests()) {
      if (request instanceof EquipRequest && equipCheck) {
        dupes++;
        equipCheck = false;
        addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/medEquip.png");
        continue;
      }
      if (request instanceof LabRequest && labCheck) {
        dupes++;
        labCheck = false;
        addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/labServ.png");
        continue;
      }
      if (request instanceof MedicineRequest && medicineCheck) {
        dupes++;
        medicineCheck = false;
        addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/medicineServ.png");
        continue;
      }
      if (dupes > 1) {
        addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/multiServ.png");
        break;
      }
    }
  }

  private void setEquip(Shape s) {
    for (int i = 0; i < location.getEquipment().size(); i++) {
      String name = location.getEquipment().get(i).getName();
      switch (name) {
        case "Beds":
          addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/beds.png");
          break;
        case "Infusion Pumps":
          addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/infuPump.png");
          break;
        case "Recliners":
          addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/recliner.png");
          break;
        default:
          addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/DefaultEquip.png");
      }
    }
  }

  private void setLocationIcon(Shape s) {

    switch (location.getNodeType()) {
      case "PATI":
        addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/pati.png");
        break;
      case "STOR":
        addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/stor.png");
        break;
      case "DIRT":
        for (int i = 0; i < location.getEquipment().size(); i++) {
          String currE = location.getEquipment().get(i).getName().trim();
          if (currE.equals("Recliners")) {
            addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/recliner.png");
          }
          if (currE.equals("Beds")) {
            addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/beds.png");
          }
          if (currE.equals("Infusion Pumps")) {
            addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/infuPump.png");
          } else {
            addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/dirt.png");
          }
        }
        break;
      case "HALL":
        addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/hall.png");
        break;
      case "ELEV":
        addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/elev.png");
        break;
      case "REST":
        addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/restroom.png");
        break;
      case "STAI":
        addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/stairs.png");
        break;
      case "DEPT":
        addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/dept.png");
        break;
      case "LABS":
        addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/lab3.png");
        break;
      case "INFO":
        addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/info.png");
        break;
      case "CONF":
        addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/conf.png");
        break;
      case "EXIT":
        addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/exit.png");
        break;
      case "RETL":
        addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/retail.png");
        break;
      case "SERV":
        addMapIcon("edu/wpi/cs3733/D22/teamU/mapIcons/serv.png");
        break;
      default:
        s.setFill(Color.YELLOWGREEN);
        break;
    }
  }

  public Location getLocation() {
    return location;
  }

  public AnchorPane getPane() {
    return pane;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }

  public void setX(double x) {
    this.x = x;
  }

  public void setY(double y) {
    this.y = y;
  }
}
