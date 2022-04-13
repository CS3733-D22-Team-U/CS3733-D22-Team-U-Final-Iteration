package edu.wpi.cs3733.D22.teamU.frontEnd.services.medicine;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;

public class medicineUI {

  private String id;
  private String name;
  private String patientName;
  private String destination;
  private String status;
  private Employee employee;
  private String date;
  private String time;
  private String staffName;
  private int requestAmount;

  public medicineUI(
      String id,
      String name,
      String patientName,
      String destination,
      String status,
      Employee employee,
      String date,
      String time) {
    this.id = id;
    this.name = name;
    this.patientName = patientName;
    this.destination = destination;
    this.status = status;
    this.employee = employee;
    this.date = date;
    this.time = time;
    staffName = employee.getEmployeeID();
  }

  public medicineUI(
      String id,
      String name,
      String patientName,
      String destination,
      String status,
      Employee employee,
      String date,
      String time,
      int amount) {
    this.id = id;
    this.name = name;
    this.patientName = patientName;
    this.destination = destination;
    this.status = status;
    this.employee = employee;
    this.date = date;
    this.time = time;
    requestAmount = amount;
    staffName = employee.getEmployeeID();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPatientName() {
    return patientName;
  }

  public void setPatientName(String patientName) {
    this.patientName = patientName;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
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

  public String getStaffName() {
    return staffName;
  }

  public void setStaffName(String staffName) {
    this.staffName = staffName;
  }

  public int getRequestAmount() {
    return requestAmount;
  }

  public void setRequestAmount(int requestAmount) {
    this.requestAmount = requestAmount;
  }
}
