package edu.wpi.cs3733.D22.teamU.frontEnd.services.maintenance;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;

public class MaintenanceUI {

    String typeOfMaintenance;
    String description;

    public MaintenanceRequest(
            String ID,
            String name,
            String status,
            String destination,
            Employee employee,
            String typeOfMaintenance,
            String description,
            String date,
            String time) {
        this.ID = ID;
        this.name = name;
        this.status = status;
        this.destination = destination;
        this.employee = employee;
        this.typeOfMaintenance = typeOfMaintenance;
        this.description = description;
        this.date = date;
        this.time = time;
    }

    public String getTypeOfMaintenance() {
        return typeOfMaintenance;
    }

    public void setTypeOfMaintenance(String typeOfMaintenance) {
        this.typeOfMaintenance = typeOfMaintenance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
