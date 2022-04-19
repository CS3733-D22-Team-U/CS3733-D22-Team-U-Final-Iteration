package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MaintenanceRequest.MaintenanceRequest;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.SneakyThrows;

public class MaintenanceRequestController extends ServiceController {

  public ComboBox<String> locations;
  public ComboBox<String> employees;
  @FXML TableView<MaintenanceRequest> table;
  @FXML VBox requestHolder;
  @FXML Text requestText;
  @FXML Button clearButton;
  @FXML Button submitButton;

  // these are for the table attributes shown to the user
  @FXML TableColumn<MaintenanceRequest, String> activeReqID;
  @FXML TableColumn<MaintenanceRequest, String> activeReqStatus;
  @FXML TableColumn<MaintenanceRequest, String> activeReqDestination;
  @FXML TableColumn<MaintenanceRequest, String> activeReqDescription;
  @FXML TableColumn<MaintenanceRequest, String> activeDate;
  @FXML TableColumn<MaintenanceRequest, String> activeTime;

  @FXML TableView<MaintenanceRequest> activeRequestTable;
  @FXML VBox inputFields;

  @FXML StackPane requestsStack;
  @FXML Pane newRequestPane;
  @FXML Pane allEquipPane;
  @FXML Pane activeRequestPane;

  @FXML Button newReqButton;
  @FXML Button activeReqButton;
  @FXML Button allEquipButton;
  @FXML Text time;

  @FXML TextArea textInput;

  ObservableList<MaintenanceRequest> maintenanceRequests = FXCollections.observableArrayList();
  ObservableList<MaintenanceRequest> maintenanceUIRequests = FXCollections.observableArrayList();
  // Udb udb;
  ArrayList<String> nodeIDs;
  ArrayList<String> staff;
  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // super.initialize(location, resources);
    // udb = Udb.getInstance();
    setUpAllMaintenance();
    // setUpActiveRequests();
    nodeIDs = new ArrayList<>();
    for (Location l : Udb.getInstance().locationImpl.list()) {
      nodeIDs.add(l.getNodeID());
    }
    locations.setTooltip(new Tooltip());
    locations.getItems().addAll(nodeIDs);
    new ComboBoxAutoComplete<String>(locations, 650, 290);

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

  private void setUpAllMaintenance() throws SQLException, IOException {
    activeReqID.setCellValueFactory(new PropertyValueFactory<MaintenanceRequest, String>("ID"));
    activeReqStatus.setCellValueFactory(
        new PropertyValueFactory<MaintenanceRequest, String>("status"));
    activeReqDestination.setCellValueFactory(
        new PropertyValueFactory<MaintenanceRequest, String>("destination"));
    activeReqDescription.setCellValueFactory(
        new PropertyValueFactory<MaintenanceRequest, String>("description"));
    activeDate.setCellValueFactory(new PropertyValueFactory<MaintenanceRequest, String>("date"));
    activeTime.setCellValueFactory(new PropertyValueFactory<MaintenanceRequest, String>("time"));
  }

  private ObservableList<MaintenanceRequest> newRequest(
      String id,
      String name,
      String status,
      String destination,
      Employee employee,
      String typeOfMaintenanceRequest,
      String description,
      String date,
      String time) {
    maintenanceUIRequests.add(
        new MaintenanceRequest(
            id,
            name,
            status,
            destination,
            employee,
            typeOfMaintenanceRequest,
            description,
            date,
            time));
    return maintenanceUIRequests;
  }

  private ObservableList<MaintenanceRequest> getMaintenanceRequestsList()
      throws SQLException, IOException {
    maintenanceRequests.clear();
    for (MaintenanceRequest maintenanceReq : Udb.getInstance().maintenanceRequestImpl.list) {
      maintenanceRequests.add(
          new MaintenanceRequest(
              maintenanceReq.getID(),
              maintenanceReq.getName(),
              maintenanceReq.getStatus(),
              maintenanceReq.getDestination(),
              maintenanceReq.getEmployee(),
              maintenanceReq.getTypeOfMaintenance(),
              maintenanceReq.getDescription(),
              maintenanceReq.getDate(),
              maintenanceReq.getTime()));
    }

    return maintenanceRequests;
  }

  private ObservableList<MaintenanceRequest> getActiveMaintenanceRequestList()
      throws SQLException, IOException {
    for (MaintenanceRequest maintenanceReq : Udb.getInstance().maintenanceRequestImpl.list) {
      maintenanceUIRequests.add(
          new MaintenanceRequest(
              maintenanceReq.getID(),
              maintenanceReq.getName(),
              maintenanceReq.getStatus(),
              maintenanceReq.getDestination(),
              maintenanceReq.getEmployee(),
              maintenanceReq.getTypeOfMaintenance(),
              maintenanceReq.getDescription(),
              maintenanceReq.getDate(),
              maintenanceReq.getTime()));
    }
    return maintenanceUIRequests;
  }

  @Override
  public void addRequest() {
    StringBuilder startRequestString = new StringBuilder("Your request for : ");

    String endRequest = " has been placed successfully";
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    double rand = Math.random() * 10000;

    Employee empty = new Employee("N/A");

    MaintenanceRequest request =
        new MaintenanceRequest(
            (int) rand + "",
            "N/A",
            "Pending",
            locations.getValue(),
            empty,
            "N/A",
            textInput.getText().trim(),
            sdf3.format(timestamp).substring(0, 10),
            sdf3.format(timestamp).substring(11));

    activeRequestTable.setItems(
        newRequest(
            request.getID(),
            request.getName(),
            request.getStatus(),
            request.getDestination(),
            request.getEmployee(),
            request.getTypeOfMaintenance(),
            request.getDescription(),
            request.getDate(),
            request.getTime()));
    try {
      Udb.getInstance().add(request);

    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    requestText.setText(startRequestString + endRequest);
    requestText.setVisible(true);
    new Thread(
            () -> {
              try {
                Thread.sleep(3500); // milliseconds
                Platform.runLater(
                    () -> {
                      requestText.setVisible(false);
                    });
              } catch (InterruptedException ie) {
              }
            })
        .start();
  }

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}

  public void clearRequest() {
    requestText.setText("Cleared Requests!");
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
    allEquipButton.setUnderline(false);
  }

  public void switchToActive(ActionEvent actionEvent) {
    ObservableList<Node> stackNodes = requestsStack.getChildren();
    Node active = stackNodes.get(stackNodes.indexOf(activeRequestPane));
    for (Node node : stackNodes) {
      node.setVisible(false);
    }
    active.setVisible(true);
    active.toBack();
    activeReqButton.setUnderline(true);
    newReqButton.setUnderline(false);
    allEquipButton.setUnderline(false);
  }

  public void switchToEquipment(ActionEvent actionEvent) {
    ObservableList<Node> stackNodes = requestsStack.getChildren();
    Node active = stackNodes.get(stackNodes.indexOf(allEquipPane));
    for (Node node : stackNodes) {
      node.setVisible(false);
    }
    active.setVisible(true);
    active.toBack();
    activeReqButton.setUnderline(false);
    newReqButton.setUnderline(false);
    allEquipButton.setUnderline(true);
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
