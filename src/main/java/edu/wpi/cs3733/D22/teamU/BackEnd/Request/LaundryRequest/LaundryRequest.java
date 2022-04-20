package edu.wpi.cs3733.D22.teamU.BackEnd.Request.LaundryRequest;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;

public class LaundryRequest extends Request {

  String pickUpDate;
  String dropOffDate;
  String services;
  String notes;

  public LaundryRequest(
      String ID,
      String patientName,
      Employee employee,
      String status,
      String destination,
      String pickUpDate,
      String dropOffDate,
      String time,
      String services,
      String notes) {
    this.ID = ID;
    this.patientName = patientName;
    this.employee = employee;
    this.status = status;
    this.destination = destination;
    this.pickUpDate = pickUpDate;
    this.dropOffDate = dropOffDate;
    this.services = services;
    this.time = time;
    this.notes = notes;
  }

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
}
