package edu.wpi.cs3733.D22.teamU.API.Location;

public class DefaultLocation {
  private String ID;

  public DefaultLocation(String id) {
    this.ID = id;
  }

  public String getID() {
    return ID;
  }

  public void setID(String ID) {
    this.ID = ID;
  }

  @Override
  public String toString() {
    return getID();
  }
}
