package edu.wpi.cs3733.D22.teamU.BackEnd.Request.LaundryRequest;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;

public class LaundryRequest extends Request {

  String status;
  String destination;
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
      String services,
      String additionalNotes) {
    this.ID = ID;
    this.patientName = patientName;
    this.employee = employee;
    this.status = status;
    this.destination = destination;
    this.pickUpDate = pickUpDate;
    this.dropOffDate = dropOffDate;
    this.services = services;
    this.notes = additionalNotes;
  }

  public String getPatientName() {
    return patientName;
  }

  public void setPatientName(String patientName) {
    this.patientName = patientName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String location) {
    this.destination = location;
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
