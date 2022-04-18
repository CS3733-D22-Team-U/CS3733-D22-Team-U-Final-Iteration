package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Equipment.Equipment;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.EquipRequest.EquipRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects.ComboBoxAutoComplete;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.SneakyThrows;

public class EquipmentDeliverySystemController extends ServiceController {

  public ComboBox<String> locations;
  public ComboBox employees;
  @FXML TabPane tabPane;
  @FXML TableColumn<Equipment, String> nameCol;
  @FXML TableColumn<Equipment, Integer> inUse;
  @FXML TableColumn<Equipment, Integer> available;
  @FXML TableColumn<Equipment, String> total;
  @FXML TableColumn<Equipment, String> location;
  @FXML TableView<Equipment> table;
  @FXML VBox requestHolder;
  @FXML Text requestText;
  @FXML Button clearButton;
  @FXML Button submitButton;
  @FXML TableColumn<EquipRequest, String> activeReqID;
  @FXML TableColumn<EquipRequest, String> activeReqName;
  @FXML TableColumn<EquipRequest, Integer> activeReqAmount;
  @FXML TableColumn<EquipRequest, String> activeReqType;
  @FXML TableColumn<EquipRequest, String> activeReqDestination;
  @FXML TableColumn<EquipRequest, String> activeDate;
  @FXML TableColumn<EquipRequest, String> activeTime;
  @FXML TableColumn<EquipRequest, Integer> activePriority;

  @FXML TableView<EquipRequest> activeRequestTable;
  @FXML VBox inputFields;
  @FXML VBox locationInput;

  ObservableList<Equipment> equipmentUI = FXCollections.observableArrayList();
  ObservableList<JFXCheckBox> checkBoxes = FXCollections.observableArrayList();
  ObservableList<JFXTextArea> checkBoxesInput = FXCollections.observableArrayList();
  ObservableList<JFXTextArea> locInput = FXCollections.observableArrayList();

  ObservableList<EquipRequest> equipmentUIRequests = FXCollections.observableArrayList();
  // Udb udb;
  ArrayList<String> nodeIDs;
  ArrayList<String> staff;
  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    // udb = Udb.getInstance();
    setUpAllEquipment();
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
    employees.setTooltip(new Tooltip());
    employees.getItems().addAll(staff);
    new ComboBoxAutoComplete<String>(employees, 675, 380);

    for (Node checkBox : requestHolder.getChildren()) {
      checkBoxes.add((JFXCheckBox) checkBox);
    }
    for (Node textArea : inputFields.getChildren()) {
      checkBoxesInput.add((JFXTextArea) textArea);
    }
    for (int i = 0; i < checkBoxesInput.size(); i++) {
      int finalI = i;
      checkBoxesInput
          .get(i)
          .disableProperty()
          .bind(
              Bindings.createBooleanBinding(
                  () -> !checkBoxes.get(finalI).isSelected(),
                  checkBoxes.stream().map(CheckBox::selectedProperty).toArray(Observable[]::new)));
    }
    clearButton
        .disableProperty()
        .bind(
            Bindings.createBooleanBinding(
                () -> checkBoxes.stream().noneMatch(JFXCheckBox::isSelected),
                checkBoxes.stream().map(CheckBox::selectedProperty).toArray(Observable[]::new)));

    submitButton
        .disableProperty()
        .bind(
            Bindings.createBooleanBinding(
                () -> checkBoxes.stream().noneMatch(JFXCheckBox::isSelected),
                checkBoxes.stream().map(CheckBox::selectedProperty).toArray(Observable[]::new)));
  }

  private void setUpAllEquipment() throws SQLException, IOException {
    nameCol.setCellValueFactory(new PropertyValueFactory<Equipment, String>("Name"));
    inUse.setCellValueFactory(new PropertyValueFactory<Equipment, Integer>("InUse"));
    available.setCellValueFactory(new PropertyValueFactory<Equipment, Integer>("Available"));
    total.setCellValueFactory(new PropertyValueFactory<Equipment, String>("Amount"));
    location.setCellValueFactory(new PropertyValueFactory<Equipment, String>("locationID"));

    table.setItems(getEquipmentList());
  }

  private void setUpActiveRequests() throws SQLException, IOException {
    activeReqID.setCellValueFactory(new PropertyValueFactory<>("ID"));
    activeReqName.setCellValueFactory(new PropertyValueFactory<>("name"));
    activeReqAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
    activeReqType.setText("Status");
    activeReqType.setCellValueFactory(new PropertyValueFactory<>("status"));
    activeReqDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));
    activeDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    activeTime.setCellValueFactory(new PropertyValueFactory<>("time"));
    activePriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
    activeRequestTable.setItems(getActiveRequestList());
  }

  private ObservableList<EquipRequest> newRequest(
      String id,
      String name,
      int amount,
      String typeOfRequest,
      String status,
      String employee,
      String destination,
      String date,
      String time,
      int priority) {
    equipmentUIRequests.add(
        new EquipRequest(
            id,
            name,
            amount,
            typeOfRequest,
            status,
            checkEmployee(employee),
            destination,
            date,
            time,
            priority));
    return equipmentUIRequests;
  }

  private ObservableList<Equipment> getEquipmentList() throws SQLException, IOException {
    equipmentUI.clear();
    for (Equipment equipment : Udb.getInstance().EquipmentImpl.EquipmentList) {
      equipmentUI.add(
          new Equipment(
              equipment.getName(),
              equipment.getAmount(),
              equipment.getInUse(),
              equipment.getLocationID()));
    }
    return equipmentUI;
  }

  private ObservableList<EquipRequest> getActiveRequestList() throws SQLException, IOException {
    for (EquipRequest equipRequest : Udb.getInstance().equipRequestImpl.hList().values()) {
      equipmentUIRequests.add(
          new EquipRequest(
              equipRequest.getID(),
              equipRequest.getName(),
              equipRequest.getAmount(),
              equipRequest.getType(),
              equipRequest.getStatus(),
              equipRequest.getEmployee(),
              equipRequest.getDestination(),
              equipRequest.getDate(),
              equipRequest.getTime(),
              equipRequest.getPriority()));
    }
    return equipmentUIRequests;
  }

  @Override
  public void addRequest() {
    StringBuilder startRequestString = new StringBuilder("Your request for : ");

    String endRequest = " has been placed successfully";
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    int requestAmount = 0;
    for (int i = 0; i < checkBoxes.size(); i++) {
      if (checkBoxes.get(i).isSelected()) {
        String inputString = "";

        if (checkBoxesInput.get(i).getText().trim().equals("")) {
          inputString = "0";
        } else {
          inputString = checkBoxesInput.get(i).getText().trim();
        }
        String room = locations.getValue().toString();

        requestAmount = Integer.parseInt(inputString);

        startRequestString
            .append(requestAmount)
            .append(" ")
            .append(checkBoxes.get(i).getText())
            .append("(s) to room ")
            .append(room)
            .append(", ");

        double rand = Math.random() * 10000;

        EquipRequest request =
            new EquipRequest(
                (int) rand + "",
                "equipment",
                requestAmount,
                checkBoxes.get(i).getText(),
                "in progress",
                new Employee("n/a"),
                room,
                sdf3.format(timestamp).substring(0, 10),
                sdf3.format(timestamp).substring(11),
                1);

        activeRequestTable.setItems(
            newRequest(
                request.getID(),
                request.getName(),
                request.getAmount(),
                request.getType(),
                request.getStatus(),
                request.getEmployee().getEmployeeID(),
                request.getDestination(),
                request.getDate(),
                request.getTime(),
                request.getPriority()));
        try {
          Udb.getInstance()
              .add( // TODO Have random ID and enter Room Destination
                  new EquipRequest(
                      request.getID(),
                      request.getName(),
                      request.getAmount(),
                      request.getType(),
                      request.getStatus(),
                      checkEmployee(employees.getValue().toString()),
                      request.getDestination(),
                      request.getDate(),
                      request.getTime(),
                      1));

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

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}

  public void clearRequest() {
    for (int i = 0; i < checkBoxes.size(); i++) {
      checkBoxes.get(i).setSelected(false);
      checkBoxesInput.get(i).clear();
    }
    requestText.setText("Cleared Requests!");
    requestText.setVisible(true);
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
}
