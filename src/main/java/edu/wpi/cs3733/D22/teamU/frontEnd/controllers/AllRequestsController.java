package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Equipment.EquipmentDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.EquipRequest.EquipRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.EquipRequest.EquipRequestDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.GiftRequest.GiftRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.GiftRequest.GiftRequestDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.LabRequest.LabRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.LabRequest.LabRequestDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.LaundryRequest.LaundryRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.LaundryRequest.LaundryRequestDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MealRequest.MealRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MealRequest.MealRequestDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MedicineRequest.MedicineRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MedicineRequest.MedicineRequestDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AllRequestsController {
    @FXML TableColumn<MedicineRequest, String> allID;
    @FXML TableColumn<MedicineRequest, String> allName;
    @FXML TableColumn<MedicineRequest, Integer> allPatient;
    @FXML TableColumn<MedicineRequest, Integer> allDate;
    @FXML TableColumn<MedicineRequest, String> allTime;
    @FXML TableColumn<MedicineRequest, String> allStatus;
    @FXML TableColumn<MedicineRequest, String> allDestination;
    @FXML TableColumn<MedicineRequest, String> allEmployee;
    @FXML TableColumn<MedicineRequest, String> everyStatus;

    ObservableList<Request> EquipRequests = FXCollections.observableArrayList();
    ObservableList<Request> GiftRequests = FXCollections.observableArrayList();
    ObservableList<Request> LabRequests = FXCollections.observableArrayList();
    ObservableList<Request> LaundryRequests = FXCollections.observableArrayList();
    ObservableList<Request> MealRequests = FXCollections.observableArrayList();
    ObservableList<Request> MedRequests = FXCollections.observableArrayList();

    ObservableList<Request> EquipRequests2 = FXCollections.observableArrayList();
    ObservableList<Request> GiftRequests2 = FXCollections.observableArrayList();
    ObservableList<Request> LabRequests2 = FXCollections.observableArrayList();
    ObservableList<Request> LaundryRequests2 = FXCollections.observableArrayList();
    ObservableList<Request> MealRequests2 = FXCollections.observableArrayList();
    ObservableList<Request> MedRequests2 = FXCollections.observableArrayList();


    ArrayList<String> medicineTable = new ArrayList<String>();
    ArrayList<String> equipTable = new ArrayList<String>();
    ArrayList<String> giftTable = new ArrayList<String>();
    ArrayList<String> labTable = new ArrayList<String>();
    ArrayList<String> laundryTable = new ArrayList<String>();
    ArrayList<String> mealTable = new ArrayList<String>();
    ArrayList<String> requests = new ArrayList<String>();



    @FXML
    TableView activeRequestTable;


    @SneakyThrows
    public void initialize(URL location, ResourceBundle resources) {
        for (MedicineRequest medicineRequest : Udb.getInstance().medicineRequestImpl.List.values()) {

            MedRequests.add(
                    new Request(
                           medicineRequest.getID(),
                            medicineRequest.getName(),
                            medicineRequest.getPatientName(),
                            medicineRequest.getDate(),
                            medicineRequest.getTime(),
                            medicineRequest.getStatus(),
                            medicineRequest.getDestination(),
                            medicineRequest.getEmployee(),
                            medicineRequest.getLocation())
                    );



            medicineTable.add(medicineRequest.getID());
            medicineTable.add(medicineRequest.getName());
            medicineTable.add(medicineRequest.getPatientName());
            medicineTable.add(medicineRequest.getDate());
            medicineTable.add(medicineRequest.getTime());
            medicineTable.add(medicineRequest.getStatus());
            medicineTable.add(medicineRequest.getDestination());
            medicineTable.add(medicineRequest.employee.getEmployeeID());
            medicineTable.add(medicineRequest.getStatus());
        }

        for (EquipRequest equipRequest : Udb.getInstance().equipRequestImpl.List.values()) {

            EquipRequests.add(
                    new Request(
                            equipRequest.getID(),
                            equipRequest.getName(),
                            equipRequest.getPatientName(),
                            equipRequest.getDate(),
                            equipRequest.getTime(),
                            equipRequest.getStatus(),
                            equipRequest.getDestination(),
                            equipRequest.getEmployee(),
                            equipRequest.getLocation())
            );


             equipTable.add(equipRequest.getID());
             equipTable.add(equipRequest.getName());
             equipTable.add(equipRequest.getPatientName());
             equipTable.add(equipRequest.getDate());
             equipTable.add(equipRequest.getTime());
             equipTable.add(equipRequest.getStatus());
             equipTable.add(equipRequest.getDestination());
             equipTable.add(equipRequest.employee.getEmployeeID());
             equipTable.add(equipRequest.getStatus());
        }

        for (GiftRequest giftRequest : Udb.getInstance().giftRequestImpl.List.values()) {

            GiftRequests.add(
                    new Request(
                            giftRequest.getID(),
                            giftRequest.getName(),
                            giftRequest.getPatientName(),
                            giftRequest.getDate(),
                            giftRequest.getTime(),
                            giftRequest.getStatus(),
                            giftRequest.getDestination(),
                            giftRequest.getEmployee(),
                            giftRequest.getLocation())
            );

              giftTable.add(giftRequest.getID());
              giftTable.add(giftRequest.getName());
              giftTable.add(giftRequest.getPatientName());
              giftTable.add(giftRequest.getDate());
              giftTable.add(giftRequest.getTime());
              giftTable.add(giftRequest.getStatus());
              giftTable.add(giftRequest.getDestination());
              giftTable.add(giftRequest.employee.getEmployeeID());
              giftTable.add(giftRequest.getStatus());
        }

        for (LabRequest labRequest : Udb.getInstance().labRequestImpl.List.values()) {

            LabRequests.add(
                    new Request(
                            labRequest.getID(),
                            labRequest.getName(),
                            labRequest.getPatientName(),
                            labRequest.getDate(),
                            labRequest.getTime(),
                            labRequest.getStatus(),
                            labRequest.getDestination(),
                            labRequest.getEmployee(),
                            labRequest.getLocation())
            );

               labTable.add(labRequest.getID());
               labTable.add(labRequest.getName());
               labTable.add(labRequest.getPatientName());
               labTable.add(labRequest.getDate());
               labTable.add(labRequest.getTime());
               labTable.add(labRequest.getStatus());
               labTable.add(labRequest.getDestination());
               labTable.add(labRequest.getDestination());
               labTable.add(labRequest.getStatus());
        }

        for (LaundryRequest laundryRequest : Udb.getInstance().laundryRequestImpl.List.values()) {
            LaundryRequests.add(
                    new Request(
                            laundryRequest.getID(),
                            laundryRequest.getName(),
                            laundryRequest.getPatientName(),
                            laundryRequest.getDate(),
                            laundryRequest.getTime(),
                            laundryRequest.getStatus(),
                            laundryRequest.getDestination(),
                            laundryRequest.getEmployee(),
                            laundryRequest.getLocation())
            );

          laundryTable.add( laundryRequest.getID());
          laundryTable.add(laundryRequest.getName());
          laundryTable.add(laundryRequest.getPatientName());
          laundryTable.add(laundryRequest.getDate());
          laundryTable.add(laundryRequest.getTime());
          laundryTable.add(laundryRequest.getStatus());
          laundryTable.add(laundryRequest.getDestination());
          laundryTable.add(laundryRequest.getEmployee().getEmployeeID());
          laundryTable.add(laundryRequest.getStatus());
        }

        for (MealRequest mealRequest : Udb.getInstance().mealRequestImpl.List.values()){

            MealRequests.add(
                    new Request(
                            mealRequest.getID(),
                            mealRequest.getName(),
                            mealRequest.getPatientName(),
                            mealRequest.getDate(),
                            mealRequest.getTime(),
                            mealRequest.getStatus(),
                            mealRequest.getDestination(),
                            mealRequest.getEmployee(),
                            mealRequest.getLocation())
            );

            mealTable.add(mealRequest.getID());
            mealTable.add(mealRequest.getName());
            mealTable.add(mealRequest.getPatientName());
            mealTable.add(mealRequest.getDate());
            mealTable.add(mealRequest.getTime());
            mealTable.add(mealRequest.getStatus());
            mealTable.add(mealRequest.getDestination());
            mealTable.add(mealRequest.getEmployee().getEmployeeID());
            mealTable.add(mealRequest.getStatus());
        }

    }

    private void setUpActiveRequests() throws SQLException, IOException {
        allID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        allName.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPatient.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        allDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        allTime.setCellValueFactory(new PropertyValueFactory<>("time"));
        allStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        allDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));
        allEmployee.setCellValueFactory(new PropertyValueFactory<>("employee"));
        everyStatus.setCellValueFactory(new PropertyValueFactory<>("location"));
        activeRequestTable.setItems(getActiveRequestList());
    }

    private ObservableList<Request> getActiveRequestList() throws SQLException, IOException {


        for (MedicineRequest request :
                MedicineRequestDaoImpl.List.values()) {
            MedRequests2.add(
                    new Request(
                            MedRequests.get(0).ID,
                            MedRequests.get(1).name,
                            MedRequests.get(2).patientName,
                            MedRequests.get(3).date,
                            MedRequests.get(4).time,
                            MedRequests.get(5).status,
                            MedRequests.get(6).destination,
                            MedRequests.get(7).employee,
                            MedRequests.get(8).location)
                            );
        }

        for(EquipRequest request :
                EquipRequestDaoImpl.List.values()){
            EquipRequests2.add(
                    new Request(
                            EquipRequests.get(0).ID,
                            EquipRequests.get(1).name,
                            EquipRequests.get(2).patientName,
                            EquipRequests.get(3).date,
                            EquipRequests.get(4).time,
                            EquipRequests.get(5).status,
                            EquipRequests.get(6).destination,
                            EquipRequests.get(7).employee,
                            EquipRequests.get(8).location)
                    );

        }


        for(MealRequest request :
                MealRequestDaoImpl.List.values()){
            MealRequests2.add(
                    new Request(
                            MealRequests.get(0).ID,
                            MealRequests.get(1).name,
                            MealRequests.get(2).patientName,
                            MealRequests.get(3).date,
                            MealRequests.get(4).time,
                            MealRequests.get(5).status,
                            MealRequests.get(6).destination,
                            MealRequests.get(7).employee,
                            MealRequests.get(8).location)
                    );

        }

        for(GiftRequest request :
                GiftRequestDaoImpl.List.values()){
            GiftRequests2.add(
                    new Request(
                            GiftRequests.get(0).ID,
                            GiftRequests.get(1).name,
                            GiftRequests.get(2).patientName,
                            GiftRequests.get(3).date,
                            GiftRequests.get(4).time,
                            GiftRequests.get(5).status,
                            GiftRequests.get(6).destination,
                            GiftRequests.get(7).employee,
                            GiftRequests.get(8).location)

                    );

        }


        for(LaundryRequest request :
                LaundryRequestDaoImpl.List.values()){
            LaundryRequests2.add(
                    new Request(
                            LaundryRequests.get(0).ID,
                            LaundryRequests.get(1).name,
                            LaundryRequests.get(2).patientName,
                            LaundryRequests.get(3).date,
                            LaundryRequests.get(4).time,
                            LaundryRequests.get(5).status,
                            LaundryRequests.get(6).destination,
                            LaundryRequests.get(7).employee,
                            LaundryRequests.get(8).location)


                    );

        }

        for(LabRequest request :
                LabRequestDaoImpl.List.values()){
            LabRequests2.add(
                    new Request(
                            LabRequests.get(0).ID,
                            LabRequests.get(1).name,
                            LabRequests.get(2).patientName,
                            LabRequests.get(3).date,
                            LabRequests.get(4).time,
                            LabRequests.get(5).status,
                            LabRequests.get(6).destination,
                            LabRequests.get(7).employee,
                            LabRequests.get(8).location)

            );

        }

        return EquipRequests2;

    }

    public void toAllServices(ActionEvent actionEvent) throws IOException {
        Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/AllRequests.fxml");
        Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        appStage.setScene(scene);
        appStage.show();
    }


}
