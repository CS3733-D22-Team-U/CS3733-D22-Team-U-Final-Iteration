package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Report.Report;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import lombok.SneakyThrows;

public class ReportController extends ServiceController {
  public ComboBox<Employee> employees;
  public ComboBox<String> typeOfReport;

  @FXML Button clearButton;
  @FXML Button submitButton;
  @FXML JFXTextArea reportDescrip;
  @FXML BarChart reportBarChart;
  @FXML Text time;

  ArrayList<Employee> staff;
  ObservableList<String> typeList =
      FXCollections.observableArrayList(
          "Sexual Harassment", "Patient Mistreatment", "Medical Misconduct", "Other");

  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
    if (Udb.admin) {
      setUpGraph();
    } else {
      reportBarChart.setVisible(false);
    }
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

  private Employee checkEmployee(String employee) {
    if (EmployeeDaoImpl.List.get(employee) != null) {
      return EmployeeDaoImpl.List.get(employee);
    } else {
      Employee empty = new Employee("00000");
      return empty;
    }
  }

  @Override
  public void addRequest() throws SQLException, IOException {
    Employee temp_employee = employees.getValue();
    String inputType = typeOfReport.getValue();
    String inputDesc = reportDescrip.getText().trim();
    boolean alreadyHere = true;
    String reportID = "notWork";

    while (alreadyHere) {
      double rand = Math.random() * 10000;

      try {
        alreadyHere = Udb.getInstance().compServRequestImpl.hList().containsKey("GIF" + (int) rand);
      } catch (Exception e) {
        System.out.println(
            "alreadyHere variable messed up in gift and floral service request controller");
      }
      reportID = "REP" + (int) rand;
    }
    Report r =
        new Report(reportID, temp_employee, inputType, inputDesc, true, "00-00-00", "00:00:00");
    // Report r = new Report(
    try {
      Udb.getInstance().add(r);
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

  public void mouseHovered(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: #E6F6F7");
  }

  public void mouseExit(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: transparent");
  }

  public void setUpGraph() throws SQLException, IOException {

    reportBarChart.getXAxis().setLabel("Name of Employee");
    reportBarChart.getYAxis().setLabel("Number of Reports");

    XYChart.Series data = new XYChart.Series();
    data.setName("Reports Greater than 1");
    for (Employee employee : Udb.getInstance().EmployeeImpl.hList().values()) {
      if (employee.getReports() >= 1) {
        data.getData()
            .add(
                new XYChart.Data<>(
                    employee.getFirstName() + " " + employee.getLastName(), employee.getReports()));
      }
    }

    reportBarChart.getData().add(data);
  }
}
