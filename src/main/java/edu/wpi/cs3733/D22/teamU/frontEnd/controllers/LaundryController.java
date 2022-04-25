package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXCheckBox;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
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
import javafx.application.Platform;
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
  @FXML Pane editRequestPane;
  @FXML Button newReqButton;
  @FXML Button activeReqButton;
  @FXML Button editButton;
  @FXML ComboBox<Location> locations;
  @FXML ComboBox<Employee> employees;
  @FXML Button submitButton;
  @FXML Button clearButton;
  @FXML Text time;
  @FXML DatePicker pickupDateInput;
  @FXML DatePicker dropOffDateInput;
  @FXML Text requestText;
  @FXML TableColumn<LaundryRequest, String> activeReqID;
  @FXML TableColumn<LaundryRequest, String> patientName;
  @FXML TableColumn<LaundryRequest, String> staffName;
  @FXML TableColumn<LaundryRequest, String> serviceType;
  @FXML TableColumn<LaundryRequest, String> location;
  @FXML TableColumn<LaundryRequest, String> pickUp;
  @FXML TableColumn<LaundryRequest, String> dropOff;
  @FXML TableView<LaundryRequest> activeRequestTable;

  // edit
  @FXML TableView<LaundryRequest> editTable;
  @FXML TableColumn<LaundryRequest, String> ETabID;
  @FXML TableColumn<LaundryRequest, String> ETabRoom;
  @FXML TableColumn<LaundryRequest, String> ETabPatient;
  @FXML TableColumn<LaundryRequest, String> ETabStat;

  @FXML TextField editID;
  @FXML TextField editPatient;
  @FXML ComboBox<Location> editDest;
  @FXML ComboBox<Employee> editEmployee;
  @FXML DatePicker editPick;
  @FXML DatePicker editDrop;
  @FXML TextField editStat;
  @FXML TextField editService;
  @FXML TextArea editNotes;

  @FXML Button submitEdit;
  @FXML Button removeButton;

  @FXML TextField patientNameInput;
  ObservableList<LaundryRequest> laundryRequests = FXCollections.observableArrayList();
  ObservableList<JFXCheckBox> checkBoxes = FXCollections.observableArrayList();
  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  ArrayList<String> nodeIDs;
  ArrayList<String> staff;

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    if (Udb.admin) {
      editButton.setVisible(true);
    } else {
      editButton.setVisible(false);
    }

    setUpActiveRequests();
    setUpEditRequests();
    for (Node checkBox : requestHolder.getChildren()) {
      checkBoxes.add((JFXCheckBox) checkBox);
    }

    locations.setTooltip(new Tooltip());
    locations.getItems().addAll(Udb.getInstance().locationImpl.locations);
    new ComboBoxAutoComplete<Location>(locations, 650, 290);
    editDest.setTooltip(new Tooltip());
    editDest.getItems().addAll(Udb.getInstance().locationImpl.locations);
    new ComboBoxAutoComplete<Location>(editDest, 650, 290);

    employees.setTooltip(new Tooltip());
    employees.getItems().addAll(Udb.getInstance().EmployeeImpl.hList().values());
    new ComboBoxAutoComplete<Employee>(employees, 675, 380);
    editEmployee.setTooltip(new Tooltip());
    editEmployee.getItems().addAll(Udb.getInstance().EmployeeImpl.hList().values());
    new ComboBoxAutoComplete<Employee>(editEmployee, 675, 380);

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
    staffName.setCellValueFactory(new PropertyValueFactory<>("employee"));
    serviceType.setCellValueFactory(new PropertyValueFactory<>("services"));
    location.setCellValueFactory(new PropertyValueFactory<>("location"));
    pickUp.setCellValueFactory(new PropertyValueFactory<>("pickUpDate"));
    dropOff.setCellValueFactory(new PropertyValueFactory<>("dropOffDate"));

    activeRequestTable.setItems(getActiveRequestList());
  }

  private void setUpEditRequests() throws SQLException, IOException {
    ETabID.setCellValueFactory(new PropertyValueFactory<>("ID"));
    ETabPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
    ETabRoom.setCellValueFactory(new PropertyValueFactory<>("location"));
    ETabStat.setCellValueFactory(new PropertyValueFactory<>("status"));

    editTable.setItems(getActiveRequestList());
  }

  @SneakyThrows
  private ObservableList<LaundryRequest> getActiveRequestList() {
    for (LaundryRequest e : Udb.getInstance().laundryRequestImpl.hList().values()) {
      e.gettingTheLocation();
    }
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
  public void addRequest() {
    StringBuilder startRequestString = new StringBuilder("Your request for : ");

    String endRequest = " has been placed successfully";
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    for (int i = 0; i < checkBoxes.size(); i++) {
      if (checkBoxes.get(i).isSelected()) {
        String inputString = "";

        String room = locations.getValue().toString();

        startRequestString
            .append(" ")
            .append(checkBoxes.get(i).getText())
            .append("(s) to room ")
            .append(room)
            .append(", ");

        double rand = Math.random() * 10000;

        LaundryRequest request =
            new LaundryRequest(
                (int) rand + "",
                patientNameInput.getText().trim(),
                employees.getValue(),
                "done",
                locations.getValue().getNodeID(),
                pickupDateInput.getValue().toString(),
                dropOffDateInput.getValue().toString(),
                sdf3.format(timestamp).substring(11),
                checkBoxes.get(i).getText().trim(),
                "N/A");
        request.gettingTheLocation();
        laundryRequests.add(request);
        activeRequestTable.setItems(laundryRequests);
        try {
          Location l =
              Udb.getInstance()
                  .locationImpl
                  .list()
                  .get(Udb.getInstance().locationImpl.search(request.getDestination()));
          LaundryRequest e =
              new LaundryRequest(
                  request.getID(),
                  request.getPatientName(),
                  request.getEmployee(),
                  request.getStatus(),
                  request.getDestination(),
                  request.getPickUpDate(),
                  request.getDropOffDate(),
                  request.getTime(),
                  request.getServices(),
                  request.getNotes());
          e.setLocation(l);
          Udb.getInstance().add(e);

        } catch (IOException e) {
          e.printStackTrace();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
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

  // TODO bring to UI
  // removes request from database
  @Override
  public void removeRequest() {
    LaundryRequest request = editTable.getSelectionModel().getSelectedItem();
    laundryRequests.remove(request);
    try {
      Udb.getInstance().remove(request);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    clearUpdate();
  }

  // TODO bring to UI
  // updates the request
  @Override
  public void updateRequest() {
    LaundryRequest oldRequest = editTable.getSelectionModel().getSelectedItem();
    String ID = oldRequest.getID();
    String room = editDest.getValue().getNodeID();
    String patient = editPatient.getText();
    Employee employee = editEmployee.getValue();
    String pickUp = editPick.getValue().toString();
    String dropOff = editDrop.getValue().toString();
    String status = editStat.getText();
    String service = editService.getText();
    String time = oldRequest.getTime();
    String notes = editNotes.getText();

    LaundryRequest request =
        new LaundryRequest(
            ID, patient, employee, status, room, pickUp, dropOff, time, service, notes);
    request.gettingTheLocation();
    laundryRequests.remove(oldRequest);
    laundryRequests.add(request);
    activeRequestTable.setItems(laundryRequests);
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

  // TODO Bring to Ui
  // Set fields to edit or remove
  public void updateFields() {
    LaundryRequest temp = editTable.getSelectionModel().getSelectedItem();
    LocalDate pick = LocalDate.parse(temp.getPickUpDate());
    LocalDate drop = LocalDate.parse(temp.getDropOffDate());
    editID.setText(temp.getID());
    editDest.setValue(temp.getLocation());
    editPatient.setText(temp.getPatientName());
    editEmployee.setValue(temp.getEmployee());
    editPick.setValue(pick);
    editDrop.setValue(drop);
    editStat.setText(temp.getStatus());
    editService.setText(temp.getServices());
    editNotes.setText(temp.getNotes());
  }

  // TODO  bring to ui
  // clears the text fields from the edit
  public void clearUpdate() {
    editID.setText("");
    editDest.setValue(null);
    editPatient.setText("");
    editEmployee.setValue(null);
    editPick.setValue(null);
    editDrop.setValue(null);
    editStat.setText("");
    editService.setText("");
    editNotes.setText("");
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
    editButton.setUnderline(false);
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
    editButton.setUnderline(false);
  }

  public void switchToEditRequest(ActionEvent actionEvent) {
    ObservableList<Node> stackNodes = requestsStack.getChildren();
    Node active = stackNodes.get(stackNodes.indexOf(editRequestPane));
    for (Node node : stackNodes) {
      node.setVisible(false);
    }
    active.setVisible(true);
    active.toBack();
    activeReqButton.setUnderline(false);
    newReqButton.setUnderline(false);
    editButton.setUnderline(true);
  }

  public void mouseHovered(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: #E6F6F7");
  }

  public void mouseExit(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: transparent");
  }

  public Employee checkEmployee(String employee) throws NullPointerException {
    if (EmployeeDaoImpl.List.get(employee) != null) {
      return EmployeeDaoImpl.List.get(employee);
    } else {
      Employee empty = new Employee("N/A");
      return empty;
    }
  }

  public void clearRequest(ActionEvent actionEvent) {}
}
