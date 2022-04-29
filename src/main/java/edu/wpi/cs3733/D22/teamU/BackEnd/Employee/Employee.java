package edu.wpi.cs3733.D22.teamU.BackEnd.Employee;

import edu.wpi.cs3733.D22.teamU.BackEnd.Report.Report;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;
import java.util.ArrayList;

public class Employee {

  String employeeID;
  String firstName;
  String lastName;
  String occupation;
  int reports;
  boolean onDuty;
  String username;
  String password;

  ArrayList<Report> reportList = new ArrayList<Report>();

  ArrayList<Request> requests = new ArrayList<Request>();

  public Employee(String employeeID) {
    this.employeeID = employeeID;
    this.occupation = "N/A";
    this.reports = 0;
    this.onDuty = false;
    this.username = "N/A";
    this.password = "N/A";
  }

  public Employee() {
    this.employeeID = employeeID;
    this.occupation = "N/A";
    this.reports = 0;
    this.onDuty = false;
  }

  public Employee(
      String employeeID,
      String firstName,
      String lastName,
      String occupation,
      int reports,
      boolean onDuty,
      String username,
      String password) {
    this.employeeID = employeeID;
    this.firstName = firstName;
    this.lastName = lastName;
    this.occupation = occupation;
    this.reports = reports;
    this.onDuty = onDuty;
    this.username = username;
    this.password = password;
  }

  public Employee(
      String employeeID, String firstName, String lastName, String occupation, boolean onDuty) {
    this.employeeID = employeeID;
    this.firstName = firstName;
    this.lastName = lastName;
    this.occupation = occupation;
    this.reports = reports;
    this.onDuty = onDuty;
  }

  public ArrayList<Report> getReportList() {
    return reportList;
  }

  public void setReportList(ArrayList<Report> reportList) {
    this.reportList = reportList;
  }

  public void addReport(Report r) {
    reportList.add(r);
  }

  public ArrayList<Request> getRequests() {
    return requests;
  }

  public void setRequests(ArrayList<Request> requests) {
    this.requests = requests;
  }

  public void addRequest(Request r) {
    requests.add(r);
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

  @Override
  public String toString() {
    return firstName + ", " + lastName;
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
}
