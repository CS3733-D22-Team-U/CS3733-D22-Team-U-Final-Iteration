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
import javafx.scene.text.Text;
import lombok.SneakyThrows;

public class MaintenanceRequestController extends ServiceController {

  public ComboBox<Location> locations;
  public ComboBox<Employee> staffDropDown;
  @FXML Text requestText;
  @FXML Button clearButton;
  @FXML Button submitButton;

  // these are for the table attributes shown to the user
  @FXML TableColumn<MaintenanceRequest, String> activeReqID;
  @FXML TableColumn<MaintenanceRequest, String> activeReqStatus;
  @FXML TableColumn<MaintenanceRequest, String> activeReqDestination;
  @FXML TableColumn<MaintenanceRequest, String> activeReqDescription;
  @FXML TableColumn<MaintenanceRequest, String> activeStaff;
  @FXML TableColumn<MaintenanceRequest, String> activeDate;
  @FXML TableColumn<MaintenanceRequest, String> activeTime;

  @FXML TableView<MaintenanceRequest> activeRequestTable;

  @FXML StackPane requestsStack;
  @FXML Pane newRequestPane;
  @FXML Pane activeRequestPane;
  @FXML Button newReqButton;
  @FXML Button activeReqButton;
  @FXML Text time;
  @FXML Text sucessRequest;
  @FXML Text clearRequest;
  @FXML Text missingDescription;

  @FXML TextArea textInput;

  ObservableList<MaintenanceRequest> maintenanceRequests = FXCollections.observableArrayList();
  ArrayList<Location> nodeIDs;
  ArrayList<Employee> staff;
  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public void fillDestinations() throws SQLException, IOException {
    nodeIDs = new ArrayList<>();
    for (Location l : Udb.getInstance().locationImpl.list()) {
      nodeIDs.add(l);
    }
    locations.setTooltip(new Tooltip());
    locations.getItems().addAll(nodeIDs);
    new ComboBoxAutoComplete<Location>(locations, 650, 290);
  }

  public void fillStaff() throws SQLException, IOException {
    staff = new ArrayList<>();
    for (Employee l : Udb.getInstance().EmployeeImpl.hList().values()) {
      staff.add(l);
    }

    staffDropDown.setTooltip(new Tooltip());
    staffDropDown.getItems().addAll(staff);
    new ComboBoxAutoComplete<Employee>(staffDropDown, 675, 400);
  }

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    sucessRequest.setVisible(false);
    clearRequest.setVisible(false);
    missingDescription.setVisible(false);

    setUpAllMaintenance();

    fillDestinations();
    fillStaff();

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
        new PropertyValueFactory<MaintenanceRequest, String>("location"));
    activeReqDescription.setCellValueFactory(
        new PropertyValueFactory<MaintenanceRequest, String>("description"));
    activeStaff.setCellValueFactory(
        new PropertyValueFactory<MaintenanceRequest, String>("employee"));
    activeDate.setCellValueFactory(new PropertyValueFactory<MaintenanceRequest, String>("date"));
    activeTime.setCellValueFactory(new PropertyValueFactory<MaintenanceRequest, String>("time"));

    activeRequestTable.setItems(getMaintenanceRequestsList());
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
    MaintenanceRequest r =
        new MaintenanceRequest(
            id,
            name,
            status,
            destination,
            employee,
            typeOfMaintenanceRequest,
            description,
            date,
            time);
    r.gettingTheLocation();
    maintenanceRequests.add(r);

    return maintenanceRequests;
  }

  private ObservableList<MaintenanceRequest> getMaintenanceRequestsList()
      throws SQLException, IOException {
    maintenanceRequests.clear();
    for (MaintenanceRequest maintenanceReq :
        Udb.getInstance().maintenanceRequestImpl.List.values()) {

      MaintenanceRequest r =
          new MaintenanceRequest(
              maintenanceReq.getID(),
              maintenanceReq.getName(),
              maintenanceReq.getStatus(),
              maintenanceReq.getDestination(),
              maintenanceReq.getEmployee(),
              maintenanceReq.getTypeOfMaintenance(),
              maintenanceReq.getDescription(),
              maintenanceReq.getDate(),
              maintenanceReq.getTime());

      r.gettingTheLocation();
      maintenanceRequests.add(r);
    }

    return maintenanceRequests;
  }

  private ObservableList<MaintenanceRequest> getActiveMaintenanceRequestList()
      throws SQLException, IOException {
    for (MaintenanceRequest maintenanceReq :
        Udb.getInstance().maintenanceRequestImpl.List.values()) {
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

  @Override
  public void addRequest() {
    if (textInput.getText().equals("")) {
      missingDescription.setVisible(true);
      new Thread(
              () -> {
                try {
                  Thread.sleep(3500); // milliseconds
                  Platform.runLater(
                      () -> {
                        missingDescription.setVisible(false);
                      });
                } catch (InterruptedException ie) {
                }
              })
          .start();
      return;
    }

    clearRequest.setVisible(false);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    double rand = Math.random() * 10000;

    // String empty = staffDropDown.getValue();

    MaintenanceRequest request =
        new MaintenanceRequest(
            (int) rand + "",
            "N/A",
            "Pending",
            locations.getValue().getNodeID(),
            staffDropDown.getValue(),
            "N/A",
            textInput.getText().trim(),
            sdf3.format(timestamp).substring(0, 10),
            sdf3.format(timestamp).substring(11));

    request.gettingTheLocation();
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
      System.out.printf("print first catch");
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.printf("print second catch");
    }

    sucessRequest.setVisible(true);
    new Thread(
            () -> {
              try {
                Thread.sleep(3500); // milliseconds
                Platform.runLater(
                    () -> {
                      sucessRequest.setVisible(false);
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
    sucessRequest.setVisible(false);
    clearRequest.setVisible(true);
    textInput.setText("");
    staffDropDown.getItems().clear();
    locations.getItems().clear();
    new Thread(
            () -> {
              try {
                Thread.sleep(1500); // milliseconds
                Platform.runLater(
                    () -> {
                      clearRequest.setVisible(false);
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
    Node active = stackNodes.get(stackNodes.indexOf(activeRequestPane));
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
