package edu.wpi.cs3733.D22.teamU.BackEnd.Report;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;

public class Report {
  public String id;
  public String type;
  public Employee employee;
  public String description;
  public boolean status;
  public String date;
  public String time;

  public Report(
      String id,
      Employee employee,
      String type,
      String description,
      Boolean status,
      String date,
      String time) {
    this.id = id;
    this.employee = employee;
    this.type = type;
    this.description = description;
    this.status = status;
    this.date = date;
    this.time = time;
  }

  public boolean getStatus() {
    return status;
  }

  public void setStatus(boolean status) {
    this.status = status;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public boolean isStatus() {
    return status;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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
}
