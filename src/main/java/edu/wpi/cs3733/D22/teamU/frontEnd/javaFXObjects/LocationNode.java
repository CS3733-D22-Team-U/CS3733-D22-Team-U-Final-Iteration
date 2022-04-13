package edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects;

import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.DBController;
import java.io.IOException;
import javafx.scene.Group;
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
    if (location.getRequests().size() > 0) {
      color = Color.YELLOW;
    } else color = Color.BLACK;

    if (location.getEquipment().size() > 0) {
      Rectangle r = new Rectangle();
      r.setX(x - scale);
      r.setWidth(2 * scale);
      r.setHeight(2 * scale);
      r.setY(y - scale);
      setColor(r);
      r.setStroke(color);
      r.setStrokeWidth(5);
      getChildren().add(r);
    } else {
      Circle c = new Circle();
      c.setCenterY(y);
      c.setCenterX(x);
      c.setRadius(scale);
      setColor(c);
      c.setStroke(color);
      c.setStrokeWidth(5);
      getChildren().add(c);
    }
  }

  private void setColor(Shape s) {

    switch (location.getNodeType()) {
      case "PATI":
        s.setFill(Color.RED);
        break;
      case "STOR":
        s.setFill(Color.ORANGE);
        break;
      case "DIRT":
        s.setFill(Color.YELLOW);
        break;
      case "HALL":
        s.setFill(Color.GREEN);
        break;
      case "ELEV":
        s.setFill(Color.BLUE);
        break;
      case "REST":
        s.setFill(Color.BLUEVIOLET);
        break;
      case "STAI":
        s.setFill(Color.PURPLE);
        break;
      case "DEPT":
        s.setFill(Color.ROSYBROWN);
        break;
      case "LABS":
        s.setFill(Color.SILVER);
        break;
      case "INFO":
        s.setFill(Color.WHEAT);
        break;
      case "CONF":
        s.setFill(Color.BLACK);
        break;
      case "EXIT":
        s.setFill(Color.DARKRED);
        break;
      case "RETL":
        s.setFill(Color.MAGENTA);
        break;
      case "SERV":
        s.setFill(Color.INDIANRED);
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
