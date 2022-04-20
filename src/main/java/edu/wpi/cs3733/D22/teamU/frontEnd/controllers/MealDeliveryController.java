package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXCheckBox;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MealRequest.MealRequest;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.SneakyThrows;

public class MealDeliveryController extends ServiceController {

  public ComboBox<String> locations;
  public ComboBox<String> employees;
  public ComboBox<String> patients;

  @FXML CheckBox veganCheckbox;
  @FXML CheckBox vegCheckbox;
  @FXML CheckBox glutenCheckbox;
  @FXML CheckBox kosherCheckbox;
  @FXML CheckBox halalCheckbox;
  @FXML CheckBox lactoseCheckbox;
  @FXML CheckBox nutsCheckbox;
  @FXML CheckBox shellfishCheckbox;

  @FXML TableColumn<MealRequest, String> activeReqID;
  @FXML TableColumn<MealRequest, String> activeReqName;
  @FXML TableColumn<MealRequest, String> activeReqDiet;
  @FXML TableColumn<MealRequest, String> activeReqDestination;
  @FXML TableColumn<MealRequest, String> activeReqEmployee;
  @FXML TableColumn<MealRequest, String> activeNotes;
  @FXML TableColumn<MealRequest, String> activeDate;
  @FXML TableColumn<MealRequest, String> activeTime;
  @FXML TableColumn<MealRequest, String> activeStatus;
  @FXML TableView<MealRequest> activeRequestTable;

  @FXML StackPane requestsStack;
  @FXML Pane newRequestPane;
  @FXML Pane activeRequestPane;
  @FXML VBox requestHolder;
  @FXML Button clearButton;
  @FXML Button submitButton;
  @FXML Button newReqButton;
  @FXML Button activeReqButton;
  @FXML Text time;
  ObservableList<MealRequest> meals = FXCollections.observableArrayList();
  ObservableList<JFXCheckBox> checkBoxes = FXCollections.observableArrayList();
  ObservableList<MealRequest> MealRequests = FXCollections.observableArrayList();
  ObservableList<String> patientList =
      FXCollections.observableArrayList("Deepti", "Kody", "Joselin");
  ObservableList<String> StatusList = FXCollections.observableArrayList("Processing", "Done");

  @FXML TextArea notesMeal;
  @FXML StackPane stack;
  @FXML AnchorPane masterPane;
  ArrayList<String> nodeIDs;
  ArrayList<String> staff;
  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setUpActiveRequests();
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
    for (Node checkBox : requestHolder.getChildren()) {
      checkBoxes.add((JFXCheckBox) checkBox);
    }

    employees.setTooltip(new Tooltip());
    employees.getItems().addAll(staff);

    patients.getItems().addAll(patientList);

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

  public Employee checkEmployee(String employee) throws NullPointerException {
    if (EmployeeDaoImpl.List.get(employee) != null) {
      return EmployeeDaoImpl.List.get(employee);
    } else {
      Employee empty = new Employee("N/A");
      return empty;
    }
  }

  private void setUpActiveRequests() throws SQLException, IOException {
    activeReqID.setCellValueFactory(new PropertyValueFactory<>("ID"));
    activeReqName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
    activeReqDiet.setCellValueFactory(new PropertyValueFactory<>("dietRest"));
    activeStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    activeReqEmployee.setCellValueFactory(new PropertyValueFactory<>("employee"));
    activeReqDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));
    activeNotes.setCellValueFactory(new PropertyValueFactory<>("addNotes"));
    activeDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    activeTime.setCellValueFactory(new PropertyValueFactory<>("time"));
    activeRequestTable.setItems(getActiveRequestList());
  }

  private ObservableList<MealRequest> newRequest(
      String ID,
      String patientName,
      String dietRest,
      String status,
      Employee employee,
      String destination,
      String addNotes,
      String date,
      String time) {
    meals.add(
        new MealRequest(
            ID, patientName, dietRest, status, employee, destination, addNotes, date, time));
    return meals;
  }

  private ObservableList<MealRequest> getActiveRequestList() throws SQLException, IOException {
    for (MealRequest aMeal : Udb.getInstance().mealRequestImpl.hList().values()) {
      MealRequests.add(
          new MealRequest(
              aMeal.getID(),
              aMeal.getPatientName(),
              aMeal.getDietRest(),
              aMeal.getStatus(),
              aMeal.getEmployee(),
              aMeal.getDestination(),
              aMeal.getAddNotes(),
              aMeal.getDate(),
              aMeal.getTime()));
    }
    return MealRequests;
  }

  @Override
  public void addRequest() {
    int requestAmount = 0;
    String inputString = "";
    for (int i = 0; i < checkBoxes.size(); i++) {
      if (checkBoxes.get(i).isSelected()) {
        inputString += checkBoxes.get(i).getText().trim() + ", ";
      }
    }
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    String room = locations.getValue();
    String em = employees.getValue();
    // Employee empty = new Employee(employees.getValue());
    double rand = Math.random() * 10000;

    MealRequest request =
        new MealRequest(
            (int) rand + "",
            patients.getValue(),
            inputString,
            "Ordered",
            checkEmployee(em),
            room,
            notesMeal.getText(),
            sdf3.format(timestamp).substring(0, 10),
            sdf3.format(timestamp).substring(11));

    activeRequestTable.setItems(
        newRequest(
            request.getID(),
            request.getPatientName(),
            request.getDietRest(),
            request.getStatus(),
            request.getEmployee(),
            request.getDestination(),
            request.getAddNotes(),
            request.getDate(),
            request.getTime()));
    try {
      Udb.getInstance()
          .add( // TODO Have random ID and enter Room Destination
              new MealRequest(
                  request.getID(),
                  request.getPatientName(),
                  request.getDietRest(),
                  "sent",
                  checkEmployee(employees.getValue()),
                  request.getDestination(),
                  request.getAddNotes(),
                  request.getDate(),
                  request.getTime()));

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

  public void clearRequest(ActionEvent actionEvent) {
    veganCheckbox.setSelected(false);
    vegCheckbox.setSelected(false);
    glutenCheckbox.setSelected(false);
    kosherCheckbox.setSelected(false);
    halalCheckbox.setSelected(false);
    lactoseCheckbox.setSelected(false);
    nutsCheckbox.setSelected(false);
    shellfishCheckbox.setSelected(false);
    notesMeal.clear();
    locations.setValue("");
    patients.setValue("");
    employees.setValue("");
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
