package edu.wpi.cs3733.D22.teamU.BackEnd.Request.CompServRequest;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;

public class CompServRequest extends Request {
  public String device;
  public String message;

  public CompServRequest(
      String ID,
      String message,
      String status,
      Employee employee,
      String destination,
      String date,
      String time,
      String device) {
    this.ID = ID;
    this.message = message;
    this.status = status;
    this.employee = employee;
    this.destination = destination;
    this.date = date;
    this.time = time;
    this.device = device;
    this.firstName = employee.getFirstName();
    this.lastName = employee.getLastName();
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getDevice() {
    return device;
  }

  public void setDevice(String device) {
    this.device = device;
  }
}
