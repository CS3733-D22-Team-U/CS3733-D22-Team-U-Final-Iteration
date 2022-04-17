package edu.wpi.cs3733.D22.teamU.BackEnd.Request.MealRequest;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;

public class MealRequest extends Request {

    String dietRest;
    String addNotes;

    public MealRequest(
            String ID,
            String patientName,
            String dietRest,
            String status,
            Employee employee,
            String destination,
            String addNotes,
            String date,
            String time) {
        this.ID = ID;
        this.name = patientName;
        this.dietRest = dietRest;
        this.status = status;
        this.employee = employee;
        this.destination = destination;
        this.addNotes = addNotes;
        this.date = date;
        this.time = time;
    }

    public String getDietRest() {
        return dietRest;
    }

    public void setDietRest(String dietRest) {
        this.dietRest = dietRest;
    }

    public String getAddNotes() {
        return addNotes;
    }

    public void setAddNotes(String addNotes) {
        this.addNotes = addNotes;
    }

}
