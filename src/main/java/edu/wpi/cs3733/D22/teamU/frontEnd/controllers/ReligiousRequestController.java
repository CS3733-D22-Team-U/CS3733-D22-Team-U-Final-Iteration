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

  public ComboBox<Location> locations;
  public ComboBox<Employee> employees;

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
  @FXML Pane editRequestPane;

  @FXML Button newReqButton;
  @FXML Button activeReqButton;
  @FXML Button editButton;
  @FXML TextArea inputName;
  @FXML TextArea inputPatient;
  @FXML TextArea inputReligion;
  @FXML TextArea inputNotes;
  @FXML Text time;

  // edit
  @FXML TableView<ReligiousRequest> editTable;
  @FXML TableColumn<ReligiousRequest, String> ETabID;
  @FXML TableColumn<ReligiousRequest, String> ETabPatient;
  @FXML TableColumn<ReligiousRequest, String> ETabName;

  @FXML TextField editID;
  @FXML TextField editPatient;
  @FXML TextField editName;
  @FXML TextField editReligion;
  @FXML ComboBox<Location> editDest;
  @FXML ComboBox<Employee> editEmployee;
  @FXML TextField editStat;
  @FXML TextArea editNotes;

  @FXML Button submitEdit;
  @FXML Button removeButton;

  ObservableList<ReligiousRequest> religiousUIRequests = FXCollections.observableArrayList();
  // Udb udb;
  ArrayList<Location> nodes;
  ArrayList<Employee> staff;
  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    if (Udb.admin) {
      editButton.setVisible(true);
    } else {
      editButton.setVisible(false);
    }

    setUpAllReligiousReq();
    setUpEditReligiousReq();

    requestText.setVisible(false);
    // Displays Locations in Table View
    nodes = new ArrayList<>();
    for (Location l : Udb.getInstance().locationImpl.list()) {
      nodes.add(l);
    }
    locations.setTooltip(new Tooltip());
    locations.getItems().addAll(nodes);
    new ComboBoxAutoComplete<Location>(locations, 650, 290);
    editDest.setTooltip(new Tooltip());
    editDest.getItems().addAll(nodes);
    new ComboBoxAutoComplete<Location>(editDest, 650, 290);

    // Displays EMployess in Table View
    staff = new ArrayList<>();
    for (Employee l : Udb.getInstance().EmployeeImpl.hList().values()) {
      staff.add(l);
    }
    employees.setTooltip(new Tooltip());
    employees.getItems().addAll(staff);
    new ComboBoxAutoComplete<Employee>(employees, 675, 380);
    editEmployee.setTooltip(new Tooltip());
    editEmployee.getItems().addAll(staff);
    new ComboBoxAutoComplete<Employee>(editEmployee, 675, 380);

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
    destination.setCellValueFactory(new PropertyValueFactory<ReligiousRequest, String>("location"));
    employee.setCellValueFactory(new PropertyValueFactory<ReligiousRequest, String>("employee"));
    notes.setCellValueFactory(new PropertyValueFactory<ReligiousRequest, String>("notes"));
    table.setItems(getReligiousList());
  }

  private void setUpEditReligiousReq() throws SQLException, IOException {
    ETabID.setCellValueFactory(new PropertyValueFactory("ID"));
    ETabName.setCellValueFactory(new PropertyValueFactory<ReligiousRequest, String>("name"));
    ETabPatient.setCellValueFactory(
        new PropertyValueFactory<ReligiousRequest, String>("patientName"));
    editTable.setItems(getReligiousList());
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
    ReligiousRequest r =
        new ReligiousRequest(
            id, name, date, time, patientName, religion, status, destination, employee, notes);
    r.gettingTheLocation();
    religiousUIRequests.add(r);
    return religiousUIRequests;
  }

  private ObservableList<ReligiousRequest> getReligiousList() throws SQLException, IOException {
    religiousUIRequests.clear();
    for (ReligiousRequest request : Udb.getInstance().religiousRequestImpl.List.values()) {
      ReligiousRequest r =
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
              request.getNotes());
      r.gettingTheLocation();
      religiousUIRequests.add(r);
    }
    return religiousUIRequests;
  }

  @Override
  public void addRequest() {
    StringBuilder startRequestString = new StringBuilder("Your request for: ");

    String endRequest = "Has been placed successfully";
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    // String room = locations.getValue().toString();

    // String employ = employees.getValue().toString();

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
            locations.getValue().getNodeID(),
            employees.getValue(),
            inputNotes.getText().trim());

    request.gettingTheLocation();

    table.setItems(
        newRequest(
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
    try {
      Udb.getInstance().add(request);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void removeRequest() {
    ReligiousRequest request = editTable.getSelectionModel().getSelectedItem();
    religiousUIRequests.remove(request);
    try {
      Udb.getInstance().remove(request);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    clearUpdate();
  }

  @Override
  public void updateRequest() {
    ReligiousRequest oldRequest = editTable.getSelectionModel().getSelectedItem();
    religiousUIRequests.remove(oldRequest);
    String ID = oldRequest.getID();
    String name = editName.getText().trim();
    String oldDate = oldRequest.getDate();
    String oldTime = oldRequest.getTime();
    String patient = editPatient.getText().trim();
    String religion = editReligion.getText().trim();
    Location destination = editDest.getValue();
    Employee employee = editEmployee.getValue();
    String notes = editNotes.getText().trim();

    ReligiousRequest request =
        new ReligiousRequest(
            ID,
            name,
            oldDate,
            oldTime,
            patient,
            religion,
            "Pending",
            destination.getNodeID(),
            employee,
            notes);

    request.gettingTheLocation();

    table.setItems(
        newRequest(
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
    try {
      Udb.getInstance().remove(oldRequest);
      Udb.getInstance().add(request);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void updateFields() {
    ReligiousRequest temp = editTable.getSelectionModel().getSelectedItem();
    editID.setText(temp.getID());
    editPatient.setText(temp.getPatient());
    editName.setText(temp.getName());
    editReligion.setText(temp.getReligion());
    editDest.setValue(temp.getLocation());
    editEmployee.setValue(temp.getEmployee());
    editStat.setText(temp.getStatus());
    editNotes.setText(temp.getNotes());
  }

  public void clearUpdate() {
    editID.setText("");
    editPatient.setText("");
    editName.setText("");
    editReligion.setText("");
    editDest.setValue(null);
    editEmployee.setValue(null);
    editStat.setText("");
    editNotes.setText("");
  }

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
    editButton.setUnderline(false);
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
    editButton.setUnderline(false);
    newReqButton.setUnderline(false);
  }

  public void switchToEditRequest(ActionEvent actionEvent) {
    ObservableList<Node> stackNodes = requestsStack.getChildren();
    Node newReq = stackNodes.get(stackNodes.indexOf(editRequestPane));
    for (Node node : stackNodes) {
      node.setVisible(false);
    }
    newReq.setVisible(true);
    newReq.toBack();
    activeReqButton.setUnderline(false);
    newReqButton.setUnderline(false);
    editButton.setUnderline(true);
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
