package edu.wpi.cs3733.D22.teamU.BackEnd.Request.LabRequest;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;

public class LabRequest extends Request {
  int amount;

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public LabRequest(
      String ID,
      String labType,
      int amount,
      String patientName,
      String status,
      Employee employee,
      String destination,
      String date,
      String time) {
    this.ID = ID;
    this.name = labType;
    this.amount = amount;
    this.patientName = patientName;
    this.status = status;
    this.employee = employee;
    this.destination = destination;
    this.date = date;
    this.time = time;
    this.firstName = employee.getFirstName();
    this.lastName = employee.getLastName();
  }
}
