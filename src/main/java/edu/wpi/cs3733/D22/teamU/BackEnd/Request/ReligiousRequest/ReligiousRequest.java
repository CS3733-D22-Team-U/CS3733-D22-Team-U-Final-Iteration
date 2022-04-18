package edu.wpi.cs3733.D22.teamU.BackEnd.Request.ReligiousRequest;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;

public class ReligiousRequest extends Request {

  private String patient;
  private String religion;

  ReligiousRequest(
      String ID,
      String name,
      String date,
      String time,
      String patient,
      String religion,
      String status,
      String destination,
      Employee employee) {

    this.ID = ID;
    this.name = name;
    this.date = date;
    this.time = time;
    this.patient = patient;
    this.religion = religion;
    this.status = status;
    this.destination = destination;
    this.employee = employee;
  }

  public String getPatient() {
    return patient;
  }

  public void setPatient(String patient) {
    this.patient = patient;
  }

  public String getReligion() {
    return religion;
  }

  public void setReligion(String religion) {
    this.religion = religion;
  }
}
