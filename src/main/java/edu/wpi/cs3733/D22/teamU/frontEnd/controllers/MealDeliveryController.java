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
import javafx.fxml.FXMLLoader;
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

  public ComboBox<Location> locations;
  public ComboBox<Employee> employees;
  public TextField patients;

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

  ObservableList<String> StatusList = FXCollections.observableArrayList("Processing", "Done");

  @FXML TextArea notesMeal;
  @FXML StackPane stack;
  @FXML AnchorPane masterPane;
  ArrayList<String> nodeIDs;
  ArrayList<String> staff;
  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  // =========declare buttons, popup pane and controller===========
  RequestEditController newCon;
  AnchorPane EditRequestPopUp;
  @FXML Button editButton;
  @FXML Button closeButton;
  @FXML Button submitEditButton;
  @FXML Button removeButton;
  // ================================================

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setUpActiveRequests();
    locations.setTooltip(new Tooltip());
    locations.getItems().addAll(Udb.getInstance().locationImpl.list());
    new ComboBoxAutoComplete<Location>(locations, 650, 290);

    for (Node checkBox : requestHolder.getChildren()) {
      checkBoxes.add((JFXCheckBox) checkBox);
    }

    employees.setTooltip(new Tooltip());
    employees.getItems().addAll(Udb.getInstance().EmployeeImpl.List.values());

    new ComboBoxAutoComplete<Employee>(employees, 675, 380);
    handleTime();

    // =============initialize fxml and controller=============================
    EditRequestPopUp = new AnchorPane();
    try {
      FXMLLoader loader =
          new FXMLLoader(
              getClass().getResource("/edu/wpi/cs3733/D22/teamU/views/EditRequestPopUp.fxml"));
      EditRequestPopUp = loader.load();
      newCon = (RequestEditController) loader.getController();

      EditRequestPopUp.setLayoutX(100);
      EditRequestPopUp.setLayoutY(200);

    } catch (IOException e) {
      e.printStackTrace();
    }
    // =====================================================================

    // ==============initialize edit stuff visibility ================
    editButton.setVisible(false);
    removeButton.setVisible(false);
    closeButton.setVisible(false);
    submitEditButton.setVisible(false);
    // =========================================

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
    activeReqDestination.setCellValueFactory(new PropertyValueFactory<>("location"));
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
    MealRequest r =
        new MealRequest(
            ID, patientName, dietRest, status, employee, destination, addNotes, date, time);
    r.gettingTheLocation();
    meals.add(r);
    return meals;
  }

  private ObservableList<MealRequest> getActiveRequestList() throws SQLException, IOException {
    for (MealRequest aMeal : Udb.getInstance().mealRequestImpl.hList().values()) {
      MealRequest r =
          new MealRequest(
              aMeal.getID(),
              aMeal.getPatientName(),
              aMeal.getDietRest(),
              aMeal.getStatus(),
              aMeal.getEmployee(),
              aMeal.getDestination(),
              aMeal.getAddNotes(),
              aMeal.getDate(),
              aMeal.getTime());
      r.gettingTheLocation();
      meals.add(r);
    }
    return meals;
  }

  @Override
  public void addRequest() {
    int requestAmount = 0;
    String inputString = "";
    for (int i = 0; i < checkBoxes.size(); i++) {
      if (checkBoxes.get(i).isSelected()) {
        inputString += checkBoxes.get(i).getText().trim() + ":";
      }
    }
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    // Employee empty = new Employee(employees.getValue());

    boolean alreadyHere = true;
    String serviceID = "notWork";

    // makes the id
    while (alreadyHere) {
      double rand = Math.random() * 10000;

      try {
        alreadyHere = Udb.getInstance().compServRequestImpl.hList().containsKey("MEA" + (int) rand);
      } catch (Exception e) {
        System.out.println("alreadyHere variable messed up in meal service request controller");
      }

      serviceID = "MEA" + (int) rand;
    }

    MealRequest request =
        new MealRequest(
            serviceID,
            patients.getText().trim(),
            inputString,
            "In Progress",
            employees.getValue(),
            locations.getValue().getNodeID(),
            notesMeal.getText(),
            sdf3.format(timestamp).substring(0, 10),
            sdf3.format(timestamp).substring(11));
    request.gettingTheLocation();

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
      MealRequest r =
          new MealRequest(
              request.getID(),
              request.getPatientName(),
              request.getDietRest(),
              "In Progress",
              request.employee,
              request.getDestination(),
              request.getAddNotes(),
              request.getDate(),
              request.getTime());
      r.gettingTheLocation();
      Udb.getInstance().add(r);

    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // ======remove edit request=============
  @Override
  public void removeRequest() {
    // ---CHANGE---
    MealRequest request = activeRequestTable.getSelectionModel().getSelectedItem();
    meals.remove(request);
    // -----------
    try {
      Udb.getInstance().remove(request);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    closeEdit();
  }
  // ====================================
  // =============Update the request from edit======================
  @Override
  public void updateRequest() {
    // -----change------------
    MealRequest oldRequest = activeRequestTable.getSelectionModel().getSelectedItem();
    newCon.updateRequest();
    MealRequest request = (MealRequest) newCon.getRequest();
    request.gettingTheLocation();
    meals.remove(oldRequest);
    meals.add(request);
    activeRequestTable.setItems(meals);
    // ----------------------------------------------
    try {
      Udb.getInstance().remove(oldRequest);
      Udb.getInstance().add(request);

    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  // ====================================================

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

    // =========edit and remove buttons========
    editButton.setVisible(false);
    removeButton.setVisible(false);
    closeButton.setVisible(false);
    submitEditButton.setVisible(false);
    EditRequestPopUp.setVisible(false);
    // =====================================
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

    // =====edit and remove buttons=====
    editButton.setVisible(true);
    removeButton.setVisible(true);
    // ====================================
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
    locations.getSelectionModel().clearSelection();
    patients.setText("");
    employees.getSelectionModel().clearSelection();
  }

  public void mouseHovered(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: #E6F6F7");
  }

  public void mouseExit(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: transparent");
  }

  // ==========edit button==========================
  public void editClick(MouseEvent event) {
    if (activeRequestTable.getSelectionModel().getSelectedItem() != null) {

      submitEditButton.setVisible(true);
      closeButton.setVisible(true);
      EditRequestPopUp.setVisible(true);
      Pane pane = (Pane) editButton.getParent();
      if (!pane.getChildren().contains(EditRequestPopUp)) {
        pane.getChildren().add(EditRequestPopUp);
      }
      newCon.setUp(activeRequestTable.getSelectionModel().getSelectedItem());
    }
  }
  // ==============================================

  // =======submit edit button===========
  public void submitEdit(MouseEvent event) {
    this.updateRequest();
    closeEdit();
  }
  // =====================================

  // =====close edit pane===================
  public void closeEdit() {
    activeRequestTable.getSelectionModel().clearSelection();
    EditRequestPopUp.setVisible(false);
    submitEditButton.setVisible(false);
    closeButton.setVisible(false);
  }
  // ======================================

  // ====remove req ======
  public void editRemoveReq() {
    removeRequest();
  }
  // =========================
}
