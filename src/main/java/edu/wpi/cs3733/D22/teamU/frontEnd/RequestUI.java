package edu.wpi.cs3733.D22.teamU.frontEnd;

public class RequestUI {
  String ID;
  String name;
  String patientName;
  String date;
  String time;
  String status;
  String reqType;
  String destination;
  String firstName;
  String lastName;

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

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public RequestUI(
      String ID,
      String reqType,
      String firstName,
      String lastName,
      String patientName,
      String destination,
      String status,
      String date,
      String time) {
    this.ID = ID;
    this.reqType = reqType;
    this.firstName = firstName;
    this.lastName = lastName;
    this.patientName = patientName;
    this.destination = destination;
    this.status = status;
    this.date = date;
    this.time = time;
  }
}
