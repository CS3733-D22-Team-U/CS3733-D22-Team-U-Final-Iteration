package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.frontEnd.services.Service;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.sql.SQLException;

public class EmployeeInfoController extends ServiceController {

    public void toSettings(ActionEvent actionEvent) {
        System.out.println("Going to settings");
    }

    public void logOut(ActionEvent actionEvent) {
        System.out.println("Logging out");
    }

    @Override
    public void addRequest() throws SQLException, IOException {

    }

    @Override
    public void removeRequest() {

    }

    @Override
    public void updateRequest() {

    }
}
