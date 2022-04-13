package edu.wpi.cs3733.D22.teamU.BackEnd.Location;

import edu.wpi.cs3733.D22.teamU.BackEnd.Equipment.Equipment;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;
import java.util.ArrayList;
import java.util.Objects;

public class Location {

  String nodeID;
  int xcoord;
  int ycoord;
  String floor;
  String building;
  String nodeType;
  String longName;
  String shortName;
  ArrayList<Equipment> equipment = new ArrayList<>();
  ArrayList<Request> requests = new ArrayList<>();
  /** Empty constructor */
  public Location() {}

  /**
   * Location constuctor that only take in the nodeID
   *
   * @param nodeID
   */
  public Location(String nodeID) {
    this.nodeID = nodeID;
    this.xcoord = 0;
    this.ycoord = 0;
    this.floor = "N/A";
    this.building = "N/A";
    this.nodeType = "N/A";
    this.longName = "N/A";
    this.shortName = "N/A";
  }

  /**
   * Location constructor that takes in all attributes
   *
   * @param nodeID
   * @param xcoord
   * @param ycoord
   * @param floor
   * @param building
   * @param nodeType
   * @param longName
   * @param shortName
   */
  public Location(
      String nodeID,
      int xcoord,
      int ycoord,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName) {
    this.nodeID = nodeID;
    this.xcoord = xcoord;
    this.ycoord = ycoord;
    this.floor = floor;
    this.building = building;
    this.nodeType = nodeType;
    this.longName = longName;
    this.shortName = shortName;
  }

  /**
   * Method that rturn the locations header
   *
   * @return String
   */
  @Override
  public String toString() {
    return "edu.wpi.team_u.BackEnd.Location.Location{"
        + "nodeID='"
        + nodeID
        + '\''
        + ", xcoord="
        + xcoord
        + ", ycoord="
        + ycoord
        + ", floor='"
        + floor
        + '\''
        + ", building='"
        + building
        + '\''
        + ", nodeType='"
        + nodeType
        + '\''
        + ", longName='"
        + longName
        + '\''
        + ", shortName='"
        + shortName
        + '\''
        + '}';
  }

  public String getNodeID() {
    return nodeID;
  }

  public void setNodeID(String nodeID) {
    this.nodeID = nodeID;
  }

  public int getXcoord() {
    return xcoord;
  }

  public void setXcoord(int xcoord) {
    this.xcoord = xcoord;
  }

  public int getYcoord() {
    return ycoord;
  }

  public void setYcoord(int ycoord) {
    this.ycoord = ycoord;
  }

  public String getFloor() {
    return floor;
  }

  public void setFloor(String floor) {
    this.floor = floor;
  }

  public String getBuilding() {
    return building;
  }

  public void setBuilding(String building) {
    this.building = building;
  }

  public String getNodeType() {
    return nodeType;
  }

  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
  }

  public String getLongName() {
    return longName;
  }

  public void setLongName(String longName) {
    this.longName = longName;
  }

  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  public void addEquipment(Equipment e) {
    equipment.add(e);
  }

  public void addRequest(Request e) {
    requests.add(e);
  }

  public ArrayList<Request> getRequests() {
    return requests;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Location location = (Location) o;
    return Objects.equals(nodeID, location.nodeID);
  }

  public void setRequests(ArrayList<Request> requests) {
    this.requests = requests;
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeID);
  }

  public ArrayList<Equipment> getEquipment() {
    return equipment;
  }

  public void setEquipment(ArrayList<Equipment> equipment) {
    this.equipment = equipment;
  }
}
