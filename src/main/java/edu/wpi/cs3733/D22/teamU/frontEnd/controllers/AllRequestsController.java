package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.BackEnd.Request.CompServRequest.CompServRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.EquipRequest.EquipRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.GiftRequest.GiftRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.LabRequest.LabRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.LaundryRequest.LaundryRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MaintenanceRequest.MaintenanceRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MealRequest.MealRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MedicineRequest.MedicineRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.ReligiousRequest.ReligiousRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.SecurityRequest.SecurityRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.TranslatorRequest.TranslatorRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.RequestUI;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.SneakyThrows;

public class AllRequestsController implements Initializable {
  @FXML TableColumn<RequestUI, String> allID;
  @FXML TableColumn<RequestUI, String> allType;
  @FXML TableColumn<RequestUI, String> allEmployeeF;
  @FXML TableColumn<RequestUI, String> allEmployeeL;
  @FXML TableColumn<RequestUI, String> allPatient;
  @FXML TableColumn<RequestUI, String> allDestination;
  @FXML TableColumn<RequestUI, String> allStatus;
  @FXML TableColumn<RequestUI, Integer> allDate;
  @FXML TableColumn<RequestUI, String> allTime;

  @FXML TableColumn<RequestUI, String> everyStatus;

  ObservableList<RequestUI> allRequests = FXCollections.observableArrayList();

  @FXML TableView<RequestUI> activeRequestTable;

  @SneakyThrows
  public void initialize(URL location, ResourceBundle resources) {
    setUpActiveRequests();
    // testing... System.out.println("we are here");
  }

  private void setUpActiveRequests() throws SQLException, IOException {
    allID.setCellValueFactory(new PropertyValueFactory<>("ID"));
    allType.setCellValueFactory(new PropertyValueFactory<>("reqType"));
    allEmployeeF.setCellValueFactory(new PropertyValueFactory<>("firstName"));
    allEmployeeL.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    allPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
    allDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));
    allStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    allDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    allTime.setCellValueFactory(new PropertyValueFactory<>("time"));
    activeRequestTable.setItems(getActiveRequestList());
  }

  private ObservableList<RequestUI> getActiveRequestList() throws SQLException, IOException {

    for (MedicineRequest request : Udb.getInstance().medicineRequestImpl.hList().values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              "Medicine Delivery Request",
              request.getEmployee().getFirstName(),
              request.getEmployee().getLastName(),
              request.getPatientName(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }

    for (EquipRequest request : Udb.getInstance().equipRequestImpl.hList().values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              "Equipment Delivery Request",
              request.getEmployee().getFirstName(),
              request.getEmployee().getLastName(),
              request.getPatientName(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }

    for (MealRequest request : Udb.getInstance().mealRequestImpl.hList().values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              "Meal Request",
              request.getEmployee().getFirstName(),
              request.getEmployee().getLastName(),
              request.getPatientName(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }

    for (GiftRequest request : Udb.getInstance().giftRequestImpl.hList().values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              "Gift Request",
              request.getEmployee().getFirstName(),
              request.getEmployee().getLastName(),
              request.getPatientName(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }

    for (LaundryRequest request : Udb.getInstance().laundryRequestImpl.hList().values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              "Laundry Request",
              request.getEmployee().getFirstName(),
              request.getEmployee().getLastName(),
              request.getPatientName(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }

    for (LabRequest request : Udb.getInstance().labRequestImpl.hList().values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              "Lab Request",
              request.getEmployee().getFirstName(),
              request.getEmployee().getLastName(),
              request.getPatientName(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }

    for (SecurityRequest request : Udb.getInstance().securityRequestImpl.List.values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              "Security Request",
              request.getEmployee().getFirstName(),
              request.getEmployee().getLastName(),
              request.getPatientName(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }

    for (CompServRequest request : Udb.getInstance().compServRequestImpl.List.values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              "Computer Service Request",
              request.getEmployee().getFirstName(),
              request.getEmployee().getLastName(),
              request.getPatientName(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }

    for (MaintenanceRequest request : Udb.getInstance().maintenanceRequestImpl.List.values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              "Maintenance Request",
              request.getEmployee().getFirstName(),
              request.getEmployee().getLastName(),
              request.getPatientName(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }

    for (ReligiousRequest request : Udb.getInstance().religiousRequestImpl.List.values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              "Religious Request",
              request.getEmployee().getFirstName(),
              request.getEmployee().getLastName(),
              request.getPatientName(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }

    for (SecurityRequest request : Udb.getInstance().securityRequestImpl.List.values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              "Security Request",
              request.getEmployee().getFirstName(),
              request.getEmployee().getLastName(),
              request.getPatientName(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }

    for (TranslatorRequest request : Udb.getInstance().translatorRequestImpl.List.values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              "Translator Request",
              request.getEmployee().getFirstName(),
              request.getEmployee().getLastName(),
              request.getPatientName(),
              request.getDestination(),
              request.getStatus(),
              request.getDate(),
              request.getTime()));
    }

    return allRequests;
  }

  public void toAllServices(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/AllRequests.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }

  public void toHome(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/Dashboard.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }

  public void toEquipmentDelivery(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/equipmentDelivery.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }

  public void toEmployeePage(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/employeePage.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }

  public void toMealDelivery(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/mealDelivery.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }

  public void toGiftAndFloral(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/giftFloralService.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }

  public void toMedicineDelivery(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/medRevamp.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
  }

  public void toLabRequest(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/labRequestServices.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }

  public void toLaundry(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/laundryService.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }

  public void toSideView(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/map.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }

  public void toLabRequestHelp(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/labRequestHelpPage.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }

  public void toAbout(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/about.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }

  public void toMedHelp(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/medHelp.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }

  public void toMap(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/sideView.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
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
