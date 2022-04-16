package edu.wpi.cs3733.D22.teamU.BackEnd.Request.LaundryRequest;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;

public class LaundryRequest extends Request {

  String patientName;
  String status;
  String destination;

  public LaundryRequest(
      String ID,
      String name,
      String patientName,
      Employee employee,
      String status,
      String location,
      String date,
      String time) {
    this.ID = ID;
    this.name = name; // type of lab request
    this.patientName = patientName;
    this.employee = employee;
    this.status = status;
    this.destination = location;
    this.date = date;
    this.time = time;
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
}
