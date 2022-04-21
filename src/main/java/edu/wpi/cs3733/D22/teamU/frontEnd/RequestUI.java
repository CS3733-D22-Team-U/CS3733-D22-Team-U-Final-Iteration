package edu.wpi.cs3733.D22.teamU.frontEnd;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;

public class RequestUI {
  String ID;
  String name;
  String patientName;
  String date;
  String time;
  String status;
  String reqType;
  String destination;
  Employee employee;

  public String getReqType() {
    return reqType;
  }

  public void setReqType(String reqType) {
    this.reqType = reqType;
  }



  public String getID() {
    return ID;
  }

  public void setID(String ID) {
    this.ID = ID;
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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public RequestUI(
      String ID,
      String reqType,
      Employee employee,
      String patientName,
      String destination,
      String status,
      String date,
      String time){
    this.ID = ID;
    this.reqType = reqType;
    this.employee = employee;
    this.patientName = patientName;
    this.destination = destination;
    this.status = status;
    this.date = date;
    this.time = time;
  }

}
