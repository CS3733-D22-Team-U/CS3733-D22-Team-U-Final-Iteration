package edu.wpi.cs3733.D22.teamU.BackEnd.Request.SecurityRequest;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;

public class SecurityRequest extends Request {

  public String descriptionOfThreat;
  public String leathalForcePermited;
  private String employeeName;

  public String getEmployeeName() {
    return employeeName;
  }

  public void setEmployeeName(String employeeName) {
    this.employeeName = employeeName;
  }

  public SecurityRequest(
      String ID,
      String name, // sender name
      String status,
      Employee employee,
      String destination,
      String descript,
      String lethal,
      String date,
      String time) {
    this.ID = ID;
    this.name = name; // sender name
    this.status = status;
    this.employee = employee;
    this.destination = destination;
    this.descriptionOfThreat = descript;
    this.leathalForcePermited = lethal;
    this.date = date;
    this.time = time;
    employeeName = employee.getEmployeeID();
    this.firstName = employee.getFirstName();
    this.lastName = employee.getLastName();
  }

  public void setDescriptionOfThreat(String descriptionOfThreat) {
    this.descriptionOfThreat = descriptionOfThreat;
  }

  public void setLeathalForcePermited(String leathalForcePermited) {
    this.leathalForcePermited = leathalForcePermited;
  }

  public String getLeathalForcePermited() {
    return leathalForcePermited;
  }

  public String getDescriptionOfThreat() {
    return descriptionOfThreat;
  }
}
