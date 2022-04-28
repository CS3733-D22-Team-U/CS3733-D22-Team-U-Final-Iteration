package edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects;

import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.CompServRequest.CompServRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.EquipRequest.EquipRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.GiftRequest.GiftRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.LabRequest.LabRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.LaundryRequest.LaundryRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MaintenanceRequest.MaintenanceRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MealRequest.MealRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MedicineRequest.MedicineRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.ReligiousRequest.ReligiousRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.SecurityRequest.SecurityRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.TranslatorRequest.TranslatorRequest;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import edu.wpi.cs3733.D22.teamU.frontEnd.controllers.DraggableMaker;
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
    boolean mealCheck = true;
    boolean giftCheck = true;
    boolean laundryCheck = true;
    boolean maintenanceCheck = true;
    boolean religionCheck = true;
    boolean translatorCheck = true;
    boolean compServCheck = true;
    boolean securityCheck = true;
    for (Request request : location.getRequests()) {
      if (request instanceof EquipRequest && equipCheck) {
        dupes++;
        equipCheck = false;
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/medEquip.png");
        continue;
      }
      if (request instanceof LabRequest && labCheck) {
        dupes++;
        labCheck = false;
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/labServ.png");
        continue;
      }
      if (request instanceof MedicineRequest && medicineCheck) {
        dupes++;
        medicineCheck = false;
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/medicineServ.png");
        continue;
      }
      if (request instanceof MealRequest && mealCheck) {
        dupes++;
        mealCheck = false;
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/mealServ.png");
        continue;
      }
      if (request instanceof GiftRequest && giftCheck) {
        dupes++;
        giftCheck = false;
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/giftServ.png");
        continue;
      }
      if (request instanceof LaundryRequest && laundryCheck) {
        dupes++;
        laundryCheck = false;
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/laundryServ.png");
        continue;
      }
      if (request instanceof MaintenanceRequest && maintenanceCheck) {
        dupes++;
        maintenanceCheck = false;
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/maintenanceServ.png");
        continue;
      }
      if (request instanceof ReligiousRequest && religionCheck) {
        dupes++;
        religionCheck = false;
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/religiousServ.png");
        continue;
      }
      if (request instanceof TranslatorRequest && translatorCheck) {
        dupes++;
        translatorCheck = false;
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/translatorServ.png");
        continue;
      }
      if (request instanceof SecurityRequest && securityCheck) {
        dupes++;
        securityCheck = false;
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/securityServ.png");
        continue;
      }
      if (request instanceof CompServRequest && compServCheck) {
        dupes++;
        compServCheck = false;
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/compServ.png");
        continue;
      }
      if (dupes > 1) {
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/multiServ.png");
        break;
      }
    }
  }

  private void setEquip(Shape s) {
    for (int i = 0; i < location.getEquipment().size(); i++) {
      String name = location.getEquipment().get(i).getName();
      switch (name) {
        case "Beds":
          addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/beds.png");
          break;
        case "Infusion Pumps":
          addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/infoPump.png");
          break;
        case "Recliners":
          addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/recliner.png");
          break;
        default:
          addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/medEquip.png");
      }
    }
  }

  private void setLocationIcon(Shape s) {

    switch (location.getNodeType()) {
      case "PATI":
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/pati.png");
        break;
      case "STOR":
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/stor.png");
        break;
      case "DIRT":
        for (int i = 0; i < location.getEquipment().size(); i++) {
          String currE = location.getEquipment().get(i).getName().trim();
          if (currE.equals("Recliners")) {
            addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/recliner.png");
          }
          if (currE.equals("Beds")) {
            addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/beds.png");
          }
          if (currE.equals("Infusion Pumps")) {
            addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/infoPump.png");
          } else {
            addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/dirt.png");
          }
        }
        break;
      case "HALL":
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/hall.png");
        break;
      case "ELEV":
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/elev.png");
        break;
      case "REST":
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/rest.png");
        break;
      case "STAI":
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/stairs.png");
        break;
      case "DEPT":
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/dept.png");
        break;
      case "LABS":
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/lab3.png");
        break;
      case "INFO":
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/info.png");
        break;
      case "CONF":
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/conf.png");
        break;
      case "EXIT":
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/exit.png");
        break;
      case "RETL":
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/retail.png");
        break;
      case "SERV":
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/serv.png");
        break;
      default:
        addMapIcon("edu/wpi/cs3733/D22/teamU/EditedIcons/serv.png");
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
