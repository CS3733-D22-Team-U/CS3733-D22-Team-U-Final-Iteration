package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXCheckBox;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.LabRequest.LabRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.DBController;
import edu.wpi.cs3733.D22.teamU.frontEnd.services.lab.LabUI;
import java.io.IOException;
import java.net.URL;
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
  @FXML TableColumn<LabUI, String> activeReqID;
  @FXML TableColumn<LabUI, String> patientNameReq;
  @FXML TableColumn<LabUI, String> activeReqStaff;
  @FXML TableColumn<LabUI, String> activeReqType;
  @FXML TableColumn<LabUI, String> activeDate;
  @FXML TableColumn<LabUI, String> activeTime;
  @FXML TableView<LabUI> activeRequestTable;
  @FXML VBox requestHolder;

  ObservableList<LabUI> labUIRequests = FXCollections.observableArrayList();
  ObservableList<JFXCheckBox> checkBoxes = FXCollections.observableArrayList();

  Udb udb = DBController.udb;

  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    setUpActiveRequests();
    for (Node checkbox : requestHolder.getChildren()) {
      checkBoxes.add((JFXCheckBox) checkbox);
    }
  }

  private void setUpActiveRequests() {
    activeReqID.setCellValueFactory(new PropertyValueFactory<>("id"));
    patientNameReq.setCellValueFactory(new PropertyValueFactory<>("patientName"));
    activeReqStaff.setCellValueFactory(new PropertyValueFactory<>("staffName"));
    activeReqType.setCellValueFactory(new PropertyValueFactory<>("labType"));
    activeDate.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
    activeTime.setCellValueFactory(new PropertyValueFactory<>("requestTime"));
    activeRequestTable.setItems(getActiveRequestList());
  }

  private ObservableList<LabUI> getActiveRequestList() {
    for (LabRequest request : udb.labRequestImpl.hList().values()) {
      labUIRequests.add(
          new LabUI(
              request.getID(),
              request.getPatient(),
              request.getEmployee().getEmployeeID(),
              request.getName(),
              request.getDate(),
              request.getTime()));
    }
    return labUIRequests;
  }

  @Override
  public void addRequest() {

    String patientInput = patientNameField.getText().trim();
    String staffInput = staffMemberField.getText().trim();
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    for (int i = 0; i < checkBoxes.size(); i++) {
      if (checkBoxes.get(i).isSelected()) {
        double rand = Math.random() * 10000;
        LabUI request =
            new LabUI(
                (int) rand + "",
                patientInput,
                staffInput,
                checkBoxes.get(i).getText().trim(),
                sdf3.format(timestamp).substring(0, 10),
                sdf3.format(timestamp).substring(11));
        activeRequestTable.setItems(
            newRequest(
                request.getId(),
                request.getPatientName(),
                request.getStaffName(),
                request.getLabType(),
                request.getRequestDate(),
                request.getRequestTime()));
        try {
          udb.labRequestImpl.add(
              new LabRequest(
                  request.getId(),
                  request.getPatientName(),
                  new Employee(request.getId()),
                  request.getLabType(),
                  request.getRequestDate(),
                  request.getRequestTime()));
          submission.setText("Request for " + checkBoxes.get(i).getText() + " successfully sent.");
        } catch (IOException e) {
          e.printStackTrace();
          submission.setText("Request for " + checkBoxes.get(i).getText() + " failed.");
        }
      }
    }
    clear();
  }

  private ObservableList<LabUI> newRequest(
      String id, String patientName, String staffName, String labType, String date, String time) {
    labUIRequests.add(new LabUI(id, patientName, staffName, labType, date, time));
    return labUIRequests;
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
