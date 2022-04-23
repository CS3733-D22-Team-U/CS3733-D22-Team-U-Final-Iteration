package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Equipment.Equipment;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.LabRequest.LabRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects.ComboBoxAutoComplete;
import edu.wpi.cs3733.D22.teamU.frontEnd.services.equipmentDelivery.EquipmentUI;
import edu.wpi.cs3733.D22.teamU.frontEnd.services.lab.LabUI;
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
import lombok.SneakyThrows;
import org.w3c.dom.Text;

public class labRequestServices extends ServiceController {

  @FXML StackPane requestsStack;
  @FXML Pane newRequestPane;
  @FXML Pane allEquipPane;
  @FXML Pane activeRequestPane;
  @FXML Button newReqButton;
  @FXML Button activeReqButton;
  @FXML Button allEquipButton;
  public Button toHelpPage;
  public Button clear;
  public Label submission;
  public ComboBox<String> locations;

  @FXML TextArea otherField;
  @FXML TextField patientNameField;
  @FXML TextField staffMemberField;

  @FXML TableColumn<LabUI, String> activeReqID;
  @FXML TableColumn<LabUI, String> patientNameReq;
  @FXML TableColumn<LabUI, String> activeReqStaff;
  @FXML TableColumn<LabUI, String> activeReqType;
  @FXML TableColumn<LabUI, String> activeDate;
  @FXML TableColumn<LabUI, String> activeTime;
  @FXML TableColumn<LabUI, Integer> activeReqAmount;
  @FXML TableColumn<LabUI, Integer> activeReqDestination;

  @FXML TableView<LabUI> activeRequestTable;
  @FXML VBox requestHolder;
  @FXML Text requestText;
  // Testing below
  @FXML javafx.scene.text.Text time;
  @FXML VBox inputFields;
  @FXML TableView<EquipmentUI> table;
  @FXML Button clearButton;
  @FXML Button submitButton;

  ObservableList<LabUI> labUIRequests = FXCollections.observableArrayList();
  ObservableList<JFXCheckBox> checkBoxes = FXCollections.observableArrayList();
  ObservableList<JFXTextArea> checkBoxesInput = FXCollections.observableArrayList();
  ObservableList<EquipmentUI> labRequestUI = FXCollections.observableArrayList();
  ObservableList<EquipmentUI> labUIRequestss = FXCollections.observableArrayList();

  // Udb udb = DBController.udb;
  ArrayList<String> nodeIDs;
  ArrayList<String> staff;

  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    // super.initialize(location, resources);
    // udb = Udb.getInstance();
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

  private void setUpActiveRequests() throws SQLException, IOException {
    activeReqID.setCellValueFactory(new PropertyValueFactory<>("id"));
    patientNameReq.setCellValueFactory(new PropertyValueFactory<>("patientName"));
    activeReqStaff.setCellValueFactory(new PropertyValueFactory<>("staffName"));
    activeReqAmount.setCellValueFactory(new PropertyValueFactory<>("activeReqAmount"));
    activeReqType.setCellValueFactory(new PropertyValueFactory<>("labType"));
    activeDate.setCellValueFactory(new PropertyValueFactory<>("requestDate"));
    activeTime.setCellValueFactory(new PropertyValueFactory<>("requestTime"));
    activeReqDestination.setCellValueFactory(new PropertyValueFactory<>("location"));
    activeRequestTable.setItems(getActiveRequestList());
  }

  private ObservableList<LabUI> newRequest(
      String id,
      String name,
      int amount,
      String destination,
      String date,
      String time,
      int priority) {
    labUIRequestss.add(new EquipmentUI(id, name, amount, destination, date, time, priority));
    return labUIRequests;
  }

  private ObservableList<EquipmentUI> getEquipmentList() throws SQLException, IOException {
    labRequestUI.clear();
    for (Equipment equipment : Udb.getInstance().EquipmentImpl.EquipmentList) {
      labRequestUI.add(
          new EquipmentUI(
              equipment.getName(),
              equipment.getInUse(),
              equipment.getAvailable(),
              equipment.getAmount(),
              equipment.getLocationID()));
    }

    return labRequestUI;
  }

  private ObservableList<LabUI> getActiveRequestList() throws SQLException, IOException {
    for (LabRequest request : Udb.getInstance().labRequestImpl.hList().values()) {
      labUIRequests.add(
          new LabUI(
              request.getID(),
              request.getPatientName(),
              request.getEmployee().getEmployeeID(),
              request.getAmount(),
              request.getName(),
              request.getDestination(),
              request.getDate(),
              request.getTime()));
    }
    return labUIRequests;
  }

  @Override
  public void addRequest() {

    String patientInput = patientNameField.getText().trim();
    String staffInput = staffMemberField.getText().trim();
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    for (int i = 0; i < checkBoxes.size(); i++) {
      if (checkBoxes.get(i).isSelected()) {

        boolean alreadyHere = true;
        String serviceID = "notWork";

        // makes the id
        while (alreadyHere) {
          double rand = Math.random() * 10000;

          try {
            alreadyHere =
                Udb.getInstance().compServRequestImpl.hList().containsKey("LAB" + (int) rand);
          } catch (Exception e) {
            System.out.println("alreadyHere variable messed up in lab service request controller");
          }

          serviceID = "LAB" + (int) rand;
        }

        String inputString = checkBoxesInput.get(i).getText().trim();
        String room = locations.getValue().toString();
        LabUI request =
            new LabUI(
                serviceID,
                patientInput,
                staffInput,
                Integer.parseInt(inputString),
                checkBoxes.get(i).getText().trim(),
                room,
                sdf3.format(timestamp).substring(0, 10),
                sdf3.format(timestamp).substring(11));
        activeRequestTable.setItems(
            newRequest(
                request.getId(),
                request.getPatientName(),
                request.getStaffName(),
                request.getActiveReqAmount(),
                request.getLabType(),
                request.getLocation(),
                request.getRequestDate(),
                request.getRequestTime()));
        try {
          Udb.getInstance()
              .labRequestImpl
              .add(
                  new LabRequest(
                      request.getId(),
                      request.getLabType(),
                      request.getActiveReqAmount(),
                      request.getPatientName(),
                      "Sent",
                      new Employee(request.getId()),
                      request.getLocation(),
                      request.getRequestDate(),
                      request.getRequestTime()));
          //          submission.setText("Request for " + checkBoxes.get(i).getText() + "
          // successfully sent.");
        } catch (IOException e) {
          e.printStackTrace();
          //          submission.setText("Request for " + checkBoxes.get(i).getText() + " failed.");
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    clear();
  }

  private ObservableList<LabUI> newRequest(
      String id,
      String patientName,
      String staffName,
      int amount,
      String labType,
      String location,
      String date,
      String time) {
    labUIRequests.add(new LabUI(id, patientName, staffName, amount, labType, location, date, time));
    return labUIRequests;
  }

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}

  public void clear(ActionEvent actionEvent) {
    clear();
  }

  private void clear() {

    for (int i = 0; i < checkBoxes.size(); i++) {
      checkBoxes.get(i).setSelected(false);
    }
    patientNameField.setText("");
    staffMemberField.setText("");
  }

  public void mouseHovered(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: #E6F6F7");
  }

  public void mouseExit(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: transparent");
  }

  public void clearRequest() {
    for (int i = 0; i < checkBoxes.size(); i++) {
      checkBoxes.get(i).setSelected(false);
      checkBoxesInput.get(i).clear();
    }
    //    requestText.setText("Cleared Requests!");
    //    requestText.setVisible(true);
    new Thread(
            () -> {
              try {
                Thread.sleep(1500); // milliseconds
                Platform.runLater(
                    () -> {
                      //                      requestText.setVisible(false);
                    });
              } catch (InterruptedException ie) {
              }
            })
        .start();
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
}
