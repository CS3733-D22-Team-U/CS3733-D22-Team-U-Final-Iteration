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

public class SecurityRequestController extends ServiceController {

  public ComboBox<Location> locations;
  public ComboBox<Employee> staffDropDown;
  @FXML Text requestText;
  @FXML Button clearButton;
  @FXML Button submitButton;
  @FXML CheckBox lethalForceButton;
  // these are for the table attributes shown to the user
  @FXML TableColumn<SecurityRequest, String> activeReqID;
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
  @FXML AnchorPane sideBarAnchor;
  @FXML Button sideBarButton;

  @FXML TextArea textInput;

  ObservableList<SecurityRequest> securityUIRequests = FXCollections.observableArrayList();
  ArrayList<String> nodeIDs;
  ArrayList<String> staff;
  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public void fillDestinations() throws SQLException, IOException {
    locations.setTooltip(new Tooltip());
    locations.getItems().addAll(Udb.getInstance().locationImpl.locations);
    new ComboBoxAutoComplete<Location>(locations, 650, 290);
  }

  public void fillStaff() throws SQLException, IOException {

    staffDropDown.setTooltip(new Tooltip());
    staffDropDown.getItems().addAll(Udb.getInstance().EmployeeImpl.hList().values());
    new ComboBoxAutoComplete<Employee>(staffDropDown, 675, 400);
  }

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    sucessRequest.setVisible(false);
    clearRequest.setVisible(false);
    missingDescription.setVisible(false);

    try {
      setUpAllMaintenance();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    try {
      fillDestinations();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      fillStaff();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

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

  private void setUpAllMaintenance() throws SQLException, IOException {
    activeReqID.setCellValueFactory(new PropertyValueFactory<SecurityRequest, String>("ID"));
    activeReqStatus.setCellValueFactory(
        new PropertyValueFactory<SecurityRequest, String>("status"));
    activeStaff.setCellValueFactory(new PropertyValueFactory<SecurityRequest, String>("employee"));
    activeReqDestination.setCellValueFactory(
        new PropertyValueFactory<SecurityRequest, String>("location"));
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
    SecurityRequest r =
        new SecurityRequest(id, name, status, employee, destination, descript, lethal, date, time);
    r.gettingTheLocation();
    securityUIRequests.add(r);
    return securityUIRequests;
  }

  private ObservableList<SecurityRequest> getSecurityRequestsList()
      throws SQLException, IOException {
    securityUIRequests.clear();
    for (SecurityRequest securityRequest : Udb.getInstance().securityRequestImpl.List.values()) {
      SecurityRequest r =
          new SecurityRequest(
              securityRequest.getID(),
              securityRequest.getName(),
              securityRequest.getStatus(),
              securityRequest.getEmployee(),
              securityRequest.getDestination(),
              securityRequest.getDescriptionOfThreat(),
              securityRequest.getLeathalForcePermited(),
              securityRequest.getDate(),
              securityRequest.getTime());
      r.gettingTheLocation();
      securityUIRequests.add(r);
    }

    return securityUIRequests;
  }

  private ObservableList<SecurityRequest> getActiveSecurityRequestList()
      throws SQLException, IOException {
    for (SecurityRequest securityRequest : Udb.getInstance().securityRequestImpl.List.values()) {
      SecurityRequest r =
          new SecurityRequest(
              securityRequest.getID(),
              securityRequest.getName(),
              securityRequest.getStatus(),
              securityRequest.getEmployee(),
              securityRequest.getDestination(),
              securityRequest.getDescriptionOfThreat(),
              securityRequest.getLeathalForcePermited(),
              securityRequest.getDate(),
              securityRequest.getTime());
      r.gettingTheLocation();
      securityUIRequests.add(r);
    }
    return securityUIRequests;
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

    boolean alreadyHere = true;
    String serviceID = "notWork";

    // makes the id
    while (alreadyHere) {
      double rand = Math.random() * 10000;

      try {
        alreadyHere = Udb.getInstance().compServRequestImpl.hList().containsKey("SEC" + (int) rand);
      } catch (Exception e) {
        System.out.println(
            "alreadyHere variable messed up in sercurity service request controller");
      }

      serviceID = "SEC" + (int) rand;
    }

    String employ = staffDropDown.getValue().getEmployeeID();

    String lethal = "No";

    if (lethalForceButton.isSelected()) {
      lethal = "Yes";
    }

    SecurityRequest request =
        new SecurityRequest(
            serviceID,
            "admin",
            "Pending",
            checkEmployee(employ),
            locations.getValue().getNodeID(),
            textInput.getText().trim(),
            lethal,
            sdf3.format(timestamp).substring(0, 10),
            sdf3.format(timestamp).substring(11));
    request.gettingTheLocation();

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
    staffDropDown.getSelectionModel().clearSelection();
    locations.getSelectionModel().clearSelection();
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
