package edu.wpi.cs3733.D22.teamU.BackEnd.Employee;

public class Employee {

  String employeeID;
  String occupation;
  int reports;
  boolean onDuty;
  String username;
  String password;

  public Employee(String employeeID) {
    this.employeeID = employeeID;
    this.occupation = "N/A";
    this.reports = 0;
    this.onDuty = false;
    this.username = "N/A";
    this.password = "N/A";
  }

  public Employee(
      String employeeID,
      String occupation,
      int reports,
      boolean onDuty,
      String username,
      String password) {
    this.employeeID = employeeID;
    this.occupation = occupation;
    this.reports = reports;
    this.onDuty = onDuty;
    this.username = username;
    this.password = password;
  }

  public String getEmployeeID() {
    return employeeID;
  }

  public void setEmployeeID(String employeeID) {
    this.employeeID = employeeID;
  }

  public String getOccupation() {
    return occupation;
  }

  public void setOccupation(String occupation) {
    this.occupation = occupation;
  }

  public int getReports() {
    return reports;
  }

  public void setReports(int reports) {
    this.reports = reports;
  }

  public boolean getOnDuty() {
    return onDuty;
  }

  public void setOnDuty(boolean onDuty) {
    this.onDuty = onDuty;
  }

  public boolean isOnDuty() {
    return onDuty;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
