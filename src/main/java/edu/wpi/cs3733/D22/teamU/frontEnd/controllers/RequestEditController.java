package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects.ComboBoxAutoComplete;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RequestEditController {
  private Request request;
  private ArrayList<Request> requests;
  private ArrayList<String> fields;

  @FXML TextField ID;
  @FXML TextField name;
  @FXML TextField patientName;
  @FXML TextField status;
  @FXML ComboBox<Location> locations;
  @FXML ComboBox<Employee> employees;

  @FXML TextField service;
  @FXML TextField notes;
  @FXML DatePicker pickUp;
  @FXML DatePicker dropOff;

  @FXML Button submit;

  public void setUp(Request request) {
    this.request = request;
    this.requests = new ArrayList<>(); // initialize this to something diff in future
    this.fields = new ArrayList<>();
    try {
      locations.setTooltip(new Tooltip());
      locations.getItems().addAll(Udb.getInstance().locationImpl.locations);
      new ComboBoxAutoComplete<Location>(locations, 650, 290);
      employees.setTooltip(new Tooltip());
      employees.getItems().addAll(Udb.getInstance().EmployeeImpl.hList().values());
      new ComboBoxAutoComplete<Employee>(employees, 675, 380);
    } catch (Exception e) {
      e.printStackTrace();
    }

    switch (request.getClass().getSimpleName()) {
      case "LaundryRequest":
        fields.add("patientName");
        fields.add("ID");
        fields.add("employee");
        fields.add("status");
        fields.add("destination");
        fields.add("pickUpDate");
        fields.add("dropOffDate");
        fields.add("services");
        fields.add("time");
        fields.add("notes");
        break;
    }

    updateFields();
  }

  // removes request from database
  public void removeRequest() {
    requests.remove(request);
    try {
      Udb.getInstance().remove(request);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    // close pane
  }

  // updates the request
  public void updateRequest() {
    Request newRequest = request;
    for (String field : fields) {
      switch (field) {
        case "patientName":
          newRequest.setPatientName(patientName.getText().trim());
          break;
        case "employee":
          newRequest.setEmployee(employees.getValue());
          break;
        case "status":
          newRequest.setStatus(status.getText().trim());
          break;
        case "destination":
          newRequest.setDestination(locations.getValue().getNodeID());
          break;
        case "pickUpDate":
          newRequest.setPickUpDate(pickUp.getValue().toString());
          break;
        case "dropOffDate":
          newRequest.setDropOffDate(dropOff.getValue().toString());
          break;
        case "services":
          newRequest.setServices(service.getText().trim());
          break;
        case "notes":
          newRequest.setNotes(notes.getText().trim());
          break;
        default:
          break;
      }
    }
    System.out.println(request);

    // close pane?
  }

  // Set fields to edit or remove
  public void updateFields() {
    for (String field : fields) {
      switch (field) {
        case "ID":
          ID.setText(request.getID());
          break;
        case "patientName":
          patientName.setText(request.getPatientName());
          break;
        case "employee":
          employees.setValue(request.getEmployee());
          break;
        case "status":
          status.setText(request.getStatus());
          break;
        case "destination":
          locations.setValue(request.getLocation());
          break;
        case "pickUpDate":
          pickUp.setValue(LocalDate.parse(request.getPickUpDate()));
          break;
        case "dropOffDate":
          dropOff.setValue(LocalDate.parse(request.getDropOffDate()));
          break;
        case "services":
          service.setText(request.getServices());
          break;
        case "notes":
          notes.setText(request.getNotes());
          break;
      }
    }
  }

  public Request submitClick() {
    return request;
  }
}
