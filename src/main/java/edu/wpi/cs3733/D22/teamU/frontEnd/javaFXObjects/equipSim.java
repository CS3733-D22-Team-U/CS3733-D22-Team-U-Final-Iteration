package edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects;

import edu.wpi.cs3733.D22.teamU.BackEnd.Equipment.Equipment;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import java.util.ArrayList;

public class equipSim extends Equipment {
  public Location location;
  public Enum Status;

  enum Status {
    LOW,
    MEDIUM,
    HIGH
  }

  public equipSim(String name, int amount, int inuse, String locationID, Status state) {
    super(name, amount, inuse, locationID);
  }

  public equipSim(
      String name, int amount, int inuse, int available, String locationID, Status state) {
    super(name, amount, inuse, available, locationID);
  }

  public void setLocation(String locationID, ArrayList<Location> locations) {
    Location temp = new Location();
    temp.setNodeID(locationID);
    Location l = locations.get(locations.indexOf(temp));
    this.location = l;
  }
}
