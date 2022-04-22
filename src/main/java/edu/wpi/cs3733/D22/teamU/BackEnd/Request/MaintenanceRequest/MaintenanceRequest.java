package edu.wpi.cs3733.D22.teamU.BackEnd.Request.MaintenanceRequest;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;

public class MaintenanceRequest extends Request {

  String typeOfMaintenance;
  String description;

  public MaintenanceRequest(
      String ID,
      String name,
      String status,
      String destination,
      Employee employee,
      String typeOfMaintenance,
      String description,
      String date,
      String time) {
    this.ID = ID;
    this.name = name;
    this.status = status;
    this.destination = destination;
    this.employee = employee;
    this.typeOfMaintenance = typeOfMaintenance;
    this.description = description;
    this.date = date;
    this.time = time;
    this.firstName = employee.getFirstName();
    this.lastName = employee.getLastName();
  }

  public String getTypeOfMaintenance() {
    return typeOfMaintenance;
  }

  public void setTypeOfMaintenance(String typeOfMaintenance) {
    this.typeOfMaintenance = typeOfMaintenance;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
