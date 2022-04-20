package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.ReligiousRequest.ReligiousRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects.ComboBoxAutoComplete;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import lombok.SneakyThrows;

public class ReligiousRequestController extends ServiceController {

  public ComboBox<String> locations;
  public ComboBox<String> employees;

  @FXML TableColumn<ReligiousRequest, String> nameID;
  @FXML TableColumn<ReligiousRequest, String> name;
  @FXML TableColumn<ReligiousRequest, String> date;
  @FXML TableColumn<ReligiousRequest, String> newTime;
  @FXML TableColumn<ReligiousRequest, String> patientName;
  @FXML TableColumn<ReligiousRequest, String> religion;
  @FXML TableColumn<ReligiousRequest, String> status;
  @FXML TableColumn<ReligiousRequest, String> destination;
  @FXML TableColumn<ReligiousRequest, String> employee;
  @FXML TableColumn<ReligiousRequest, String> notes;
  @FXML TableView<ReligiousRequest> table;

  @FXML Button clearButton;
  @FXML Button submitButton;

  @FXML Text requestText;
  @FXML StackPane requestsStack;
  @FXML Pane newRequestPane;
  @FXML Pane allActiveRequestsPane;

  @FXML Button newReqButton;
  @FXML Button activeReqButton;
  @FXML TextArea inputName;
  @FXML TextArea inputPatient;
  @FXML TextArea inputReligion;
  @FXML TextArea inputNotes;
  @FXML Text time;

  ObservableList<ReligiousRequest> religiousUIRequests = FXCollections.observableArrayList();
  // Udb udb;
  ArrayList<String> nodeIDs;
  ArrayList<String> staff;
  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    setUpAllReligiousReq();

    requestText.setVisible(false);
    // Displays Locations in Table View
    nodeIDs = new ArrayList<>();
    for (Location l : Udb.getInstance().locationImpl.list()) {
      nodeIDs.add(l.getNodeID());
    }
    locations.setTooltip(new Tooltip());
    locations.getItems().addAll(nodeIDs);
    new ComboBoxAutoComplete<String>(locations, 650, 290);

    // Displays EMployess in Table View
    staff = new ArrayList<>();
    for (Employee l : Udb.getInstance().EmployeeImpl.hList().values()) {
      staff.add(l.getEmployeeID());
    }
    employees.setTooltip(new Tooltip());
    employees.getItems().addAll(staff);
    new ComboBoxAutoComplete<String>(employees, 675, 380);

    handleTime();
  }

  private void handleTime() {
    Thread timeThread =
        new Thread(
            () -> {
              while (Uapp.running) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String timeStampTime = sdf3.format(timestamp).substring(11);
                time.setText(timeStampTime);
              }
            });
    timeThread.start();
  }

  //
  private void setUpAllReligiousReq() throws SQLException, IOException {
    nameID.setCellValueFactory(new PropertyValueFactory("ID"));
    name.setCellValueFactory(new PropertyValueFactory<ReligiousRequest, String>("name"));
    date.setCellValueFactory(new PropertyValueFactory<ReligiousRequest, String>("date"));
    newTime.setCellValueFactory(new PropertyValueFactory<ReligiousRequest, String>("time"));
    patientName.setCellValueFactory(
        new PropertyValueFactory<ReligiousRequest, String>("patientName"));
    religion.setCellValueFactory(new PropertyValueFactory<ReligiousRequest, String>("religion"));
    status.setCellValueFactory(new PropertyValueFactory<ReligiousRequest, String>("status"));
    destination.setCellValueFactory(
        new PropertyValueFactory<ReligiousRequest, String>("destination"));
    employee.setCellValueFactory(
        new PropertyValueFactory<ReligiousRequest, String>("employeeName"));
    notes.setCellValueFactory(new PropertyValueFactory<ReligiousRequest, String>("notes"));
    table.setItems(getReligiousList());
  }

  private ObservableList<ReligiousRequest> newRequest(
      String id,
      String name,
      String date,
      String time,
      String patientName,
      String religion,
      String status,
      String destination,
      Employee employee,
      String notes) {
    religiousUIRequests.add(
        new ReligiousRequest(
            id, name, date, time, patientName, religion, status, destination, employee, notes));
    return religiousUIRequests;
  }

  private ObservableList<ReligiousRequest> getReligiousList() throws SQLException, IOException {
    religiousUIRequests.clear();
    for (ReligiousRequest request : Udb.getInstance().religiousRequestImpl.List.values()) {
      religiousUIRequests.add(
          new ReligiousRequest(
              request.getID(),
              request.getName(),
              request.getDate(),
              request.getTime(),
              request.getPatientName(),
              request.getReligion(),
              request.getStatus(),
              request.getDestination(),
              request.getEmployee(),
              request.getNotes()));
    }
    return religiousUIRequests;
  }

  @Override
  public void addRequest() throws SQLException, IOException {
    StringBuilder startRequestString = new StringBuilder("Your request for: ");

    String endRequest = "Has been placed successfully";
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    String room = locations.getValue().toString();

    String employ = employees.getValue().toString();

    double rand = Math.random() * 10000;

    ReligiousRequest request =
        new ReligiousRequest(
            (int) rand + "",
            inputName.getText().trim(),
            sdf3.format(timestamp).substring(0, 10),
            sdf3.format(timestamp).substring(11),
            inputPatient.getText().trim(),
            inputReligion.getText().trim(),
            "Pending",
            room,
            checkEmployee(employ),
            inputNotes.getText().trim());

    religiousUIRequests.add(request);

    table.setItems(religiousUIRequests);
    try {
      Udb.getInstance().add(request);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}

  public void clearRequest() {
    requestText.setVisible(true);
    new Thread(
            () -> {
              try {
                Thread.sleep(1500); // milliseconds
                Platform.runLater(
                    () -> {
                      requestText.setVisible(false);
                    });
              } catch (InterruptedException ie) {
              }
            })
        .start();
  }

  public Employee checkEmployee(String employee) throws NullPointerException {
    if (EmployeeDaoImpl.List.get(employee) != null) {
      return EmployeeDaoImpl.List.get(employee);
    } else {
      Employee empty = new Employee("N/A");
      return empty;
    }
  }

  public void switchToNewRequest(ActionEvent actionEvent) {
    ObservableList<Node> stackNodes = requestsStack.getChildren();
    Node newReq = stackNodes.get(stackNodes.indexOf(newRequestPane));
    for (Node node : stackNodes) {
      node.setVisible(false);
    }
    newReq.setVisible(true);
    newReq.toBack();
    activeReqButton.setUnderline(false);
    newReqButton.setUnderline(true);
  }

  public void switchToActive(ActionEvent actionEvent) {
    ObservableList<Node> stackNodes = requestsStack.getChildren();
    Node active = stackNodes.get(stackNodes.indexOf(allActiveRequestsPane));
    for (Node node : stackNodes) {
      node.setVisible(false);
    }
    active.setVisible(true);
    active.toBack();
    activeReqButton.setUnderline(true);

    newReqButton.setUnderline(false);
  }

  public void mouseHovered(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: #E6F6F7");
  }

  public void mouseExit(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: transparent");
  }
}