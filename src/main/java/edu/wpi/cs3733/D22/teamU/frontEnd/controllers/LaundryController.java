package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXCheckBox;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.LaundryRequest.LaundryRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects.ComboBoxAutoComplete;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
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

public class LaundryController extends ServiceController {
  @FXML VBox requestHolder;
  @FXML Pane activeRequestPane;
  @FXML StackPane requestsStack;
  @FXML Pane newRequestPane;
  @FXML Button newReqButton;
  @FXML Button activeReqButton;
  @FXML ComboBox<String> locations;
  @FXML ComboBox<String> employees;
  @FXML Button submitButton;
  @FXML Button clearButton;
  @FXML Text time;
  @FXML DatePicker pickupDateInput;
  @FXML DatePicker dropOffDateInput;

  @FXML TableColumn<LaundryRequest, String> activeReqID;
  @FXML TableColumn<LaundryRequest, String> patientName;
  @FXML TableColumn<LaundryRequest, String> staffName;
  @FXML TableColumn<LaundryRequest, String> serviceType;
  @FXML TableColumn<LaundryRequest, String> location;
  @FXML TableColumn<LaundryRequest, String> pickUp;
  @FXML TableColumn<LaundryRequest, String> dropOff;

  @FXML TableView<LaundryRequest> activeRequestTable;

  ObservableList<LaundryRequest> laundryRequests = FXCollections.observableArrayList();
  ObservableList<JFXCheckBox> checkBoxes = FXCollections.observableArrayList();
  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  ArrayList<String> nodeIDs;
  ArrayList<String> staff;

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    setUpActiveRequests();
    for (Node checkBox : requestHolder.getChildren()) {
      checkBoxes.add((JFXCheckBox) checkBox);
    }

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

    clearButton
        .disableProperty()
        .bind(
            Bindings.createBooleanBinding(
                () -> checkBoxes.stream().noneMatch(JFXCheckBox::isSelected),
                checkBoxes.stream().map(JFXCheckBox::selectedProperty).toArray(Observable[]::new)));
    submitButton
        .disableProperty()
        .bind(
            Bindings.createBooleanBinding(
                () -> checkBoxes.stream().noneMatch(JFXCheckBox::isSelected),
                checkBoxes.stream().map(JFXCheckBox::selectedProperty).toArray(Observable[]::new)));
    handleTime();

    pickupDateInput.setDayCellFactory(
        picker ->
            new DateCell() {
              public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0);
              }
            });

    dropOffDateInput.setDayCellFactory(
        picker ->
            new DateCell() {
              public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0);
              }
            });
  }

  private void setUpActiveRequests() throws SQLException, IOException {
    activeReqID.setCellValueFactory(new PropertyValueFactory<>("ID"));
    patientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
    staffName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
    serviceType.setCellValueFactory(new PropertyValueFactory<>("services"));
    location.setCellValueFactory(new PropertyValueFactory<>("destination"));
    pickUp.setCellValueFactory(new PropertyValueFactory<>("pickUpDate"));
    dropOff.setCellValueFactory(new PropertyValueFactory<>("dropOffDate"));

    activeRequestTable.setItems(getActiveRequestList());
  }

  @SneakyThrows
  private ObservableList<LaundryRequest> getActiveRequestList() {
    laundryRequests.addAll(Udb.getInstance().laundryRequestImpl.hList().values());
    return laundryRequests;
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

  @Override
  public void addRequest() {}

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

  public void mouseHovered(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: #E6F6F7");
  }

  public void mouseExit(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: transparent");
  }

  public void clearRequest(ActionEvent actionEvent) {}
}
