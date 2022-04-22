package edu.wpi.cs3733.D22.teamU.BackEnd.Request.EquipRequest;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;

public class EquipRequest extends Request {
  int amount;
  String typeOfRequest;
  int priority;

  public EquipRequest(
      String ID,
      String name,
      int amount,
      String typeOfRequest,
      String status,
      Employee employee,
      String destination,
      String date,
      String time,
      int priority) {
    this.ID = ID;
    this.name = name;
    this.amount = amount;
    this.typeOfRequest = typeOfRequest;
    this.status = status;
    this.employee = employee;
    this.destination = destination;
    this.date = date;
    this.time = time;
    this.priority = priority;
    this.firstName = employee.getFirstName();
    this.lastName = employee.getLastName();
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public String getType() {
    return this.typeOfRequest;
  }

  public void setType(String newType) {
    this.typeOfRequest = newType;
  }

  public int getPriority() {
    return this.priority;
  }

  public void setPriority(int newPri) {
    this.priority = newPri;
  }
}
