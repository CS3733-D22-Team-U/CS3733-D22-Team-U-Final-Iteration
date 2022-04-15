package edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects;

import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.DBController;
import java.awt.*;
import java.io.IOException;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class LocationNode extends Group {
  private Location location;
  private Udb udb = DBController.udb;
  private AnchorPane pane;
  private double x, y;
  private final double scale = 15;

  public LocationNode(Location location, double x, double y, AnchorPane pane) throws IOException {
    super();
    this.location = location;
    this.pane = pane;
    this.x = x;
    this.y = y;
    Color color;

    Circle c = new Circle();
    setIcon(c);

    if (location.getRequests().size() > 0) {
      color = Color.YELLOW;
    } else color = Color.BLACK;

    if (location.getEquipment().size() > 0) {
      Rectangle r = new Rectangle();
      r.setX(x - scale);
      r.setWidth(2 * scale);
      r.setHeight(2 * scale);
      r.setY(y - scale);
      // setIcon(r);
      r.setStroke(color);
      r.setStrokeWidth(5);
      // getChildren().add(r);
    }
  }

  private void addMapIcon(ImageView aView) {
    aView.setFitHeight(scale * 5);
    aView.setFitWidth(scale * 5);
    aView.setX(x - (aView.getFitWidth() / 2));
    aView.setY(y - (aView.getFitHeight() / 2));
    aView.setScaleX(.8);
    aView.setScaleY(.8);
    getChildren().add(aView);
  }

  private void setIcon(Shape s) {

    switch (location.getNodeType()) {
      case "PATI":
        ImageView pati = new ImageView("edu/wpi/cs3733/D22/teamU/mapIcons/pati.png");
        addMapIcon(pati);
        break;
      case "STOR":
        ImageView stor = new ImageView("edu/wpi/cs3733/D22/teamU/mapIcons/stor.png");
        addMapIcon(stor);
        break;
      case "DIRT":
        ImageView dirt = new ImageView("edu/wpi/cs3733/D22/teamU/mapIcons/dirt.png");
        addMapIcon(dirt);
        break;
      case "HALL":
        ImageView hall = new ImageView("edu/wpi/cs3733/D22/teamU/mapIcons/hall.png");
        addMapIcon(hall);
        break;
      case "ELEV":
        ImageView elev = new ImageView("edu/wpi/cs3733/D22/teamU/mapIcons/elev.png");
        addMapIcon(elev);
        break;
      case "REST":
        ImageView rest = new ImageView("edu/wpi/cs3733/D22/teamU/mapIcons/restroom.png");
        addMapIcon(rest);
        break;
      case "STAI":
        ImageView stair = new ImageView("edu/wpi/cs3733/D22/teamU/mapIcons/stairs.png");
        addMapIcon(stair);
        break;
      case "DEPT":
        ImageView dept = new ImageView("edu/wpi/cs3733/D22/teamU/mapIcons/dept.png");
        addMapIcon(dept);
        break;
      case "LABS":
        ImageView labs = new ImageView("edu/wpi/cs3733/D22/teamU/mapIcons/lab3.png");
        addMapIcon(labs);
        break;
      case "INFO":
        ImageView info = new ImageView("edu/wpi/cs3733/D22/teamU/mapIcons/info.png");
        addMapIcon(info);
        break;
      case "CONF":
        ImageView conf = new ImageView("edu/wpi/cs3733/D22/teamU/mapIcons/conf.png");
        addMapIcon(conf);
        break;
      case "EXIT":
        ImageView exit = new ImageView("edu/wpi/cs3733/D22/teamU/mapIcons/exit.png");
        addMapIcon(exit);
        break;
      case "RETL":
        ImageView retail = new ImageView("edu/wpi/cs3733/D22/teamU/mapIcons/retail.png");
        addMapIcon(retail);
        break;
      case "SERV":
        ImageView serv = new ImageView("edu/wpi/cs3733/D22/teamU/mapIcons/serv.png");
        addMapIcon(serv);
        break;
      default:
        s.setFill(Color.YELLOWGREEN);
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
}
