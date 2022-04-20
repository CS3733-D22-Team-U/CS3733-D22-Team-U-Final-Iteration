package edu.wpi.cs3733.D22.teamU.BackEnd.Request;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import java.util.ArrayList;

public class Request {
  public Request(String ID, String name, String patientName, String date, String time, String status, String destination, Employee employee, Location location) {
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
  public String name; // or lab type
  public String patientName;
  public String date;
  public String time;
  public String status;
  public String destination;
  public Employee employee;
  public Location location;

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
}
