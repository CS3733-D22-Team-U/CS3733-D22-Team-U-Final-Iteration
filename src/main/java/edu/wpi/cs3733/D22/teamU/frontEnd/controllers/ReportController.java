package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects.ComboBoxAutoComplete;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import lombok.SneakyThrows;

public class ReportController extends ServiceController {
  public ComboBox<Employee> employees;
  public ComboBox<String> typeOfReport;

  @FXML Button clearButton;
  @FXML Button submitButton;
  @FXML JFXTextArea reportDescrip;

  ArrayList<Employee> staff;
  ObservableList<String> typeList =
      FXCollections.observableArrayList(
          "Sexual Harassment", "Patient Mistreatment", "Medical Misconduct", "Other");

  @SneakyThrows
  public void initialize(URL location, ResourceBundle resources) {
    staff = new ArrayList<>();
    for (Employee e : Udb.getInstance().EmployeeImpl.hList().values()) {
      staff.add(e);
    }
    employees.setTooltip(new Tooltip());
    employees.getItems().addAll(staff);
    new ComboBoxAutoComplete<Employee>(employees, 675, 380);

    typeOfReport.setItems(typeList);
  }

  @Override
  public void addRequest() throws SQLException, IOException {
    Employee temp = employees.getValue();
    Employee employee =
        new Employee(
            temp.getEmployeeID(),
            temp.getFirstName(),
            temp.getLastName(),
            temp.getOccupation(),
            temp.getReports() + 1,
            temp.getOnDuty(),
            temp.getUsername(),
            temp.getPassword());
    try {
      Udb.getInstance().edit(employee);
    } catch (Exception e) {
      System.out.println("Line 66 ReportController");
    }
    employees.getSelectionModel().clearSelection();
  }

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}

  public void clearRequest(ActionEvent actionEvent) {
    employees.getSelectionModel().clearSelection();
    typeOfReport.setValue("");
    reportDescrip.clear();
  }

  public void mouseHovered(MouseEvent mouseEvent) {}

  public void mouseExit(MouseEvent mouseEvent) {}
}
