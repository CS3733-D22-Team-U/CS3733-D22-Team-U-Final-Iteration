package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Equipment.Equipment;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;
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

public class filterEmployeeController implements Initializable{
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

    private void setUpEmployeeRequests() throws SQLException, IOException {
        IDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        employeeCol.setCellValueFactory(new PropertyValueFactory<>("employee"));
        patientCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        destinationCol.setCellValueFactory(new PropertyValueFactory<>("destination"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        //employeeRequests.setItems(getActiveRequestList());
    }

    /*
    ObservableList<Request> requests = FXCollections.observableArrayList();
    public void updateList(ActionEvent actionEvent) throws SQLException, IOException {
        requests.clear();
        for (Request request : Udb.getInstance().) {
            //TODO: get value from employee comboBox
            String employee = employees.getValue().toString();
            try {
                if (request.getEmployee().getFirstName().equals(employee))
                    requests.add(
                            new Request(
                                    request.getID(),
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

     */






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
