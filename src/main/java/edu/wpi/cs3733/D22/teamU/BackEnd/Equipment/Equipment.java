package edu.wpi.cs3733.D22.teamU.BackEnd.Equipment;

import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;

public class Equipment {
  String Name;
  int Amount;
  int InUse;
  int Available;
  Location location;
  String locationID;
  /**
   * Contructor for Equipment datatype that only takes a name
   *
   * @param name
   */
  public Equipment(String name, String locationID) {
    this.Name = name;
    this.Amount = 1;
    this.InUse = 0;
    this.Available = Amount - InUse;
    this.locationID = locationID;
  }

  /**
   * Contructor for Equipment datatype that take in name, amount, in use
   *
   * @param name
   * @param amount
   * @param inuse
   */
  public Equipment(String name, int amount, int inuse, String locationID) {
    this.Amount = amount;
    this.Name = name;
    this.InUse = inuse;
    this.Available = Amount - InUse;
    this.locationID = locationID;
  }

  /**
   * Return Equipment name
   *
   * @return String
   */
  public String getName() {
    return this.Name;
  }

  /**
   * return Equipment amount
   *
   * @return int
   */
  public int getAmount() {
    return this.Amount;
  }

  /**
   * return InUse
   *
   * @return int
   */
  public int getInUse() {
    return InUse;
  }

  /**
   * return available
   *
   * @return int
   */
  public int getAvailable() {
    return this.Available;
  }

  public String getLocationID() {
    return locationID;
  }

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }
}
