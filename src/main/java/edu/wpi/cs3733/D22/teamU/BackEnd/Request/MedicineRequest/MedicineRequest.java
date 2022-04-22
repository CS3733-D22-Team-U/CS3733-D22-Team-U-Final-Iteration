package edu.wpi.cs3733.D22.teamU.BackEnd.Request.MedicineRequest;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;

public class MedicineRequest extends Request {
  int amount;

  public MedicineRequest(
          String ID,
          String name,
          int amount,
          String patientName,
          String status,
          Employee employee,
          String destination,
          String date,
          String time) {
    super();
    this.ID = ID;
    this.name = name;
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

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }
}
