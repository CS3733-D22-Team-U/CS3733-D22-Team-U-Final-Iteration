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
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.SneakyThrows;

public class CompServRequestController extends ServiceController {
  public ComboBox<Location> locations;
  public ComboBox<Employee> employees;

  @FXML Text time;
  @FXML TableColumn<CompServRequest, String> reqID;
  @FXML TableColumn<CompServRequest, String> reqDevice;
  @FXML TableColumn<CompServRequest, String> reqDestination;
  @FXML TableColumn<CompServRequest, String> reqMessage;
  @FXML TableColumn<CompServRequest, String> reqStatus;
  @FXML TableColumn<CompServRequest, String> reqEmployee;
  @FXML TableColumn<CompServRequest, String> reqDate;
  @FXML TableColumn<CompServRequest, String> reqTime;
  @FXML TableView<CompServRequest> table;

  @FXML Button clearButton;
  @FXML Button submitButton;

  // @FXML Text requestText;
  @FXML StackPane requestsStack;
  @FXML Pane newRequestPane;
  @FXML Pane allRequestPane;
  @FXML Text output;

  @FXML Button newReqButton;
  @FXML Button activeReqButton;
  @FXML TextArea messageBox;
  @FXML TextArea inputDevice;
  @FXML AnchorPane sideBarAnchor;
  @FXML Button sideBarButton;

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
    try {
      setUpAllCompServReq();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Displays Locations in Table View
    nodeIDs = new ArrayList<>();
    try {
      for (Location l : Udb.getInstance().locationImpl.list()) {
        nodeIDs.add(l);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    locations.setTooltip(new Tooltip());
    locations.getItems().addAll(nodeIDs);
    new ComboBoxAutoComplete<Location>(locations, 267, 347);

    // Displays Employees in Table View
    staff = new ArrayList<>();
    try {
      for (Employee l : Udb.getInstance().EmployeeImpl.hList().values()) {
        staff.add(l);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    employees.setTooltip(new Tooltip());
    employees.getItems().addAll(staff);
    new ComboBoxAutoComplete<Employee>(employees, 502, 380);

    handleTime();
    handleBar();
  }

  private void handleBar() {
    TranslateTransition openNav = new TranslateTransition(new Duration(350), sideBarAnchor);
    openNav.setToY(596);
    TranslateTransition closeNav = new TranslateTransition(new Duration(350), sideBarAnchor);
    sideBarButton.setOnAction(
        (ActionEvent evt) -> {
          if (sideBarAnchor.getTranslateY() != 596) {
            openNav.play();
          } else {
            closeNav.setToY(0);
            closeNav.play();
          }
        });
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
    masterThread = timeThread;
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
    if (locations.getValue() != null && locations.getValue() != null) {
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

      boolean alreadyHere = true;
      String serviceID = "notWork";

      while (alreadyHere) {
        double rand = Math.random() * 10000;

        try {
          alreadyHere =
              Udb.getInstance().compServRequestImpl.hList().containsKey("COM" + (int) rand);
        } catch (Exception e) {
          System.out.println("alreadyHere variable messed up in comp service request");
        }

        serviceID = "COM" + (int) rand;
      }

      CompServRequest request =
          new CompServRequest(
              serviceID,
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
    }
  }

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}

  public void clearRequest() {
    output.setText("Cleared Requests!");
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
    Node active = stackNodes.get(stackNodes.indexOf(allRequestPane));
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
