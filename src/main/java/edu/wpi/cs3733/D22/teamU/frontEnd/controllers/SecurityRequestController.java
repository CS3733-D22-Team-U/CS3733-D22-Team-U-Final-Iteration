package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.SecurityRequest.SecurityRequest;
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

public class SecurityRequestController extends ServiceController {

  public ComboBox<String> locations;
  public ComboBox<String> staffDropDown;
  @FXML Text requestText;
  @FXML Button clearButton;
  @FXML Button submitButton;
  @FXML CheckBox lethalForceButton;
  // these are for the table attributes shown to the user
  @FXML TableColumn<SecurityRequest, String> activeReqID;
  @FXML TableColumn<SecurityRequest, String> activename;
  @FXML TableColumn<SecurityRequest, String> activeReqStatus;
  @FXML TableColumn<SecurityRequest, String> activeStaff;
  @FXML TableColumn<SecurityRequest, String> activeReqDestination;
  @FXML TableColumn<SecurityRequest, String> activeReqDescription;
  @FXML TableColumn<SecurityRequest, String> activelethal;
  @FXML TableColumn<SecurityRequest, String> activeDate;
  @FXML TableColumn<SecurityRequest, String> activeTime;

  @FXML TableView<SecurityRequest> activeRequestTable;

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

  ObservableList<SecurityRequest> securityUIRequests = FXCollections.observableArrayList();
  ArrayList<String> nodeIDs;
  ArrayList<String> staff;
  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public void fillDestinations() throws SQLException, IOException {
    nodeIDs = new ArrayList<>();
    for (Location l : Udb.getInstance().locationImpl.list()) {
      nodeIDs.add(l.getNodeID());
    }
    locations.setTooltip(new Tooltip());
    locations.getItems().addAll(nodeIDs);
    new ComboBoxAutoComplete<String>(locations, 650, 290);
  }

  public void fillStaff() throws SQLException, IOException {
    staff = new ArrayList<>();
    for (Employee l : Udb.getInstance().EmployeeImpl.hList().values()) {
      staff.add(l.getEmployeeID());
    }

    staffDropDown.setTooltip(new Tooltip());
    staffDropDown.getItems().addAll(staff);
    new ComboBoxAutoComplete<String>(staffDropDown, 675, 400);
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
    activeReqID.setCellValueFactory(new PropertyValueFactory<SecurityRequest, String>("ID"));
    activename.setCellValueFactory(new PropertyValueFactory<SecurityRequest, String>("name"));
    activeReqStatus.setCellValueFactory(
        new PropertyValueFactory<SecurityRequest, String>("status"));
    activeStaff.setCellValueFactory(
        new PropertyValueFactory<SecurityRequest, String>("employeeName"));
    activeReqDestination.setCellValueFactory(
        new PropertyValueFactory<SecurityRequest, String>("destination"));
    activeReqDescription.setCellValueFactory(
        new PropertyValueFactory<SecurityRequest, String>("descriptionOfThreat"));
    activelethal.setCellValueFactory(
        new PropertyValueFactory<SecurityRequest, String>("leathalForcePermited"));
    activeDate.setCellValueFactory(new PropertyValueFactory<SecurityRequest, String>("date"));
    activeTime.setCellValueFactory(new PropertyValueFactory<SecurityRequest, String>("time"));

    activeRequestTable.setItems(getSecurityRequestsList());
  }

  private ObservableList<SecurityRequest> newRequest(
      String id,
      String name,
      String status,
      Employee employee,
      String destination,
      String descript,
      String lethal,
      String date,
      String time) {
    securityUIRequests.add(
        new SecurityRequest(id, name, status, employee, destination, descript, lethal, date, time));
    return securityUIRequests;
  }

  private ObservableList<SecurityRequest> getSecurityRequestsList()
      throws SQLException, IOException {
    securityUIRequests.clear();
    for (SecurityRequest securityRequest : Udb.getInstance().securityRequestImpl.List.values()) {
      securityUIRequests.add(
          new SecurityRequest(
              securityRequest.getID(),
              securityRequest.getName(),
              securityRequest.getStatus(),
              securityRequest.getEmployee(),
              securityRequest.getDestination(),
              securityRequest.getDescriptionOfThreat(),
              securityRequest.getLeathalForcePermited(),
              securityRequest.getDate(),
              securityRequest.getTime()));
    }

    return securityUIRequests;
  }

  private ObservableList<SecurityRequest> getActiveSecurityRequestList()
      throws SQLException, IOException {
    for (SecurityRequest securityRequest : Udb.getInstance().securityRequestImpl.List.values()) {
      securityUIRequests.add(
          new SecurityRequest(
              securityRequest.getID(),
              securityRequest.getName(),
              securityRequest.getStatus(),
              securityRequest.getEmployee(),
              securityRequest.getDestination(),
              securityRequest.getDescriptionOfThreat(),
              securityRequest.getLeathalForcePermited(),
              securityRequest.getDate(),
              securityRequest.getTime()));
    }
    return securityUIRequests;
  }

  @Override
  public void addRequest() throws SQLException, IOException {
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

    String employ = staffDropDown.getValue();

    String lethal = "No";

    if (lethalForceButton.isSelected()) {
      lethal = "Yes";
    }

    SecurityRequest request =
        new SecurityRequest(
            (int) rand + "",
            "admin",
            "Pending",
            checkEmployee(employ),
            locations.getValue(),
            textInput.getText().trim(),
            lethal,
            sdf3.format(timestamp).substring(0, 10),
            sdf3.format(timestamp).substring(11));

    activeRequestTable.setItems(
        newRequest(
            request.getID(),
            request.getName(),
            request.getStatus(),
            request.getEmployee(),
            request.getDestination(),
            request.getDescriptionOfThreat(),
            request.getLeathalForcePermited(),
            request.getDate(),
            request.getTime()));

    try {
      Udb.getInstance().add(request);

    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
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
