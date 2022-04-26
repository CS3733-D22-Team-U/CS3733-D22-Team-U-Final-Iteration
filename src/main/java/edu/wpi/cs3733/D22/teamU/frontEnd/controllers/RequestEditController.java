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
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class RequestEditController {
  private Request request;
  private ArrayList<Request> requests;
  private ArrayList<String> fields;
  private Pane activePane;

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
  @FXML TextField amount;
  @FXML TextField typeOfRequest;
  @FXML TextField priority;
  @FXML TextField labType;
  @FXML TextField descript;
  @FXML TextField lethal;
  @FXML TextField typeOfMaintenance;
  @FXML TextField description;
  @FXML TextField message;
  @FXML TextField device;
  @FXML TextField dietRest;
  @FXML TextField addNotes;
  @FXML TextField gifts;
  @FXML TextField religion;
  @FXML TextField toLang;

  @FXML StackPane specialFields;
  @FXML Pane religiousFields;
  @FXML Pane medicineFields;
  @FXML Pane labFields;
  @FXML Pane laundryFields;
  @FXML Pane giftFields;
  @FXML Pane equipmentFields;
  @FXML Pane securityFields;
  @FXML Pane compServFields;
  @FXML Pane mealFields;
  @FXML Pane translatorFields;
  @FXML Pane maintenanceFields;

  public void setUp(Request request) {
    this.request = request;
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
        activePane = laundryFields;
        break;
    }

    switchPane();

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
        case "name":
          newRequest.setName(name.getText().trim());
          break;
        case "location":
          newRequest.setLocation(locations.getValue());
          break;
        case "amount":
          newRequest.setAmount(Integer.parseInt(amount.getText().trim()));
          break;
        case "typeOfRequest":
          newRequest.setTypeOfRequest(typeOfRequest.getText().trim());
          break;
        case "priority":
          newRequest.setPriority(Integer.parseInt(priority.getText().trim()));
          break;
        case "labType":
          newRequest.setLabType(labType.getText().trim());
          break;
        case "descript":
          newRequest.setDescript(descript.getText().trim());
          break;
        case "lethal":
          newRequest.setLethal(lethal.getText().trim());
          break;
        case "typeOfMaintenance":
          newRequest.setTypeOfMaintenance(typeOfMaintenance.getText().trim());
          break;
        case "description":
          newRequest.setDescription(description.getText().trim());
          break;
        case "message":
          newRequest.setMessage(message.getText().trim());
          break;
        case "device":
          newRequest.setDevice(device.getText().trim());
          break;
        case "dietRest":
          newRequest.setDietRest(dietRest.getText().trim());
          break;
        case "addNotes":
          newRequest.setAddNotes(addNotes.getText().trim());
          break;
        case "gifts":
          newRequest.setGifts(gifts.getText().trim());
        case "religion":
          newRequest.setReligion(religion.getText().trim());
          break;
        case "toLang":
          newRequest.setToLang(toLang.getText().trim());
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
        case "amount":
          amount.setText(String.valueOf(request.getAmount()));
          break;
        case "typeOfRequest":
          typeOfRequest.setText(request.getTypeOfRequest());
          break;
        case "priority":
          priority.setText(String.valueOf(request.getPriority()));
          break;
        case "labType":
          labType.setText(request.getLabType());
          break;
        case "descript":
          descript.setText(request.getDescript());
          break;
        case "lethal":
          lethal.setText(request.getLethal());
          break;
        case "typeOfMaintenance":
          typeOfMaintenance.setText(request.getTypeOfMaintenance());
          break;
        case "description":
          description.setText(request.getDescription());
          break;
        case "message":
          message.setText(request.getMessage());
          break;
        case "device":
          device.setText(request.getDevice());
          break;
        case "dietRest":
          dietRest.setText(request.getDietRest());
          break;
        case "addNotes":
          addNotes.setText(request.getAddNotes());
          break;
        case "gifts":
          gifts.setText(request.getGifts());
          break;
        case "religion":
          religion.setText(request.getReligion());
          break;
        case "toLang":
          toLang.setText(request.getToLang());
          break;
        default:
          break;
      }
    }
  }

  public Request getRequest() {
    return request;
  }

  public void switchPane() {
    ObservableList<Node> stackNodes = specialFields.getChildren();
    Node active = stackNodes.get(stackNodes.indexOf(activePane));
    for (Node node : stackNodes) {
      node.setVisible(false);
    }
    active.setVisible(true);
  }
}
