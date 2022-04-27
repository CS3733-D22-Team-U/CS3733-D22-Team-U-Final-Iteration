package edu.wpi.cs3733.D22.teamU.BackEnd.Equipment;

import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;

public class Equipment {
  String id;

  public String getId() {
    return id;
  }

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
    id = name + "_" + locationID;
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

  public Equipment(String name, int amount, int inuse, int available, String locationID) {
    this.Amount = amount;
    this.Name = name;
    this.InUse = inuse;
    this.Available = available;
    this.locationID = locationID;
    id = name + "_" + locationID;
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

  public void gettingTheLocation() {
    try {
      int index = Udb.getInstance().locationImpl.search(locationID);
      this.location = Udb.getInstance().locationImpl.locations.get(index);
    } catch (Exception e) {
      System.out.println("equipment class on line 107");
    }
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public void setLocationID(String locationID) {
    this.locationID = locationID;
  }
}
