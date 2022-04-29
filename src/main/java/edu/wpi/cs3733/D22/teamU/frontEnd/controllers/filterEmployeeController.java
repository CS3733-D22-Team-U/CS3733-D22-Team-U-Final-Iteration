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
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import lombok.SneakyThrows;

public class filterEmployeeController extends ServiceController implements Initializable {
  @FXML TextField IDTxt;
  @FXML TextField firstTxt;
  @FXML TextField lastTxt;
  @FXML TextField occupationTxt;
  @FXML TextField dutyTxt;
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
  @FXML AnchorPane sideBarAnchor;
  @FXML Button sideBarButton;
  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  @FXML Button addButton;
  @FXML Button removeButton;
  @FXML Button editButton;
  @FXML Button submitEditButton;
  @FXML Button submitAddButton;
  @FXML Button cancelButton;

  ObservableList<Request> employeeRequestsList = FXCollections.observableArrayList();

  ArrayList<Employee> staff = new ArrayList<Employee>();

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);
    try {
      setUpEmployeeRequests();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    staff = new ArrayList<>();
    try {
      staff.addAll(Udb.getInstance().EmployeeImpl.hList().values());
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    employees.setTooltip(new Tooltip());
    employees.getItems().addAll(staff);
    new ComboBoxAutoComplete<Employee>(employees, 675, 380);
    handleTime();
    handleBar();
    addButton.setVisible(Udb.admin);
    editButton.setVisible(Udb.admin);
    removeButton.setVisible(Udb.admin);
    submitEditButton.setVisible(false);
    cancelButton.setVisible(false);
  }

  private void handleBar() {
    TranslateTransition openNav = new TranslateTransition(new Duration(350), sideBarAnchor);
    openNav.setToY(670);
    TranslateTransition closeNav = new TranslateTransition(new Duration(350), sideBarAnchor);
    sideBarButton.setOnAction(
        (ActionEvent evt) -> {
          if (sideBarAnchor.getTranslateY() != 670) {
            openNav.play();
          } else {
            closeNav.setToY(0);
            closeNav.play();
          }
        });
  }

  public void clear() {
    employees.valueProperty().set(null);
    employeeRequests.getItems().clear();
    IDTxt.setText("");
    firstTxt.setText("");
    lastTxt.setText("");
    occupationTxt.setText("");
    dutyTxt.setText("");
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
    masterThread = timeThread;
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

  public void showAllReq() throws SQLException, IOException {
    employeeRequests.setItems(getRequestList());
  }

  public void displayInfo() {

    Employee employee = employees.getValue();

    boolean onDuty = employee.getOnDuty();

    IDTxt.setText(employee.getEmployeeID());
    firstTxt.setText(employee.getFirstName());
    lastTxt.setText(employee.getLastName());
    occupationTxt.setText(employee.getOccupation());
    dutyTxt.setText(String.valueOf(onDuty));
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

  public void editEmployee() {
    if (employees.getValue() != null) {
      employees.setDisable(true);
      editableFields(true);
      submitEditButton.setVisible(true);
      cancelButton.setVisible(true);
      displayInfo();
    }
  }

  public void addEmployee() {
    employees.setValue(null);
    employees.setDisable(true);
    cancelButton.setVisible(true);
    submitAddButton.setVisible(true);
    editableFields(true);
    clear();
  }

  public void submitEmployeeEdit() {
    //  make an employee with fields' attributes
    editableFields(false);
    // add employee to list
    String ID;
    Employee oldEmployee = employees.getValue();
    oldEmployee.setFirstName(firstTxt.getText().trim());
    oldEmployee.setLastName(lastTxt.getText().trim());
    oldEmployee.setOccupation(occupationTxt.getText().trim());
    oldEmployee.setOnDuty(Boolean.parseBoolean(dutyTxt.getText().trim()));
    new ComboBoxAutoComplete<Employee>(employees, 675, 380);
    submitEditButton.setVisible(false);
    cancelButton.setVisible(false);
    employees.setDisable(false);
    editableFields(false);
    employees.setValue(null);
  }

  public void submitEmployeeAdd() {
    //  make an employee with fields' attributes
    editableFields(false);
    // add employee to list
    String ID;
    int rand = (int) Math.floor(Math.random() * 100000);
    ID = rand + "";
    Employee employee =
        new Employee(
            ID,
            firstTxt.getText().trim(),
            lastTxt.getText().trim(),
            occupationTxt.getText().trim(),
            Boolean.parseBoolean(dutyTxt.getText().trim()));
    try {
      Udb.getInstance().EmployeeImpl.add(employee);
      employees.getItems().add(employee);
    } catch (Exception e) {
      System.out.println(e.getStackTrace());
    }
    new ComboBoxAutoComplete<Employee>(employees, 675, 380);
    submitAddButton.setVisible(false);
    cancelButton.setVisible(false);
    employees.setDisable(false);
    editableFields(false);
    employees.setValue(null);
  }

  public void removeEmployee() {
    Employee employee = employees.getValue();
    try {
      Udb.getInstance().EmployeeImpl.remove(employee);
      employees.getItems().remove(employee);
    } catch (Exception e) {
      System.out.println(e.getStackTrace());
    }
    new ComboBoxAutoComplete<Employee>(employees, 675, 380);
    employees.setValue(null);
    clear();
    try {
      showAllReq();
    } catch (Exception e) {
      System.out.println(e.getStackTrace());
    }
  }

  public void editableFields(boolean set) {
    firstTxt.setEditable(set);
    lastTxt.setEditable(set);
    occupationTxt.setEditable(set);
    dutyTxt.setEditable(set);
  }

  public void cancelEdit() {
    employees.setDisable(false);
    employees.valueProperty().set(null);
    clear();
    try {
      showAllReq();
    } catch (Exception e) {
      System.out.println(e.getStackTrace());
    }
    cancelButton.setVisible(false);
    submitEditButton.setVisible(false);
    submitAddButton.setVisible(false);
  }
}
