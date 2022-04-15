package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import javafx.scene.control.ComboBox;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MedicineRequest.MedicineRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.DBController;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects.ComboBoxAutoComplete;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.SneakyThrows;


public class MedicineDeliveryController extends ServiceController {

  public ComboBox<String> locations;
  public ComboBox employees;
  public ComboBox<String> patient;
  @FXML Button submitButton;
  @FXML Button clearButton;


  @FXML JFXCheckBox Advil;
  @FXML JFXCheckBox Alprozalam;
  @FXML JFXCheckBox AmphetamineSalt;
  @FXML JFXCheckBox Atorvastatin;
  @FXML JFXCheckBox Lisinopril;
  @FXML JFXCheckBox Metformin;
  @FXML JFXCheckBox specialCheck;

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

  @FXML TableColumn<medicineUI, String> reqID;
  @FXML TableColumn<medicineUI, String> reqPatient;
  @FXML TableColumn<medicineUI, String> reqStaff;
  @FXML TableColumn<medicineUI, String> reqMed;
  @FXML TableColumn<medicineUI, String> reqStatus;
  @FXML TableColumn<medicineUI, String> reqAmount;
  @FXML TableColumn<medicineUI, String> reqLocation;
  @FXML TableColumn<medicineUI, String> reqDate;
  @FXML TableColumn<medicineUI, String> reqTime;

  @FXML TableView<medicineUI> activeRequestTable;
  @FXML VBox requestHolder;

  ObservableList<medicineUI> medUIRequests = FXCollections.observableArrayList();
  ObservableList<JFXCheckBox> checkBoxes = FXCollections.observableArrayList();
  ObservableList<TextField> checkBoxInput = FXCollections.observableArrayList();
  ObservableList<JFXTextArea> checkBoxesInput = FXCollections.observableArrayList();
  ObservableList<JFXTextArea> locInput = FXCollections.observableArrayList();

  Udb udb = DBController.udb;
  ArrayList<String> nodeIDs;
  ArrayList<String> staff;

  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    setUpActiveRequests();
    nodeIDs = new ArrayList<>();
    for(Location l: udb.locationImpl.list()){
      nodeIDs.add(l.getNodeID());
    }
    patient.setTooltip(new Tooltip());
    patient.getItems().addAll(
            "Harsh",
            "Marko",
            "Tim",
            "Will",
            "Deepti",
            "Joselin",
            "Nick",
            "Kody",
            "Mike",
            "Belisha",
            "Iain",
            "Wong",
            "Big Wong",
            "MR.HARSH",
            "Patient00678569420");
    new ComboBoxAutoComplete<String>(patient, 650, 290);




    locations.setTooltip(new Tooltip());
    locations.getItems().addAll(nodeIDs);
    new ComboBoxAutoComplete<String>(locations, 650, 290);

    staff = new ArrayList<>();
    for(Employee l: udb.EmployeeImpl.hList().values()){
      staff.add(l.getEmployeeID());
    }

    employees.setTooltip(new Tooltip());
    employees.getItems().addAll(staff);
    new ComboBoxAutoComplete<String>(employees, 675, 380);

    for (Node checkbox : requestHolder.getChildren()) {
      checkBoxes.add((JFXCheckBox) checkbox);
    }

    for (Node textField : medVbox.getChildren()) {
      checkBoxInput.add((TextField) textField);
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

  private void setUpActiveRequests() {
    reqID.setCellValueFactory(new PropertyValueFactory<>("id"));
    reqMed.setCellValueFactory(new PropertyValueFactory<>("name"));
    reqPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
    reqLocation.setCellValueFactory(new PropertyValueFactory<>("destination"));
    reqStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    reqStaff.setCellValueFactory(new PropertyValueFactory<>("employee"));
    reqDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    reqTime.setCellValueFactory(new PropertyValueFactory<>("time"));

    activeRequestTable.setItems(getActiveRequestList());
  }

  private ObservableList<medicineUI> getActiveRequestList() {
    for (MedicineRequest request : udb.medicineRequestImpl.hList().values()) {
      medUIRequests.add(
          new medicineUI(
              request.getID(),
              request.getName(),
              request.getPatientName(),
              request.getDestination(),
              request.getStatus(),
              request.getEmployee(),
              request.getDate(),
              request.getTime(),
              request.getAmount()));
    }
    return medUIRequests;
  }

  public Employee checkEmployee(String employee) throws NullPointerException, IOException {
    if (EmployeeDaoImpl.List.get(employee) != null) {
      return EmployeeDaoImpl.List.get(employee);
    } else {
      Employee empty = new Employee("N/A");
      return empty;
    }
  }

  //note for self:
  //employee currently returning N/A because checkEmployee is always returning N/A
  //csv file is in wrong order

  @SneakyThrows
  @Override
  public void addRequest() {
    //String patientInput = patientName.getText().trim();
    String patient2 = patient.getValue().toString();
    String staffInput = (employees.getId().toString());
    String destinationInput = locations.getValue().toString();

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    for (int i = 0; i < checkBoxes.size(); i++) {
      if (checkBoxes.get(i).isSelected()) {
        double rand = Math.random() * 10000;
        int amount = 69420;
        medicineUI request =
            new medicineUI(
                (int) rand + "",
                checkBoxes.get(i).getText(),


                    patient2,
                destinationInput,
                "Ordered",
                    (checkEmployee(employees.getValue().toString())),
                sdf3.format(timestamp).substring(0, 10),
                sdf3.format(timestamp).substring(11),
                    69420);
        activeRequestTable.setItems(
            newRequest(
                request.getId(),
                request.getName(),
                request.getPatientName(),
                request.getDestination(),
                "Done",
                request.getEmployee(),
                request.getDate(),
                request.getTime(),
                amount));
        try {
          udb.add(
              new MedicineRequest(
                  request.getId(),
                  request.getName(),
                  request.getPatientName(),
                  request.getStatus(),
                  request.getEmployee(),
                  request.getDestination(),
                  request.getDate(),
                  request.getTime()
                      ));
          processText.setText("Request for " + checkBoxes.get(i).getText() + " successfully sent.");
        } catch (IOException e) {
          e.printStackTrace();
          processText.setText("Request for " + checkBoxes.get(i).getText() + " failed.");
          process();
          clear();
        }
      }
    }
    clear();
  }



  public void enableTxt() {
    if (Advil.isSelected()) {
      advilTxt.setDisable(false);
      //reset.setVisible();
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

  public void clear() {
    Advil.setSelected(false);
    Alprozalam.setSelected(false);
    AmphetamineSalt.setSelected(false);
    Atorvastatin.setSelected(false);
    Lisinopril.setSelected(false);
    Metformin.setSelected(false);
    specialCheck.setSelected(false);
    patientName.setText("");
    staffName.setText("");
    // IDtxt.setText("");
    advilTxt.setText("");
    alproTxt.setText("");
    saltTxt.setText("");
    atorvTxt.setText("");
    lisinTxt.setText("");
    metTxt.setText("");
    specialReqTxt.setText("");
    reset.setText("Cleared requests!");
    reset.setVisible(true);
    new Thread(
            () -> {
              try {
                Thread.sleep(1500); // milliseconds
                Platform.runLater(
                    () -> {
                      reset.setVisible(false);
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

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}
}
