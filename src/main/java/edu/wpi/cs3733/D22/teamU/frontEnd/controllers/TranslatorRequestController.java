package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Equipment.Equipment;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.EquipRequest.EquipRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.TranslatorRequest.TranslatorRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects.ComboBoxAutoComplete;
import edu.wpi.cs3733.D22.teamU.frontEnd.services.equipmentDelivery.EquipmentUI;
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

public class TranslatorRequestController extends ServiceController {

  public ComboBox<String> locations;
  public ComboBox<String> employees;

  @FXML TableColumn<TranslatorRequest, String> nameID;
  @FXML TableColumn<TranslatorRequest, String> patientName;
  @FXML TableColumn<TranslatorRequest, String> toLang;
  @FXML TableColumn<TranslatorRequest, String> status;
  @FXML TableColumn<TranslatorRequest, String> employeeName; // really its the ID, our employee's don't have names
  @FXML TableColumn<TranslatorRequest, String> destination;
  @FXML TableColumn<TranslatorRequest, String> date;
  @FXML TableColumn<TranslatorRequest, String> time;
  @FXML TableView<TranslatorRequest> table;

  @FXML VBox requestHolder;
  @FXML Text requestText;
  @FXML Button clearButton;
  @FXML Button submitButton;

  @FXML TableColumn<TranslatorRequestUI, String> activeReqID;
  @FXML TableColumn<TranslatorRequest, String> activeReqPatientName;
  @FXML TableColumn<TranslatorRequest, Integer> activeReqToLang;
  @FXML TableColumn<TranslatorRequest, String> activeReqStatus;
  @FXML TableColumn<TranslatorRequest, String> activeReqEmployee;
  @FXML TableColumn<TranslatorRequest, String> activeReqDestination;
  @FXML TableColumn<TranslatorRequest, String> activeDate;
  @FXML TableColumn<TranslatorRequest, String> activeTime;

  @FXML TableView<TranslatorRequest> activeRequestTable;

  @FXML VBox inputFields;

  @FXML StackPane requestsStack;
  @FXML Pane newRequestPane;
  @FXML Pane allEquipPane;
  @FXML Pane activeRequestPane;

  @FXML Button newReqButton;
  @FXML Button activeReqButton;
  @FXML Button allEquipButton;
  @FXML Text time;
  @FXML TextArea inputField;


  ObservableList<TranslatorRequest> translatorUI = FXCollections.observableArrayList();
  ObservableList<JFXCheckBox> checkBoxes = FXCollections.observableArrayList();
  ObservableList<JFXTextArea> checkBoxesInput = FXCollections.observableArrayList();
  ObservableList<TranslatorRequest> translatorUIRequests = FXCollections.observableArrayList();
  // Udb udb;
  ArrayList<String> nodeIDs;
  ArrayList<String> staff;
  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // super.initialize(location, resources);
    // udb = Udb.getInstance();
    setUpAllTranslatorReq();
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
                checkBoxes.stream().map(JFXCheckBox::selectedProperty).toArray(Observable[]::new)));

    // BooleanBinding submit =locations.idProperty().isEmpty().and(
    // Bindings.createBooleanBinding(checkBoxes.stream().noneMatch(JFXCheckBox::isSelected)));
    submitButton
        .disableProperty()
        .bind(
            Bindings.createBooleanBinding(
                () -> checkBoxes.stream().noneMatch(JFXCheckBox::isSelected),
                checkBoxes.stream().map(JFXCheckBox::selectedProperty).toArray(Observable[]::new)));
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

  private void setUpAllTranslatorReq() throws SQLException, IOException {
    nameID.setCellValueFactory(new PropertyValueFactory<TranslatorRequest, String>("ID"));
    patientName.setCellValueFactory(new PropertyValueFactory<TranslatorRequest, String>("patientName"));
    toLang.setCellValueFactory(new PropertyValueFactory<TranslatorRequest, String>("toLang"));
    status.setCellValueFactory(new PropertyValueFactory<TranslatorRequest, String>("status"));
    //employeeName.setCellValueFactory(new PropertyValueFactory<TranslatorRequest, String>("employee"));
    destination.setCellValueFactory(new PropertyValueFactory<TranslatorRequest, String>("destination"));
    date.setCellValueFactory(new PropertyValueFactory<TranslatorRequest, String>("date"));
    time.setCellValueFactory(new PropertyValueFactory<TranslatorRequest, String>("time"));
    table.setItems(getTranslatorList());
  }

  private void setUpActiveRequests() throws SQLException, IOException {
    activeReqID.setCellValueFactory(new PropertyValueFactory<>("id"));
    activeReqName.setCellValueFactory(new PropertyValueFactory<>("equipmentName"));
    activeReqAmount.setCellValueFactory(new PropertyValueFactory<>("requestAmount"));
    activeReqType.setCellValueFactory(new PropertyValueFactory<>("type"));
    activeReqDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));
    activeDate.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
    activeTime.setCellValueFactory(new PropertyValueFactory<>("requestTime"));
    activePriority.setCellValueFactory(new PropertyValueFactory<>("priority"));
    activeRequestTable.setItems(getActiveRequestList());
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
    translatorUIRequests.add(new TranslatorRequest(id, patientName, toLang, status, employee, destination, date, time));
    return translatorUIRequests;
  }

  private ObservableList<TranslatorRequest> getTranslatorList() throws SQLException, IOException {
    translatorUI.clear();
    for (TranslatorRequest request : Udb.getInstance().translatorRequestImpl.List.values()) {
      translatorUI.add(
              new TranslatorRequest(
                      request.getID(),
                      request.getPatientName(),
                      request.getToLang(),
                      request.getStatus(),
                      request.getEmployee(),
                      request.getDestination(),
                      request.getDate(),
                      request.getTime()));
    }
    return translatorUI;
  }

  private ObservableList<TranslatorRequest> getActiveRequestList() throws SQLException, IOException {
    for (TranslatorRequest request : Udb.getInstance().translatorRequestImpl.List.values()) {
      translatorUIRequests.add(
              (
                      new TranslatorRequest(
                              request.getID(),
                              request.getPatientName(),
                              request.getToLang(),
                              request.getStatus(),
                              request.getEmployee(),
                              request.getDestination(),
                              request.getDate(),
                              request.getTime()));
    }
    return translatorUIRequests;
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

        EquipmentUI request =
            new EquipmentUI(
                (int) rand + "",
                checkBoxes.get(i).getText(),
                requestAmount,
                room,
                sdf3.format(timestamp).substring(0, 10),
                sdf3.format(timestamp).substring(11),
                1);

        activeRequestTable.setItems(
            newRequest(
                request.getId(),
                request.getEquipmentName(),
                request.getRequestAmount(),
                request.getDestination(),
                request.getRequestDate(),
                request.getRequestTime(),
                1));
        try {
          Udb.getInstance()
              .add( // TODO Have random ID and enter Room Destination
                  new EquipRequest(
                      request.getId(),
                      request.getEquipmentName(),
                      request.getRequestAmount(),
                      request.getType(),
                      checkEmployee(employees.getValue().toString()),
                      request.getDestination(),
                      request.getRequestDate(),
                      request.getRequestTime(),
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
    allEquipButton.setUnderline(false);
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
    allEquipButton.setUnderline(false);
  }

  public void switchToEquipment(ActionEvent actionEvent) {
    ObservableList<Node> stackNodes = requestsStack.getChildren();
    Node active = stackNodes.get(stackNodes.indexOf(allEquipPane));
    for (Node node : stackNodes) {
      node.setVisible(false);
    }
    active.setVisible(true);
    active.toBack();
    activeReqButton.setUnderline(false);
    newReqButton.setUnderline(false);
    allEquipButton.setUnderline(true);
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
