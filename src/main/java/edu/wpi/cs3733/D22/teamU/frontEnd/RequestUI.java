package edu.wpi.cs3733.D22.teamU.frontEnd;

public class RequestUI {
    String ID;
    String name;
    String patientName;
    String date;
    String time;
    String status;
    String destination;
    String employee;



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

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }



    public RequestUI(String ID, String name, String patientName, String date, String time, String status, String destination, String employee) {
        this.ID = ID;
        this.name = name;
        this.patientName = patientName;
        this.date = date;
        this.time = time;
        this.status = status;
        this.destination = destination;
        this.employee = employee;
    }







}
