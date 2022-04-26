package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.DBController;
import edu.wpi.cs3733.D22.teamU.MedicineRequest;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import edu.wpi.cs3733.D22.teamZ.api.exception.ServiceException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class RequestPageController extends ServiceController {

  @FXML Text time;
  @FXML Text date;

  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    handeDateTime();
  }

  private void handeDateTime() {
    Thread timeThread =
        new Thread(
            () -> {
              while (Uapp.running) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                String timeStampdate = sdf3.format(timestamp).substring(0, 10);
                String timeStampTime = sdf3.format(timestamp).substring(11);
                time.setText(timeStampTime);
                date.setText(timeStampdate);
              }
            });
    timeThread.start();
    masterThread = timeThread;
  }

  public void toSettings(ActionEvent actionEvent) {
    System.out.println("Going to settings");
  }

  public void logOut(ActionEvent actionEvent) {
    System.out.println("Logging out");
  }

  @Override
  public void addRequest() throws SQLException, IOException {}

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}

  public void openTransport(ActionEvent actionEvent) throws ServiceException {
    DBController.transportApi.run(0, 0, 1920, 1080, "", "CHALL007L2", "CHALL007L2");
  }

  public void toSanitation(ActionEvent actionEvent)
      throws edu.wpi.cs3733.D22.teamD.API.ServiceException {
    DBController.sanitationAPI.run(0, 0, 1920, 1080, "", "CHALL007L2");
  }

  public void toMedicine(ActionEvent actionEvent)
      throws SQLException, IOException, edu.wpi.cs3733.D22.teamU.Exception.ServiceException {
    ArrayList<String> locs = new ArrayList<>();
    for (Location l : Udb.getInstance().locationImpl.locations) locs.add(l.getNodeID());
    MedicineRequest.addLocationIDs(locs);

    Employee test = new Employee("test");
    MedicineRequest.addAuthorizedEmployee("test");
    for (Employee l : Udb.getInstance().EmployeeImpl.hList().values())
      MedicineRequest.addAuthorizedEmployee(l.getEmployeeID());
    MedicineRequest.setCurrentEmployee("test");

    MedicineRequest.run(0, 0, 1920, 1080, "", "CHALL007L2");
  }
}
