package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.EquipRequest.EquipRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.GiftRequest.GiftRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.LabRequest.LabRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.LaundryRequest.LaundryRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MealRequest.MealRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MedicineRequest.MedicineRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MedicineRequest.MedicineRequestDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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


    ArrayList<String> medicineTable = new ArrayList<String>();
    ArrayList<String> equipTable = new ArrayList<String>();
    ArrayList<String> giftTable = new ArrayList<String>();
    ArrayList<String> labTable = new ArrayList<String>();
    ArrayList<String> laundryTable = new ArrayList<String>();
    ArrayList<String> mealTable = new ArrayList<String>();
    ArrayList<String> requests = new ArrayList<String>();



    @FXML
    TableView<MedicineRequest> activeRequestTable;


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
        //activeRequestTable.setItems(getActiveRequestList());
    }

    private ObservableList<MedicineRequest> getActiveRequestList() throws SQLException, IOException {
        for (MedicineRequest request :
                MedicineRequestDaoImpl.List.values()) {
            medUIRequests.add(
                    new MedicineRequest(
                            request.getID(),
                            request.getName(),
                            request.getAmount(),
                            request.getPatientName(),
                            request.getStatus(),
                            request.getEmployee(),
                            request.getDestination(),
                            request.getDate(),
                            request.getTime()));
        }
        return medUIRequests;
    }


}
