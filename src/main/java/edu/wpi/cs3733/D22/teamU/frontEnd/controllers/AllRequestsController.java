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
import java.util.ArrayList;
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
  @FXML TableColumn<RequestUI, String> allName;
  @FXML TableColumn<RequestUI, String> allPatient;
  @FXML TableColumn<RequestUI, Integer> allDate;
  @FXML TableColumn<RequestUI, String> allTime;
  @FXML TableColumn<RequestUI, String> allStatus;
  @FXML TableColumn<RequestUI, String> allDestination;
  @FXML TableColumn<RequestUI, String> allEmployee;
  @FXML TableColumn<RequestUI, String> everyStatus;

  ObservableList<RequestUI> EquipRequests = FXCollections.observableArrayList();
  ObservableList<RequestUI> GiftRequests = FXCollections.observableArrayList();
  ObservableList<RequestUI> LabRequests = FXCollections.observableArrayList();
  ObservableList<RequestUI> LaundryRequests = FXCollections.observableArrayList();
  ObservableList<RequestUI> MealRequests = FXCollections.observableArrayList();
  ObservableList<RequestUI> MedRequests = FXCollections.observableArrayList();

  ObservableList<RequestUI> allRequests = FXCollections.observableArrayList();
  ObservableList<RequestUI> GiftRequests2 = FXCollections.observableArrayList();
  ObservableList<RequestUI> LabRequests2 = FXCollections.observableArrayList();
  ObservableList<RequestUI> LaundryRequests2 = FXCollections.observableArrayList();
  ObservableList<RequestUI> MealRequests2 = FXCollections.observableArrayList();
  ObservableList<RequestUI> MedRequests2 = FXCollections.observableArrayList();

  ArrayList<String> medicineTable = new ArrayList<String>();
  ArrayList<String> equipTable = new ArrayList<String>();
  ArrayList<String> giftTable = new ArrayList<String>();
  ArrayList<String> labTable = new ArrayList<String>();
  ArrayList<String> laundryTable = new ArrayList<String>();
  ArrayList<String> mealTable = new ArrayList<String>();
  ArrayList<String> requests = new ArrayList<String>();

  @FXML TableView<RequestUI> activeRequestTable;

  @SneakyThrows
  public void initialize(URL location, ResourceBundle resources) {
    setUpActiveRequests();
    System.out.println("we are here");
  }
  //      for (MedicineRequest medicineRequest :
  // Udb.getInstance().medicineRequestImpl.hList().values()) {
  //
  //            MedRequests.add(
  //                    new RequestUI(
  //                           medicineRequest.getID(),
  //                            medicineRequest.getName(),
  //                            medicineRequest.getPatientName(),
  //                            medicineRequest.getDate(),
  //                            medicineRequest.getTime(),
  //                            medicineRequest.getStatus(),
  //                            medicineRequest.getDestination(),
  //                            medicineRequest.getEmployee().getEmployeeID(),
  //                            medicineRequest.getLocation().getNodeID())
  //                    );
  //
  //
  //
  //            medicineTable.add(medicineRequest.getID());
  //            medicineTable.add(medicineRequest.getName());
  //            medicineTable.add(medicineRequest.getPatientName());
  //            medicineTable.add(medicineRequest.getDate());
  //            medicineTable.add(medicineRequest.getTime());
  //            medicineTable.add(medicineRequest.getStatus());
  //            medicineTable.add(medicineRequest.getDestination());
  //            medicineTable.add(medicineRequest.employee.getEmployeeID());
  //            medicineTable.add(medicineRequest.getStatus());
  //        }
  //
  //        for (EquipRequest equipRequest : Udb.getInstance().equipRequestImpl.hList().values()) {
  //
  //            EquipRequests.add(
  //                    new RequestUI(
  //                            equipRequest.getID(),
  //                            equipRequest.getName(),
  //                            equipRequest.getPatientName(),
  //                            equipRequest.getDate(),
  //                            equipRequest.getTime(),
  //                            equipRequest.getStatus(),
  //                            equipRequest.getDestination(),
  //                            equipRequest.getEmployee().getEmployeeID(),
  //                            equipRequest.getLocation().getNodeID())
  //            );
  //
  //
  //             equipTable.add(equipRequest.getID());
  //             equipTable.add(equipRequest.getName());
  //             equipTable.add(equipRequest.getPatientName());
  //             equipTable.add(equipRequest.getDate());
  //             equipTable.add(equipRequest.getTime());
  //             equipTable.add(equipRequest.getStatus());
  //             equipTable.add(equipRequest.getDestination());
  //             equipTable.add(equipRequest.employee.getEmployeeID());
  //             equipTable.add(equipRequest.getStatus());
  //        }
  //
  //        for (GiftRequest giftRequest : Udb.getInstance().giftRequestImpl.hList().values()) {
  //
  //            GiftRequests.add(
  //                    new RequestUI(
  //                            giftRequest.getID(),
  //                            giftRequest.getName(),
  //                            giftRequest.getPatientName(),
  //                            giftRequest.getDate(),
  //                            giftRequest.getTime(),
  //                            giftRequest.getStatus(),
  //                            giftRequest.getDestination(),
  //                            giftRequest.getEmployee().getEmployeeID(),
  //                            giftRequest.getLocation().getNodeID())
  //            );
  //
  //              giftTable.add(giftRequest.getID());
  //              giftTable.add(giftRequest.getName());
  //              giftTable.add(giftRequest.getPatientName());
  //              giftTable.add(giftRequest.getDate());
  //              giftTable.add(giftRequest.getTime());
  //              giftTable.add(giftRequest.getStatus());
  //              giftTable.add(giftRequest.getDestination());
  //              giftTable.add(giftRequest.employee.getEmployeeID());
  //              giftTable.add(giftRequest.getStatus());
  //        }
  //
  //        for (LabRequest labRequest : Udb.getInstance().labRequestImpl.hList().values()) {
  //
  //            LabRequests.add(
  //                    new RequestUI(
  //                            labRequest.getID(),
  //                            labRequest.getName(),
  //                            labRequest.getPatientName(),
  //                            labRequest.getDate(),
  //                            labRequest.getTime(),
  //                            labRequest.getStatus(),
  //                            labRequest.getDestination(),
  //                            labRequest.getEmployee().getEmployeeID(),
  //                            labRequest.getLocation().getNodeID())
  //            );
  //
  //               labTable.add(labRequest.getID());
  //               labTable.add(labRequest.getName());
  //               labTable.add(labRequest.getPatientName());
  //               labTable.add(labRequest.getDate());
  //               labTable.add(labRequest.getTime());
  //               labTable.add(labRequest.getStatus());
  //               labTable.add(labRequest.getDestination());
  //               labTable.add(labRequest.getDestination());
  //               labTable.add(labRequest.getStatus());
  //        }
  //
  //        for (LaundryRequest laundryRequest :
  // Udb.getInstance().laundryRequestImpl.hList().values()) {
  //            LaundryRequests.add(
  //                    new RequestUI(
  //                            laundryRequest.getID(),
  //                            laundryRequest.getName(),
  //                            laundryRequest.getPatientName(),
  //                            laundryRequest.getDate(),
  //                            laundryRequest.getTime(),
  //                            laundryRequest.getStatus(),
  //                            laundryRequest.getDestination(),
  //                            laundryRequest.getEmployee().getEmployeeID(),
  //                            laundryRequest.getLocation().getNodeID())
  //            );
  //
  //          laundryTable.add( laundryRequest.getID());
  //          laundryTable.add(laundryRequest.getName());
  //          laundryTable.add(laundryRequest.getPatientName());
  //          laundryTable.add(laundryRequest.getDate());
  //          laundryTable.add(laundryRequest.getTime());
  //          laundryTable.add(laundryRequest.getStatus());
  //          laundryTable.add(laundryRequest.getDestination());
  //          laundryTable.add(laundryRequest.getEmployee().getEmployeeID());
  //          laundryTable.add(laundryRequest.getStatus());
  //        }
  //
  //        for (MealRequest mealRequest : Udb.getInstance().mealRequestImpl.hList().values()){
  //
  //            MealRequests.add(
  //                    new RequestUI(
  //                            mealRequest.getID(),
  //                            mealRequest.getName(),
  //                            mealRequest.getPatientName(),
  //                            mealRequest.getDate(),
  //                            mealRequest.getTime(),
  //                            mealRequest.getStatus(),
  //                            mealRequest.getDestination(),
  //                            mealRequest.getEmployee().getEmployeeID(),
  //                            mealRequest.getLocation().getNodeID())
  //            );
  //
  //            mealTable.add(mealRequest.getID());
  //            mealTable.add(mealRequest.getName());
  //            mealTable.add(mealRequest.getPatientName());
  //            mealTable.add(mealRequest.getDate());
  //            mealTable.add(mealRequest.getTime());
  //            mealTable.add(mealRequest.getStatus());
  //            mealTable.add(mealRequest.getDestination());
  //            mealTable.add(mealRequest.getEmployee().getEmployeeID());
  //            mealTable.add(mealRequest.getStatus());
  //        }
  //
  //    }

  private void setUpActiveRequests() throws SQLException, IOException {
    allID.setCellValueFactory(new PropertyValueFactory<>("ID"));
    allName.setCellValueFactory(new PropertyValueFactory<>("name"));
    allPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
    allDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    allTime.setCellValueFactory(new PropertyValueFactory<>("time"));
    allStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    allDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));
    allEmployee.setCellValueFactory(new PropertyValueFactory<>("employee"));
    // everyStatus.setCellValueFactory(new PropertyValueFactory<>("location"));
    activeRequestTable.setItems(getActiveRequestList());
  }

  private ObservableList<RequestUI> getActiveRequestList() throws SQLException, IOException {

    for (MedicineRequest request : Udb.getInstance().medicineRequestImpl.hList().values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              request.getName(),
              request.getPatientName(),
              request.getDate(),
              request.getTime(),
              request.getStatus(),
              request.getDestination(),
              request.getEmployee().getUsername()));
    }

    for (EquipRequest request : Udb.getInstance().equipRequestImpl.hList().values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              request.getName(),
              request.getPatientName(),
              request.getDate(),
              request.getTime(),
              request.getStatus(),
              request.getDestination(),
              request.getEmployee().getUsername()));
    }

    for (MealRequest request : Udb.getInstance().mealRequestImpl.hList().values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              request.getName(),
              request.getPatientName(),
              request.getDate(),
              request.getTime(),
              request.getStatus(),
              request.getDestination(),
              request.getEmployee().getUsername()));
    }

    for (GiftRequest request : Udb.getInstance().giftRequestImpl.hList().values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              request.getName(),
              request.getPatientName(),
              request.getDate(),
              request.getTime(),
              request.getStatus(),
              request.getDestination(),
              request.getEmployee().getUsername()));
    }

    for (LaundryRequest request : Udb.getInstance().laundryRequestImpl.hList().values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              request.getName(),
              request.getPatientName(),
              request.getDate(),
              request.getTime(),
              request.getStatus(),
              request.getDestination(),
              request.getEmployee().getUsername()));
    }

    for (LabRequest request : Udb.getInstance().labRequestImpl.hList().values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              request.getName(),
              request.getPatientName(),
              request.getDate(),
              request.getTime(),
              request.getStatus(),
              request.getDestination(),
              request.getEmployee().getUsername()));
    }

    for (SecurityRequest request : Udb.getInstance().securityRequestImpl.List.values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              request.getName(),
              request.getPatientName(),
              request.getDate(),
              request.getTime(),
              request.getStatus(),
              request.getDestination(),
              request.getEmployee().getUsername()));
    }

    for (CompServRequest request : Udb.getInstance().compServRequestImpl.List.values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              request.getName(),
              request.getPatientName(),
              request.getDate(),
              request.getTime(),
              request.getStatus(),
              request.getDestination(),
              request.getEmployee().getUsername()));
    }

    for (MaintenanceRequest request : Udb.getInstance().maintenanceRequestImpl.List.values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              request.getName(),
              request.getPatientName(),
              request.getDate(),
              request.getTime(),
              request.getStatus(),
              request.getDestination(),
              request.getEmployee().getUsername()));
    }

    for (ReligiousRequest request : Udb.getInstance().religiousRequestImpl.List.values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              request.getName(),
              request.getPatientName(),
              request.getDate(),
              request.getTime(),
              request.getStatus(),
              request.getDestination(),
              request.getEmployee().getUsername()));
    }

    for (SecurityRequest request : Udb.getInstance().securityRequestImpl.List.values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              request.getName(),
              request.getPatientName(),
              request.getDate(),
              request.getTime(),
              request.getStatus(),
              request.getDestination(),
              request.getEmployee().getUsername()));
    }

    for (TranslatorRequest request : Udb.getInstance().translatorRequestImpl.List.values()) {
      allRequests.add(
          new RequestUI(
              request.getID(),
              request.getName(),
              request.getPatientName(),
              request.getDate(),
              request.getTime(),
              request.getStatus(),
              request.getDestination(),
              request.getEmployee().getUsername()));
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
