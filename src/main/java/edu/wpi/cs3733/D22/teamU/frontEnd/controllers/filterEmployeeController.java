package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import static javax.script.Bindings.*;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.CompServRequest.CompServRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.CompServRequest.CompServRequestDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.EquipRequest.EquipRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.EquipRequest.EquipRequestDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.GiftRequest.GiftRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.GiftRequest.GiftRequestDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.LabRequest.LabRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.LabRequest.LabRequestDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.LaundryRequest.LaundryRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.LaundryRequest.LaundryRequestDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MaintenanceRequest.MaintenanceRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MaintenanceRequest.MaintenanceRequestDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MealRequest.MealRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MealRequest.MealRequestDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MedicineRequest.MedicineRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MedicineRequest.MedicineRequestDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.ReligiousRequest.ReligiousRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.ReligiousRequest.ReligiousRequestDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.SecurityRequest.SecurityRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.SecurityRequest.SecurityRequestDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.TranslatorRequest.TranslatorRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.TranslatorRequest.TranslatorRequestDaoImpl;
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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class filterEmployeeController extends ServiceController implements Initializable {
  @FXML TextField IDTxt;
  @FXML TextField firstTxt;
  @FXML TextField lastTxt;
  @FXML TextField occupationTxt;
  @FXML TextField reportsTxt;
  @FXML TextField dutyTxt;
  @FXML TextField userTxt;
  @FXML TextField passwordTxt;
  @FXML ComboBox<Employee> employees = new ComboBox<Employee>();
  @FXML Button submitButton;
  @FXML Button clearButton;

  @FXML TableColumn<Request, String> IDCol;
  @FXML TableColumn<Request, String> typeCol;
  @FXML TableColumn<Request, String> employeeCol;
  @FXML TableColumn<Request, String> patientCol;
  @FXML TableColumn<Request, String> destinationCol;
  @FXML TableColumn<Request, String> statusCol;
  @FXML TableColumn<Request, String> dateCol;
  @FXML TableColumn<Request, String> timeCol;
  @FXML TableView<Request> employeeRequests;
  @FXML Text time;
  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  ObservableList<Request> employeeRequestsList = FXCollections.observableArrayList();

  ArrayList<Employee> staff = new ArrayList<Employee>();

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setUpEmployeeRequests();
    staff = new ArrayList<>();
    staff.addAll(Udb.getInstance().EmployeeImpl.hList().values());
    employees.setTooltip(new Tooltip());
    employees.getItems().addAll(staff);
    new ComboBoxAutoComplete<Employee>(employees, 675, 380);
    handleTime();
  }

  public void clear(ActionEvent actionEvent) {
    employees.valueProperty().set(null);
    employeeRequests.getItems().clear();
    IDTxt.setText("");
    firstTxt.setText("");
    lastTxt.setText("");
    occupationTxt.setText("");
    reportsTxt.setText("");
    dutyTxt.setText("");
    userTxt.setText("");
    passwordTxt.setText("");
    employees.setPromptText("Choose Employee:");
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
  public void addRequest() throws SQLException, IOException {}

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}

  private void setUpEmployeeRequests() throws SQLException, IOException {
    IDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
    typeCol.setCellValueFactory(new PropertyValueFactory<>("reqType")); // todo reqType
    employeeCol.setCellValueFactory(new PropertyValueFactory<>("employee"));
    // patientCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
    destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));
    statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
    employeeRequests.setItems(getRequestList());
  }

  private ObservableList<Request> getRequestList() throws SQLException, IOException {

    for (CompServRequest request : CompServRequestDaoImpl.List.values()) {
      employeeRequestsList.add(
          new Request(
              request.getID(),
              "Computer Service",
              request.getEmployee(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }
    for (EquipRequest request : EquipRequestDaoImpl.List.values()) {
      employeeRequestsList.add(
          new Request(
              request.getID(),
              "Equipment Request",
              request.getEmployee(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }
    for (GiftRequest request : GiftRequestDaoImpl.List.values()) {
      employeeRequestsList.add(
          new Request(
              request.getID(),
              "Gift Request",
              request.getEmployee(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }
    for (LabRequest request : LabRequestDaoImpl.List.values()) {
      employeeRequestsList.add(
          new Request(
              request.getID(),
              "Lab Request",
              request.getEmployee(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }
    for (LaundryRequest request : LaundryRequestDaoImpl.List.values()) {
      employeeRequestsList.add(
          new Request(
              request.getID(),
              "Laundry Request",
              request.getEmployee(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }
    for (MaintenanceRequest request : MaintenanceRequestDaoImpl.List.values()) {
      employeeRequestsList.add(
          new Request(
              request.getID(),
              "Maintenance Request",
              request.getEmployee(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }
    for (MealRequest request : MealRequestDaoImpl.List.values()) {
      employeeRequestsList.add(
          new Request(
              request.getID(),
              "Meal Request",
              request.getEmployee(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }
    for (MedicineRequest request : MedicineRequestDaoImpl.List.values()) {
      employeeRequestsList.add(
          new Request(
              request.getID(),
              "Medicine Request",
              request.getEmployee(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }
    for (ReligiousRequest request : ReligiousRequestDaoImpl.List.values()) {
      employeeRequestsList.add(
          new Request(
              request.getID(),
              "Religious Request",
              request.getEmployee(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }
    for (SecurityRequest request : SecurityRequestDaoImpl.List.values()) {
      employeeRequestsList.add(
          new Request(
              request.getID(),
              "Security Request",
              request.getEmployee(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }
    for (TranslatorRequest request : TranslatorRequestDaoImpl.List.values()) {
      employeeRequestsList.add(
          new Request(
              request.getID(),
              "Translator Request",
              request.getEmployee(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }
    return employeeRequestsList;
  }

  // ObservableList<Request> requests = FXCollections.observableArrayList();
  public void updateList(ActionEvent actionEvent) throws SQLException, IOException {
    displayInfo();

    ArrayList<Request> requests = new ArrayList<>();
    for (Request r : employeeRequestsList) {
      if (r.getEmployee().equals(employees.getValue())) requests.add(r);
    }

    employeeRequests.setItems(FXCollections.observableArrayList(requests));
  }

  public void showAllReq(ActionEvent actionEvent) throws SQLException, IOException {
    employeeRequests.setItems(getRequestList());
  }

  public void displayInfo() {

    Employee employee = employees.getValue();

    int reports = employee.getReports();
    boolean onDuty = employee.getOnDuty();

    IDTxt.setText(employee.getEmployeeID());
    firstTxt.setText(employee.getFirstName());
    lastTxt.setText(employee.getLastName());
    occupationTxt.setText(employee.getOccupation());
    reportsTxt.setText(String.valueOf(reports));
    dutyTxt.setText(String.valueOf(onDuty));
    userTxt.setText(employee.getUsername());
    passwordTxt.setText(employee.getPassword());
  }

  public Employee checkEmployee(String employee) throws NullPointerException {
    if (EmployeeDaoImpl.List.get(employee) != null) {
      return EmployeeDaoImpl.List.get(employee);
    } else {
      Employee empty = new Employee("N/A");
      return empty;
    }
  }

  public void mouseHovered(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: #E6F6F7");
  }

  public void mouseExit(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: transparent");
  }

  public void toEmployeePage(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/employeePage.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }
}
