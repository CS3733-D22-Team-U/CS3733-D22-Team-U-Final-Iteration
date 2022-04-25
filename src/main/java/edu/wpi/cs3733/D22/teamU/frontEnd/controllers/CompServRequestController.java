package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.CompServRequest.CompServRequest;
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

public class CompServRequestController extends ServiceController {
  // global
  @FXML Text time;
  @FXML Button editButton;
  @FXML Button newReqButton;
  @FXML Button activeReqButton;

  // edit
  @FXML TextArea editID;
  @FXML TextArea editMessage;
  @FXML TextArea editDevice;
  @FXML TextArea editStatus;
  public ComboBox<Location> locations1;
  public ComboBox<Employee> employees1;
  @FXML Button submitButtonEdit;
  @FXML TableColumn<CompServRequest, String> disEDev;
  @FXML TableColumn<CompServRequest, String> disEDest;
  @FXML TableColumn<CompServRequest, String> disEEmployee;
  @FXML TableColumn<CompServRequest, String> disEStatus;
  @FXML TableView<CompServRequest> editTable;

  // display
  @FXML TableColumn<CompServRequest, String> reqID;
  @FXML TableColumn<CompServRequest, String> reqDevice;
  @FXML TableColumn<CompServRequest, String> reqDestination;
  @FXML TableColumn<CompServRequest, String> reqMessage;
  @FXML TableColumn<CompServRequest, String> reqStatus;
  @FXML TableColumn<CompServRequest, String> reqEmployee;
  @FXML TableColumn<CompServRequest, String> reqDate;
  @FXML TableColumn<CompServRequest, String> reqTime;
  @FXML TableView<CompServRequest> table;

  // new
  public ComboBox<Location> locations;
  public ComboBox<Employee> employees;
  @FXML TextArea messageBox;
  @FXML TextArea inputDevice;
  @FXML Button clearButton;
  @FXML Button submitButton;

  // @FXML Text requestText;
  @FXML StackPane requestsStack;
  @FXML Pane newRequestPane;
  @FXML Pane allRequestPane;
  @FXML Pane editRequestPane;
  @FXML Text output;

  ObservableList<CompServRequest> CompServUIRequests = FXCollections.observableArrayList();
  // Udb udb;
  ArrayList<Location> nodeIDs;
  ArrayList<Employee> staff;
  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // super.initialize(location, resources);
    // udb = Udb.getInstance();
    if (Udb.admin) {
      editButton.setVisible(true);
    } else {
      editButton.setVisible(false);
    }
    setUpAllCompServReq();
    setUpEditCompServReq();

    // Displays Locations in Table View
    nodeIDs = new ArrayList<>();
    for (Location l : Udb.getInstance().locationImpl.list()) {
      nodeIDs.add(l);
    }
    locations.setTooltip(new Tooltip());
    locations.getItems().addAll(nodeIDs);
    new ComboBoxAutoComplete<Location>(locations, 267, 347);
    locations1.setTooltip(new Tooltip());
    locations1.getItems().addAll(nodeIDs);
    new ComboBoxAutoComplete<Location>(locations1, 24, 518);

    // Displays Employees in Table View
    staff = new ArrayList<>();
    for (Employee l : Udb.getInstance().EmployeeImpl.hList().values()) {
      staff.add(l);
    }
    employees.setTooltip(new Tooltip());
    employees.getItems().addAll(staff);
    new ComboBoxAutoComplete<Employee>(employees, 502, 380);
    employees1.setTooltip(new Tooltip());
    employees1.getItems().addAll(staff);
    new ComboBoxAutoComplete<Employee>(employees1, 159, 518);

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

  private void setUpAllCompServReq() throws SQLException, IOException {
    reqID.setCellValueFactory(new PropertyValueFactory("ID"));
    reqDevice.setCellValueFactory(new PropertyValueFactory<CompServRequest, String>("device"));
    reqDestination.setCellValueFactory(
        new PropertyValueFactory<CompServRequest, String>("location"));
    reqMessage.setCellValueFactory(new PropertyValueFactory<CompServRequest, String>("message"));
    reqStatus.setCellValueFactory(new PropertyValueFactory<CompServRequest, String>("status"));
    reqEmployee.setCellValueFactory(new PropertyValueFactory<CompServRequest, String>("employee"));
    reqDate.setCellValueFactory(new PropertyValueFactory<CompServRequest, String>("date"));
    reqTime.setCellValueFactory(new PropertyValueFactory<CompServRequest, String>("time"));
    table.setItems(getCompServList());
  }

  private void setUpEditCompServReq() throws SQLException, IOException {
    disEDev.setCellValueFactory(new PropertyValueFactory<CompServRequest, String>("device"));
    disEDest.setCellValueFactory(new PropertyValueFactory<CompServRequest, String>("location"));
    disEStatus.setCellValueFactory(new PropertyValueFactory<CompServRequest, String>("status"));
    disEEmployee.setCellValueFactory(new PropertyValueFactory<CompServRequest, String>("employee"));
    editTable.setItems(getCompServList());
  }

  private ObservableList<CompServRequest> newRequest(
      String id,
      String device,
      String destination,
      String message,
      String status,
      Employee employee,
      String date,
      String time) {
    CompServRequest r =
        new CompServRequest(id, message, status, employee, destination, date, time, device);

    r.gettingTheLocation();
    CompServUIRequests.add(r);
    return CompServUIRequests;
  }

  private ObservableList<CompServRequest> getCompServList() throws SQLException, IOException {
    CompServUIRequests.clear();
    for (CompServRequest request : Udb.getInstance().compServRequestImpl.List.values()) {
      CompServRequest r =
          new CompServRequest(
              request.getID(),
              request.getMessage(),
              request.getStatus(),
              request.getEmployee(),
              request.getDestination(),
              request.getDate(),
              request.getTime(),
              request.getDevice());
      r.gettingTheLocation();

      CompServUIRequests.add(r);
    }

    return CompServUIRequests;
  }

  @Override
  public void addRequest() {
    if (locations.getValue() != null && employees.getValue() != null) {
      output.setText("Your computer service request has been made!");
      output.setVisible(true);
      new Thread(
              () -> {
                try {
                  Thread.sleep(1500); // milliseconds
                  Platform.runLater(
                      () -> {
                        output.setVisible(false);
                      });
                } catch (InterruptedException ie) {
                }
              })
          .start();
      Timestamp timestamp = new Timestamp(System.currentTimeMillis());
      Location room = locations.getValue();
      String message = messageBox.getText().trim();
      String device = inputDevice.getText().trim();
      Employee employee = employees.getValue();

      double rand = Math.random() * 10000;

      CompServRequest request =
          new CompServRequest(
              (int) rand + "",
              message,
              "Pending",
              employee,
              room.getNodeID(),
              sdf3.format(timestamp).substring(0, 10),
              sdf3.format(timestamp).substring(11),
              device);

      request.gettingTheLocation();
      CompServUIRequests.add(request);
      table.setItems(CompServUIRequests);
      try {

        Udb.getInstance().add(request);
      } catch (IOException e) {
        e.printStackTrace();
      } catch (SQLException e) {
        e.printStackTrace();
      }
      clearRequest();
    }
  }

  @Override
  public void updateRequest() {
    if (locations1.getValue() != null && employees1.getValue() != null) {
      new Thread(
              () -> {
                try {
                  Thread.sleep(1500); // milliseconds
                  Platform.runLater(
                      () -> {
                        output.setVisible(false);
                      });
                } catch (InterruptedException ie) {
                }
              })
          .start();

      CompServRequest oldRequest = editTable.getSelectionModel().getSelectedItem();
      String oldDate = oldRequest.getDate();
      String oldTime = oldRequest.getTime();
      CompServUIRequests.remove(oldRequest);
      String ID = editID.getText();
      Location room = locations1.getValue();
      String message = editMessage.getText().trim();
      String device = editDevice.getText().trim();
      Employee employee = employees1.getValue();
      String status = editStatus.getText().trim();

      CompServRequest request =
          new CompServRequest(
              ID, message, status, employee, room.getNodeID(), oldDate, oldTime, device);

      request.gettingTheLocation();
      CompServUIRequests.add(request);
      table.setItems(CompServUIRequests);
      try {
        Udb.getInstance().remove(oldRequest);
        Udb.getInstance().add(request);
      } catch (IOException e) {
        e.printStackTrace();
      } catch (SQLException e) {
        e.printStackTrace();
      }
      clearUpdate();
    }
  }

  public void updateFields() {
    CompServRequest temp = editTable.getSelectionModel().getSelectedItem();
    editID.setText(temp.getID());
    editMessage.setText(temp.getMessage());
    editDevice.setText(temp.getDevice());
    editStatus.setText(temp.getStatus());
    locations1.setValue(temp.getLocation());
    employees1.setValue(temp.getEmployee());
  }

  public void clearUpdate() {
    editID.setText("");
    editMessage.setText("");
    editDevice.setText("");
    editStatus.setText("");
    employees1.setValue(null);
    locations1.setValue(null);
  }

  @Override
  public void removeRequest() {
    new Thread(
            () -> {
              try {
                Thread.sleep(1500); // milliseconds
                Platform.runLater(
                    () -> {
                      output.setVisible(false);
                    });
              } catch (InterruptedException ie) {
              }
            })
        .start();
    CompServRequest request = editTable.getSelectionModel().getSelectedItem();
    CompServUIRequests.remove(request);
    try {
      Udb.getInstance().remove(request);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    clearUpdate();
  }

  public void clearRequest() {
    new Thread(
            () -> {
              try {
                Thread.sleep(1500); // milliseconds
                Platform.runLater(
                    () -> {
                      output.setVisible(false);
                    });
              } catch (InterruptedException ie) {
              }
            })
        .start();
    employees.setValue(null);
    locations.setValue(null);
    messageBox.setText("");
    inputDevice.setText("");
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
    editButton.setUnderline(false);
    activeReqButton.setUnderline(false);
    newReqButton.setUnderline(true);
  }

  public void switchToActive(ActionEvent actionEvent) {
    ObservableList<Node> stackNodes = requestsStack.getChildren();
    Node active = stackNodes.get(stackNodes.indexOf(allRequestPane));
    for (Node node : stackNodes) {
      node.setVisible(false);
    }
    active.setVisible(true);
    active.toBack();
    editButton.setUnderline(false);
    activeReqButton.setUnderline(true);
    newReqButton.setUnderline(false);
  }

  public void switchToEditRequest(ActionEvent actionEvent) {
    ObservableList<Node> stackNodes = requestsStack.getChildren();
    Node editReq = stackNodes.get(stackNodes.indexOf(editRequestPane));
    for (Node node : stackNodes) {
      node.setVisible(false);
    }
    editReq.setVisible(true);
    editReq.toBack();
    editButton.setUnderline(true);
    activeReqButton.setUnderline(false);
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
