package edu.wpi.cs3733.D22.teamU.BackEnd.Request.LabRequest;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;

public class LabRequest extends Request {

  String patient;

  public LabRequest(
      String ID, String patient, Employee employee, String labType, String date, String time) {
    this.ID = ID;
    this.patient = patient;
    this.employee = employee;
    this.name = name;
    this.date = date;
    this.time = time;
  }

  public String getPatient() {
    return patient;
  }

  public void setPatient(String patient) {
    this.patient = patient;
  }
}
