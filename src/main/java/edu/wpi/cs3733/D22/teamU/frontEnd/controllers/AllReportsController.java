package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.BackEnd.Report.Report;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class AllReportsController implements Initializable {
  @FXML TableColumn<Report, String> idCol;
  @FXML TableColumn<Report, String> employeeCol;
  @FXML TableColumn<Report, String> typeCol;
  @FXML TableColumn<Report, String> descCol;
  @FXML TableColumn<Report, String> statusCol;
  @FXML TableColumn<Report, String> dateCol;
  @FXML TableColumn<Report, String> timeCol;

  @FXML TableView<Report> reportTable;

  ObservableList<Report> reportList = FXCollections.observableArrayList();

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setUpAllReports();
  }

  private void setUpAllReports() throws SQLException, IOException {

    idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
    employeeCol.setCellValueFactory(new PropertyValueFactory<>("employee"));
    descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
    statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
    timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
    reportTable.setItems(getReportsList());
  }

  private ObservableList<Report> getReportsList() throws SQLException, IOException {
    reportList.clear();
    for (Report report : Udb.getInstance().reportImpl.List.values()) {
      Report r =
          new Report(
              report.getId(),
              report.getEmployee(),
              report.getType(),
              report.getDescription(),
              report.getStatus(),
              report.getDate(),
              report.getTime());
      reportList.add(r);
    }
    return reportList;
  }

  public void backToReport(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/reportPage.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }
}
