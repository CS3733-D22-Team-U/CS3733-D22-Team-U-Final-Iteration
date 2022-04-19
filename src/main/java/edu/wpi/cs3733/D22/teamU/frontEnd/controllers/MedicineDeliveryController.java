package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Equipment.Equipment;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MedicineRequest.MedicineRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects.ComboBoxAutoComplete;
import edu.wpi.cs3733.D22.teamU.frontEnd.services.equipmentDelivery.EquipmentUI;
import edu.wpi.cs3733.D22.teamU.frontEnd.services.medicine.medicineUI;
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
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class MedicineDeliveryController extends ServiceController {
  public ComboBox<String> locations;
  public ComboBox<String> employees;
  ArrayList<String> staff;
  @FXML VBox inputFields;
  @FXML
  StackPane requestsStack;

  @FXML
  Pane newRequestPane;

  @FXML
  Pane activeRequestPane;

  @FXML
  Pane allMedPane;

  @FXML Button activeReqButton;
  @FXML Button newReqButton;
  @FXML Button submitButton;
  @FXML Button allMedButton;
  @FXML JFXCheckBox Advil;
  @FXML JFXCheckBox Alprozalam;
  @FXML JFXCheckBox AmphetamineSalt;
  @FXML JFXCheckBox Atorvastatin;
  @FXML JFXCheckBox Lisinopril;
  @FXML JFXCheckBox Metformin;
  @FXML JFXCheckBox specialCheck;
  @FXML Button clearButton;
  @FXML TextArea specialReq;
  @FXML TextField patientName;
  @FXML TextField staffName;
  @FXML TextField advilTxt;
  // @FXML TextField IDtxt;
  @FXML TextField alproTxt;
  @FXML TextField saltTxt;
  @FXML TextField atorvTxt;
  @FXML TextField lisinTxt;
  @FXML TextField metTxt;
  @FXML TextArea specialReqTxt;
  @FXML Text reset;
  @FXML Text time;
  @FXML Text processText;
  @FXML JFXHamburger hamburger;
  @FXML VBox medVbox;
  @FXML VBox nameVbox;
  @FXML VBox vBoxPane;
  @FXML Pane pane;
  @FXML Pane assistPane;
  @FXML AnchorPane bigPane;
  @FXML TabPane tab;
  @FXML TextField destination;

  @FXML Text requestText;

  @FXML TableColumn<medicineUI, String> activeReqID;
  @FXML TableColumn<medicineUI, String> activeReqName;
  @FXML TableColumn<medicineUI, String> activeReqAmount;
  //@FXML TableColumn<medicineUI, String> activeReqPatient;
  @FXML TableColumn<medicineUI, String> activeReqType;
  @FXML TableColumn<medicineUI, String> activeReqDestination;
  @FXML TableColumn<medicineUI, String> activeDate;
  @FXML TableColumn<medicineUI, String> activeTime;
  @FXML TableColumn<medicineUI, String> activePriority;

  @FXML TableView<medicineUI> activeRequestTable;
  @FXML VBox requestHolder;

  ArrayList<String> nodeIDs;

  @FXML TableColumn<EquipmentUI, String> nameCol;
  @FXML TableColumn<EquipmentUI, Integer> inUse;
  @FXML TableColumn<EquipmentUI, Integer> available;
  @FXML TableColumn<EquipmentUI, Integer> total;
  @FXML TableColumn<EquipmentUI, String> location;
  @FXML TableView<EquipmentUI> table;

  ObservableList<medicineUI> medUIRequests = FXCollections.observableArrayList();
  ObservableList<JFXCheckBox> checkBoxes = FXCollections.observableArrayList();
  ObservableList<JFXTextArea> checkBoxesInput = FXCollections.observableArrayList();

  // Udb udb = DBController.udb;

  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      setUpAllMed();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    try {
      setUpActiveRequests();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    nodeIDs = new ArrayList<>();
    try {
      for (Location l : Udb.getInstance().locationImpl.list()) {
        nodeIDs.add(l.getNodeID());
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    locations.setTooltip(new Tooltip());
    locations.getItems().addAll(nodeIDs);
    new ComboBoxAutoComplete<String>(locations, 650, 290);

    staff = new ArrayList<>();
    try {
      for (Employee l : Udb.getInstance().EmployeeImpl.hList().values()) {
        staff.add(l.getEmployeeID());
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (SQLException e) {
      throw new RuntimeException(e);
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

  private void setUpAllMed() throws SQLException, IOException {
    nameCol.setCellValueFactory(new PropertyValueFactory<EquipmentUI, String>("equipmentName"));
    inUse.setCellValueFactory(new PropertyValueFactory<EquipmentUI, Integer>("amountInUse"));
    available.setCellValueFactory(
            new PropertyValueFactory<EquipmentUI, Integer>("amountAvailable"));
    total.setCellValueFactory(new PropertyValueFactory<EquipmentUI, Integer>("totalAmount"));
    location.setCellValueFactory(new PropertyValueFactory<EquipmentUI, String>("location"));
    //table.setItems(getMedList());
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

  /*
  private ObservableList<medicineUI> getMedList() throws SQLException, IOException {
    medicineUI.clear();
    for (Medicine medicine : Udb.getInstance().EquipmentImpl.EquipmentList) {
      medicineUI.add(
              new medicineUI(
                      equipment.getName(),
                      equipment.getInUse(),
                      equipment.getAvailable(),
                      equipment.getAmount(),
                      equipment.getLocationID()));
    }

    return medicineUI;
  }

   */

  private ObservableList<medicineUI> getActiveRequestList() throws SQLException, IOException {
    for (MedicineRequest request : Udb.getInstance().medicineRequestImpl.hList().values()) {
      medUIRequests.add(
          new medicineUI(
              request.getID(),
              request.getName(),
              request.getPatientName(),
              request.getDestination(),
              request.getStatus(),
              request.getEmployee(),
              request.getDate(),
              request.getTime()));
    }
    return medUIRequests;
  }

  public Employee checkEmployee(String employee) throws SQLException, IOException {
    if (Udb.getInstance().EmployeeImpl.List.get(employee) != null) {
      return Udb.getInstance().EmployeeImpl.List.get(employee);
    } else {
      Employee empty = new Employee("N/A");
      return empty;
    }
  }

  @SneakyThrows
  @Override
  public void addRequest() {
    String patientInput = patientName.getText().trim();
    String staffInput = staffName.getText().trim();
    String destinationInput = destination.getText().trim();

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    for (int i = 0; i < checkBoxes.size(); i++) {
      if (checkBoxes.get(i).isSelected()) {
        double rand = Math.random() * 10000;
        // int amount = Integer.parseInt(checkBoxInput.get(i).toString().trim());
        int amount = 24;
        medicineUI request =
            new medicineUI(
                (int) rand + "",
                checkBoxes.get(i).getText(),
                destinationInput,
                "Ordered",
                patientInput,
                checkEmployee(staffInput),
                sdf3.format(timestamp).substring(0, 10),
                sdf3.format(timestamp).substring(11),
                amount);
        activeRequestTable.setItems(
            newRequest(
                request.getId(),
                request.getName(),
                request.getPatientName(),
                request.getDestination(),
                "Ordered",
                request.getEmployee(),
                request.getDate(),
                request.getTime(),
                amount));
        try {
          Udb.getInstance()
              .medicineRequestImpl
              .add(
                  new MedicineRequest(
                      request.getId(),
                      request.getName(),
                      request.getPatientName(),
                      request.getStatus(),
                      request.getEmployee(),
                      request.getDestination(),
                      request.getDate(),
                      request.getTime()));
          processText.setText("Request for " + checkBoxes.get(i).getText() + " successfully sent.");
        } catch (IOException e) {
          e.printStackTrace();
          processText.setText("Request for " + checkBoxes.get(i).getText() + " failed.");
        }
      }
    }
    clearRequest();
  }

  public void enableTxt() {
    if (Advil.isSelected()) {
      advilTxt.setDisable(false);
    }
    if (Alprozalam.isSelected()) {
      alproTxt.setDisable(false);
    }
    if (AmphetamineSalt.isSelected()) {
      saltTxt.setDisable(false);
    }
    if (Atorvastatin.isSelected()) {
      atorvTxt.setDisable(false);
    }
    if (Lisinopril.isSelected()) {
      lisinTxt.setDisable(false);
    }
    if (Metformin.isSelected()) {
      metTxt.setDisable(false);
    }
    if (specialCheck.isSelected()) {
      specialReqTxt.setDisable(false);
    }
  }

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
  //    lisinTxt.equals("") && metTxt.equals("") && specialReqTxt.equals(""))
  public void reqFields() {
    if (staffName.getText().equals("")
        || patientName.getText().equals("")
        // IDtxt.getText().equals("")
        || (advilTxt.getText().equals("")
                && alproTxt.getText().equals("")
                && saltTxt.getText().equals(""))
            && atorvTxt.getText().equals("")
            && lisinTxt.getText().equals("")
            && metTxt.getText().equals("")
            && specialReqTxt.getText().equals("")) {
      processText.setText("Please fill out all required fields!");
      processText.setVisible(true);
    } else {
      process();
    }
  }

  public void process() {
    processText.setText("Processing...");
    processText.setVisible(true);
    new Thread(
            () -> {
              try {
                Thread.sleep(2000); // milliseconds
                Platform.runLater(
                    () -> {
                      processText.setText(
                          "Staff Name: "
                              + staffName.getText()
                              + "\n"
                              + "Patient Name: "
                              + patientName.getText()
                              + "\n"
                              + "Order ID: "
                              // IDtxt.getText()
                              + "\n"
                              + ""
                              + "\n"
                              + "Medicine Order: "
                              + "\n"
                              + ""
                              + "\n"
                              + "Advil: "
                              + advilTxt.getText()
                              + "\n"
                              + "Alprozalam: "
                              + alproTxt.getText()
                              + "\n"
                              + "Amphetamine Salt: "
                              + saltTxt.getText()
                              + "\n"
                              + "Atorvastatin: "
                              + atorvTxt.getText()
                              + "\n"
                              + "Lisinopril: "
                              + lisinTxt.getText()
                              + "\n"
                              + "Metformin: "
                              + metTxt.getText()
                              + "\n"
                              + "Special Request: "
                              + request());
                    });
              } catch (InterruptedException ie) {
              }
            })
        .start();
  }

  private String request() {
    String request = "";
    if (specialReqTxt.equals("")) {
      request = "No response";
    } else {
      request = specialReqTxt.getText();
    }
    return request;
  }

  public void toMedHelp(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/medHelp.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }

  private ObservableList<medicineUI> newRequest(
      String id,
      String name,
      String patientName,
      String location,
      String status,
      Employee employee,
      String date,
      String time,
      int amount) {
    medUIRequests.add(
        new medicineUI(id, name, patientName, location, status, employee, date, time, amount));
    return medUIRequests;
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
    allMedButton.setUnderline(false);
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
    allMedButton.setUnderline(false);
  }

  public void switchToMedicine(ActionEvent actionEvent) {
    ObservableList<Node> stackNodes = requestsStack.getChildren();
    Node active = stackNodes.get(stackNodes.indexOf(allMedPane));
    for (Node node : stackNodes) {
      node.setVisible(false);
    }
    active.setVisible(true);
    active.toBack();
    activeReqButton.setUnderline(false);
    newReqButton.setUnderline(false);
    allMedButton.setUnderline(true);
  }

  public void mouseHovered(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: #E6F6F7");
  }

  public void mouseExit(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: transparent");
  }

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}
}
