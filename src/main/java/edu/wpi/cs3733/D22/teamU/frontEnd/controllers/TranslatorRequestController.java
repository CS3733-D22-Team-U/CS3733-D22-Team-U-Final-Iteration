package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.TranslatorRequest.TranslatorRequest;
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

public class TranslatorRequestController extends ServiceController {

  public ComboBox<Location> locations;
  public ComboBox<Employee> employees;

  @FXML TableColumn<TranslatorRequest, String> nameID;
  @FXML TableColumn<TranslatorRequest, String> patientName;
  @FXML TableColumn<TranslatorRequest, String> toLang;
  @FXML TableColumn<TranslatorRequest, String> status;
  @FXML TableColumn<TranslatorRequest, String> employeeName;
  @FXML TableColumn<TranslatorRequest, String> destination;
  @FXML TableColumn<TranslatorRequest, String> date;
  @FXML TableColumn<TranslatorRequest, String> newTime;
  @FXML TableView<TranslatorRequest> table;

  @FXML Button clearButton;
  @FXML Button submitButton;

  @FXML Text requestText;
  @FXML StackPane requestsStack;
  @FXML Pane newRequestPane;
  @FXML Pane allActiveRequestsPane;

  @FXML Button newReqButton;
  @FXML Button activeReqButton;
  @FXML TextArea inputLanguage;
  @FXML TextArea inputPatient;
  @FXML Text time;

  ObservableList<TranslatorRequest> translatorUIRequests = FXCollections.observableArrayList();
  // Udb udb;
  ArrayList<Location> nodeIDs;
  ArrayList<Employee> staff;

  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // super.initialize(location, resources);
    // udb = Udb.getInstance();
    setUpAllTranslatorReq();

    // Displays Locations in Table View
    nodeIDs = new ArrayList<>();
    for (Location l : Udb.getInstance().locationImpl.list()) {
      nodeIDs.add(l);
    }
    locations.setTooltip(new Tooltip());
    locations.getItems().addAll(nodeIDs);
    new ComboBoxAutoComplete<Location>(locations, 650, 290);

    // Displays Emloyee in Table View
    staff = new ArrayList<>();
    for (Employee e : Udb.getInstance().EmployeeImpl.hList().values()) {
      staff.add(e);
    }
    employees.setTooltip(new Tooltip());
    employees.getItems().addAll(staff);
    new ComboBoxAutoComplete<Employee>(employees, 675, 380);

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
  private void setUpAllTranslatorReq() throws SQLException, IOException {
    nameID.setCellValueFactory(new PropertyValueFactory("ID"));
    patientName.setCellValueFactory(
        new PropertyValueFactory<TranslatorRequest, String>("patientName"));
    toLang.setCellValueFactory(new PropertyValueFactory<TranslatorRequest, String>("toLang"));
    status.setCellValueFactory(new PropertyValueFactory<TranslatorRequest, String>("status"));
    employeeName.setCellValueFactory(
        new PropertyValueFactory<TranslatorRequest, String>("employee"));
    destination.setCellValueFactory(
        new PropertyValueFactory<TranslatorRequest, String>("location"));
    date.setCellValueFactory(new PropertyValueFactory<TranslatorRequest, String>("date"));
    newTime.setCellValueFactory(new PropertyValueFactory<TranslatorRequest, String>("time"));
    table.setItems(getTranslatorList());
  }

  private ObservableList<TranslatorRequest> newRequest(
      String id,
      String patientName,
      String toLang,
      String status,
      Employee employee,
      String destination,
      String date,
      String time) {
    TranslatorRequest r =
        new TranslatorRequest(id, patientName, toLang, status, employee, destination, date, time);
    r.gettingTheLocation();
    translatorUIRequests.add(r);
    return translatorUIRequests;
  }

  private ObservableList<TranslatorRequest> getTranslatorList() throws SQLException, IOException {
    translatorUIRequests.clear();
    for (TranslatorRequest request : Udb.getInstance().translatorRequestImpl.List.values()) {
      TranslatorRequest r =
          new TranslatorRequest(
              request.getID(),
              request.getPatientName(),
              request.getToLang(),
              request.getStatus(),
              request.getEmployee(),
              request.getDestination(),
              request.getDate(),
              request.getTime());
      r.gettingTheLocation();
      translatorUIRequests.add(r);
    }
    return translatorUIRequests;
  }

  @Override
  public void addRequest() {
    StringBuilder startRequestString = new StringBuilder("Your request for: ");

    String endRequest = "Has been placed successfully";
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    // String room = locations.getValue().getNodeID();
    // String employ = employees.getValue().getEmployeeID();

    boolean alreadyHere = true;
    String serviceID = "notWork";

    // makes the id
    while (alreadyHere) {
      double rand = Math.random() * 10000;

      try {
        alreadyHere = Udb.getInstance().compServRequestImpl.hList().containsKey("TRA" + (int) rand);
      } catch (Exception e) {
        System.out.println(
            "alreadyHere variable messed up in translation service request controller");
      }

      serviceID = "TRA" + (int) rand;
    }

    TranslatorRequest request =
        new TranslatorRequest(
            serviceID,
            "Patient",
            inputLanguage.getText().trim(),
            "Pending",
            employees.getValue(),
            locations.getValue().getNodeID(),
            sdf3.format(timestamp).substring(0, 10),
            sdf3.format(timestamp).substring(11));

    request.gettingTheLocation();

    table.setItems(
        newRequest(
            request.getID(),
            request.getPatientName(),
            request.getToLang(),
            request.getStatus(),
            request.getEmployee(),
            request.getDestination(),
            request.getDate(),
            request.getTime()));
    try {
      Udb.getInstance().add(request);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("i");
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println("d");
    }
    inputPatient.setText("");
    inputLanguage.setText("");
    employees.getSelectionModel().clearSelection();
    locations.getSelectionModel().clearSelection();
  }

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}

  public void clearRequest() {
    requestText.setText("Cleared Requests!");
    requestText.setVisible(true);

    inputPatient.setText("");
    inputLanguage.setText("");
    employees.getSelectionModel().clearSelection();
    locations.getSelectionModel().clearSelection();
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
