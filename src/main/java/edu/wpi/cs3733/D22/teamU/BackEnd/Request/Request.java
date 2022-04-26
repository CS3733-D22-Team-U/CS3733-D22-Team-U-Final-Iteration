package edu.wpi.cs3733.D22.teamU.BackEnd.Request;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import java.util.ArrayList;

public class Request {

  public Request(
      String ID,
      String name,
      String patientName,
      String date,
      String time,
      String status,
      String destination,
      Employee employee,
      Location location) {
    this.ID = ID;
    this.name = name;
    this.patientName = patientName;
    this.date = date;
    this.time = time;
    this.status = status;
    this.destination = destination;
    this.employee = employee;
    this.location = location;
  }

  public String ID;
  public String reqType;
  public String name; // or lab type
  public String patientName;
  public String date;
  public String time;
  public String status;
  public String destination;
  public Employee employee;
  public Location location;

  public String getPickUpDate() {
    return pickUpDate;
  }

  public void setPickUpDate(String pickUpDate) {
    this.pickUpDate = pickUpDate;
  }

  public String getDropOffDate() {
    return dropOffDate;
  }

  public void setDropOffDate(String dropOffDate) {
    this.dropOffDate = dropOffDate;
  }

  public String getServices() {
    return services;
  }

  public void setServices(String services) {
    this.services = services;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  String pickUpDate;
  String dropOffDate;
  String services;
  String notes;

  public Request(
      String ID,
      String reqType,
      Employee employee,
      String destination,
      String status,
      String date,
      String time) {
    this.ID = ID;
    this.reqType = reqType;
    this.employee = employee;
    this.destination = destination;
    this.status = status;
    this.date = date;
    this.time = time;
  }

  public String getReqType() {
    return reqType;
  }

  public void setReqType(String reqType) {
    this.reqType = reqType;
  }

  public Request() {}

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public String getPatientName() {
    return patientName;
  }

  public void setPatientName(String patientName) {
    this.patientName = patientName;
  }

  public String getID() {
    return this.ID;
  }

  public void setID(String ID) {
    this.ID = ID;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public String getDestination() {
    return destination;
  }

  public void updateLocation(String dest, ArrayList<Location> locations) {
    Location temp = new Location();
    temp.setNodeID(dest);
    Location l = locations.get(locations.indexOf(temp));
    l.addRequest(this);
    setLocation(l);
  }

  public Location getLocation() {
    return location;
  }

  public String getStatus() {
    return this.status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public void gettingTheLocation() {
    try {
      int index = Udb.getInstance().locationImpl.search(destination);
      this.location = Udb.getInstance().locationImpl.locations.get(index);
    } catch (Exception e) {
      System.out.println("Security request on line 65");
    }
  }

  public void settingTheRequests() {}
}
