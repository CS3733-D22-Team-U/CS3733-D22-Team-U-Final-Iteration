package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXCheckBox;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.LabRequest.LabRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class labRequestServices extends ServiceController {

  public Button toHelpPage;
  public Button clear;
  public Label submission;
  @FXML TextArea otherField;
  @FXML TextField patientNameField;
  @FXML TextField staffMemberField;
  @FXML TableColumn<LabRequest, String> activeReqID;
  @FXML TableColumn<LabRequest, String> patientNameReq;
  @FXML TableColumn<LabRequest, String> activeReqStaff;
  @FXML TableColumn<LabRequest, String> activeReqType;
  @FXML TableColumn<LabRequest, String> activeDate;
  @FXML TableColumn<LabRequest, String> activeTime;
  @FXML TableView<LabRequest> activeRequestTable;
  @FXML VBox requestHolder;

  ObservableList<LabRequest> labUIRequests = FXCollections.observableArrayList();
  ObservableList<JFXCheckBox> checkBoxes = FXCollections.observableArrayList();

  // Udb udb = DBController.udb;

  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    try {
      setUpActiveRequests();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    for (Node checkbox : requestHolder.getChildren()) {
      checkBoxes.add((JFXCheckBox) checkbox);
    }
  }

  private void setUpActiveRequests() throws SQLException, IOException {
    activeReqID.setCellValueFactory(new PropertyValueFactory<>("id"));
    patientNameReq.setCellValueFactory(new PropertyValueFactory<>("patientName"));
    activeReqStaff.setCellValueFactory(new PropertyValueFactory<>("staffName"));
    activeReqType.setCellValueFactory(new PropertyValueFactory<>("labType"));
    activeDate.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
    activeTime.setCellValueFactory(new PropertyValueFactory<>("requestTime"));
    activeRequestTable.setItems(getActiveRequestList());
  }

  private ObservableList<LabRequest> getActiveRequestList() throws SQLException, IOException {
    for (LabRequest request : Udb.getInstance().labRequestImpl.hList().values()) {
      labUIRequests.add(
          new LabRequest(
              request.getID(),
              request.getName(),
              request.getPatientName(),
              request.getStatus(),
              request.getEmployee(),
              request.getDestination(),
              request.getDate(),
              request.getTime()));
    }
    return labUIRequests;
  }

  @Override
  public void addRequest() throws SQLException, IOException {
    // String labInput = ...
    String patientInput = patientNameField.getText().trim();
    String staffInput = staffMemberField.getText().trim();
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    for (int i = 0; i < checkBoxes.size(); i++) {
      if (checkBoxes.get(i).isSelected()) {
        double rand = Math.random() * 10000;
        LabRequest request =
            new LabRequest(
                (int) rand + "",
                "lab",
                patientInput,
                "In Progress",
                checkEmployee(staffInput),
                checkBoxes.get(i).getText().trim(),
                sdf3.format(timestamp).substring(0, 10),
                sdf3.format(timestamp).substring(11));
        activeRequestTable.setItems(
            newRequest(
                request.getID(),
                request.getName(),
                request.getPatientName(),
                request.getStatus(),
                request.getEmployee().getEmployeeID(),
                request.getDestination(),
                request.getDate(),
                request.getTime()));
        try {
          Udb.getInstance()
              .labRequestImpl
              .add(
                  new LabRequest(
                      request.getID(),
                      request.getName(),
                      request.getPatientName(),
                      request.getStatus(),
                      checkEmployee(request.getEmployee().getEmployeeID()),
                      request.getDestination(),
                      request.getDate(),
                      request.getTime()));
          submission.setText("Request for " + checkBoxes.get(i).getText() + " successfully sent.");
        } catch (IOException e) {
          e.printStackTrace();
          submission.setText("Request for " + checkBoxes.get(i).getText() + " failed.");
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    clear();
  }

  private ObservableList<LabRequest> newRequest(
      String id,
      String labType,
      String patientName,
      String status,
      String staffName,
      String destination,
      String date,
      String time)
      throws SQLException, IOException {
    labUIRequests.add(
        new LabRequest(
            id, labType, patientName, status, checkEmployee(staffName), destination, date, time));
    return labUIRequests;
  }

  private Employee checkEmployee(String employee) throws SQLException, IOException {
    if (Udb.getInstance().EmployeeImpl.List.get(employee) != null) {
      return Udb.getInstance().EmployeeImpl.List.get(employee);
    } else {
      Employee empty = new Employee("N/A");
      return empty;
    }
  }

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}

  public void clear(ActionEvent actionEvent) {
    clear();
  }

  private void clear() {

    for (int i = 0; i < checkBoxes.size(); i++) {
      checkBoxes.get(i).setSelected(false);
    }
    patientNameField.setText("");
    otherField.setText("");
    staffMemberField.setText("");
  }
}
