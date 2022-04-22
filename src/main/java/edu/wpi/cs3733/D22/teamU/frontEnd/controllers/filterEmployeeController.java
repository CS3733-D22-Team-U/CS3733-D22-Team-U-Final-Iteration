package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Equipment.Equipment;
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
import edu.wpi.cs3733.D22.teamU.frontEnd.RequestUI;
import edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects.ComboBoxAutoComplete;
import edu.wpi.cs3733.D22.teamU.frontEnd.services.equipmentDelivery.EquipmentUI;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lombok.SneakyThrows;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
    @FXML Button submitButton;@FXML Button clearButton;

    @FXML TableColumn<Request, Integer> IDCol;
    @FXML TableColumn<Request, String> employeeCol;
    @FXML TableColumn<Request, String> patientCol;
    @FXML TableColumn<Request, String> destinationCol;
    @FXML TableColumn<Request, String> statusCol;
    @FXML TableColumn<Request, String> dateCol;
    @FXML TableColumn<Request, String> timeCol;
    @FXML TableView<Request> employeeRequests;


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
    }

    @Override
    public void addRequest() throws SQLException, IOException {

    }

    @Override
    public void removeRequest() {

    }

    @Override
    public void updateRequest() {

    }

    private void setUpEmployeeRequests() throws SQLException, IOException {
        IDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        employeeCol.setCellValueFactory(new PropertyValueFactory<>("employee"));
        patientCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        employeeRequests.setItems(getRequestList());
    }

    private ObservableList <Request> getRequestList() throws SQLException, IOException{
        for(CompServRequest request : CompServRequestDaoImpl.List.values()){
            employeeRequestsList.add(
                    new Request(
                            request.getID(),
                            "Computer Service",
                            request.getEmployee(),
                            request.getPatientName(),
                            request.getDestination(),
                            request.getStatus(),
                            request.getDate(),
                            request.getTime()));
        }
        for(EquipRequest request : EquipRequestDaoImpl.List.values()){
            employeeRequestsList.add(
                    new Request(
                            request.getID(),
                            "Equipment Request",
                            request.getEmployee(),
                            request.getPatientName(),
                            request.getDestination(),
                            request.getStatus(),
                            request.getDate(),
                            request.getTime()));
        }
        for(GiftRequest request : GiftRequestDaoImpl.List.values()){
            employeeRequestsList.add(
                    new Request(
                            request.getID(),
                            "Gift Request",
                            request.getEmployee(),
                            request.getPatientName(),
                            request.getDestination(),
                            request.getStatus(),
                            request.getDate(),
                            request.getTime()));
        }
        for(LabRequest request : LabRequestDaoImpl.List.values()){
            employeeRequestsList.add(
                    new Request(
                            request.getID(),
                            "Lab Request",
                            request.getEmployee(),
                            request.getPatientName(),
                            request.getDestination(),
                            request.getStatus(),
                            request.getDate(),
                            request.getTime()));
        }
        for(LaundryRequest request : LaundryRequestDaoImpl.List.values()){
            employeeRequestsList.add(
                    new Request(
                            request.getID(),
                            "Laundry Request",
                            request.getEmployee(),
                            request.getPatientName(),
                            request.getDestination(),
                            request.getStatus(),
                            request.getDate(),
                            request.getTime()));
        }
        for(MaintenanceRequest request : MaintenanceRequestDaoImpl.List.values()){
            employeeRequestsList.add(
                    new Request(
                            request.getID(),
                            "Maintenance Request",
                            request.getEmployee(),
                            request.getPatientName(),
                            request.getDestination(),
                            request.getStatus(),
                            request.getDate(),
                            request.getTime()));
        }
        for(MealRequest request : MealRequestDaoImpl.List.values()){
            employeeRequestsList.add(
                    new Request(
                            request.getID(),
                            "Meal Request",
                            request.getEmployee(),
                            request.getPatientName(),
                            request.getDestination(),
                            request.getStatus(),
                            request.getDate(),
                            request.getTime()));
        }
        for(MedicineRequest request : MedicineRequestDaoImpl.List.values()){
            employeeRequestsList.add(
                    new Request(
                            request.getID(),
                            "Medicine Delivery",
                            request.getEmployee(),
                            request.getPatientName(),
                            request.getDestination(),
                            request.getStatus(),
                            request.getDate(),
                            request.getTime()));
        }
        for(ReligiousRequest request : ReligiousRequestDaoImpl.List.values()){
            employeeRequestsList.add(
                    new Request(
                            request.getID(),
                            "Religious Request",
                            request.getEmployee(),
                            request.getPatientName(),
                            request.getDestination(),
                            request.getStatus(),
                            request.getDate(),
                            request.getTime()));
        }
        for(SecurityRequest request : SecurityRequestDaoImpl.List.values()){
            employeeRequestsList.add(
                    new Request(
                            request.getID(),
                            "Security Request",
                            request.getEmployee(),
                            request.getPatientName(),
                            request.getDestination(),
                            request.getStatus(),
                            request.getDate(),
                            request.getTime()));
        }
        for(TranslatorRequest request : TranslatorRequestDaoImpl.List.values()){
            employeeRequestsList.add(
                    new Request(
                            request.getID(),
                            "Translator Request",
                            request.getEmployee(),
                            request.getPatientName(),
                            request.getDestination(),
                            request.getStatus(),
                            request.getDate(),
                            request.getTime()));
        }
        return employeeRequestsList;

    }


    ObservableList<Request> requests = FXCollections.observableArrayList();
    public void updateList(ActionEvent actionEvent) throws SQLException, IOException {
        requests.clear();

        for (CompServRequest request : Udb.getInstance().compServRequestImpl.list()) {
            //TODO: get value from employee comboBox
            String employee = employees.getValue().toString();
            try {
                if (request.getEmployee().getFirstName().equals(employee))
                    requests.add(
                            new Request(
                                    request.getID(),
                                    "Computer Service",
                                    request.getEmployee(),
                                    request.getPatientName(),
                                    request.getDestination(),
                                    request.getStatus(),
                                    request.getDate(),
                                    request.getTime()));
            } catch (Exception e) {
            }
            employeeRequests.setItems(requests);

        }

        for (EquipRequest request : Udb.getInstance().equipRequestImpl.list()) {
            //TODO: get value from employee comboBox
            String employee = employees.getValue().toString();
            try {
                if (request.getEmployee().getFirstName().equals(employee))
                    requests.add(
                            new Request(
                                    request.getID(),
                                    "Equipment Request",
                                    request.getEmployee(),
                                    request.getPatientName(),
                                    request.getDestination(),
                                    request.getStatus(),
                                    request.getDate(),
                                    request.getTime()));
            } catch (Exception e) {
            }
            employeeRequests.setItems(requests);

        }

        for (GiftRequest request : Udb.getInstance().giftRequestImpl.list()) {
            //TODO: get value from employee comboBox
            String employee = employees.getValue().toString();
            try {
                if (request.getEmployee().getFirstName().equals(employee))
                    requests.add(
                            new Request(
                                    request.getID(),
                                    "Gift Request",
                                    request.getEmployee(),
                                    request.getPatientName(),
                                    request.getDestination(),
                                    request.getStatus(),
                                    request.getDate(),
                                    request.getTime()));
            } catch (Exception e) {
            }
            employeeRequests.setItems(requests);

        }

        for (LabRequest request : Udb.getInstance().labRequestImpl.list()) {
            //TODO: get value from employee comboBox
            String employee = employees.getValue().toString();
            try {
                if (request.getEmployee().getFirstName().equals(employee))
                    requests.add(
                            new Request(
                                    request.getID(),
                                    "Lab Request",
                                    request.getEmployee(),
                                    request.getPatientName(),
                                    request.getDestination(),
                                    request.getStatus(),
                                    request.getDate(),
                                    request.getTime()));
            } catch (Exception e) {
            }
            employeeRequests.setItems(requests);

        }

        for (LaundryRequest request : Udb.getInstance().laundryRequestImpl.list()) {
            //TODO: get value from employee comboBox
            String employee = employees.getValue().toString();
            try {
                if (request.getEmployee().getFirstName().equals(employee))
                    requests.add(
                            new Request(
                                    request.getID(),
                                    "Laundry Request",
                                    request.getEmployee(),
                                    request.getPatientName(),
                                    request.getDestination(),
                                    request.getStatus(),
                                    request.getDate(),
                                    request.getTime()));
            } catch (Exception e) {
            }
            employeeRequests.setItems(requests);

        }

        for (MaintenanceRequest request : Udb.getInstance().maintenanceRequestImpl.list()) {
            //TODO: get value from employee comboBox
            String employee = employees.getValue().toString();
            try {
                if (request.getEmployee().getFirstName().equals(employee))
                    requests.add(
                            new Request(
                                    request.getID(),
                                    "Maintenance Request",
                                    request.getEmployee(),
                                    request.getPatientName(),
                                    request.getDestination(),
                                    request.getStatus(),
                                    request.getDate(),
                                    request.getTime()));
            } catch (Exception e) {
            }
            employeeRequests.setItems(requests);

        }

        for (MealRequest  request : Udb.getInstance().mealRequestImpl.list()) {
            //TODO: get value from employee comboBox
            String employee = employees.getValue().toString();
            try {
                if (request.getEmployee().getFirstName().equals(employee))
                    requests.add(
                            new Request(
                                    request.getID(),
                                    "Meal Request",
                                    request.getEmployee(),
                                    request.getPatientName(),
                                    request.getDestination(),
                                    request.getStatus(),
                                    request.getDate(),
                                    request.getTime()));
            } catch (Exception e) {
            }
            employeeRequests.setItems(requests);

        }

        for (MedicineRequest request : Udb.getInstance().medicineRequestImpl.list()) {
            //TODO: get value from employee comboBox
            String employee = employees.getValue().toString();
            try {
                if (request.getEmployee().getFirstName().equals(employee))
                    requests.add(
                            new Request(
                                    request.getID(),
                                    "Medicine Delivery Request",
                                    request.getEmployee(),
                                    request.getPatientName(),
                                    request.getDestination(),
                                    request.getStatus(),
                                    request.getDate(),
                                    request.getTime()));
            } catch (Exception e) {
            }
            employeeRequests.setItems(requests);

        }

        for (ReligiousRequest request : Udb.getInstance().religiousRequestImpl.list()) {
            //TODO: get value from employee comboBox
            String employee = employees.getValue().toString();
            try {
                if (request.getEmployee().getFirstName().equals(employee))
                    requests.add(
                            new Request(
                                    request.getID(),
                                    "Religious Request",
                                    request.getEmployee(),
                                    request.getPatientName(),
                                    request.getDestination(),
                                    request.getStatus(),
                                    request.getDate(),
                                    request.getTime()));
            } catch (Exception e) {
            }
            employeeRequests.setItems(requests);

        }

        for (SecurityRequest request : Udb.getInstance().securityRequestImpl.list()) {
            //TODO: get value from employee comboBox
            String employee = employees.getValue().toString();
            try {
                if (request.getEmployee().getFirstName().equals(employee))
                    requests.add(
                            new Request(
                                    request.getID(),
                                    "Security Request",
                                    request.getEmployee(),
                                    request.getPatientName(),
                                    request.getDestination(),
                                    request.getStatus(),
                                    request.getDate(),
                                    request.getTime()));
            } catch (Exception e) {
            }
            employeeRequests.setItems(requests);

        }

        for (TranslatorRequest request : Udb.getInstance().translatorRequestImpl.list()) {
            //TODO: get value from employee comboBox
            String employee = employees.getValue().toString();
            try {
                if (request.getEmployee().getFirstName().equals(employee))
                    requests.add(
                            new Request(
                                    request.getID(),
                                    "Translator Request",
                                    request.getEmployee(),
                                    request.getPatientName(),
                                    request.getDestination(),
                                    request.getStatus(),
                                    request.getDate(),
                                    request.getTime()));
            } catch (Exception e) {
            }
            employeeRequests.setItems(requests);

        }

    }



    public void displayInfo(ActionEvent actionEvent){

        Employee employee =  employees.getValue();
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



}
