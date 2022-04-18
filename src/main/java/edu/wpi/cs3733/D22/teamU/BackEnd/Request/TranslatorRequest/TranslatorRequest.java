package edu.wpi.cs3733.D22.teamU.BackEnd.Request.TranslatorRequest;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;

public class TranslatorRequest extends Request {

    public String toLang;

    public TranslatorRequest(
            String ID,
            String patientName,
            String toLang,
            String status,
            Employee employee,
            String destination,
            String date,
            String time) {
        this.ID = ID;
        this.patientName = patientName;
        this.toLang = toLang;
        this.status = status;
        this.employee = employee;
        this.destination = destination;
        this.date = date;
        this.time = time;
    }

    public String getToLang() {
        return toLang;
    }

    public void setToLang(String toLang) {
        this.toLang = toLang;
    }
}