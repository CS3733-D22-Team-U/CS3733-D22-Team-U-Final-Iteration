package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.API.MedicineRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.DBController;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import edu.wpi.cs3733.D22.teamZ.api.exception.ServiceException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class APIPageController extends ServiceController {

  @FXML Text time;
  @FXML Text date;
  @FXML DatePicker datePicker;
  @FXML Pane datePickerPane;
  @FXML Button equipmentButton;
  @FXML Button medicineButton;
  @FXML Button labButton;
  @FXML Button mealButton;
  @FXML Button giftButton;
  @FXML Button laundryButton;
  @FXML Button compButton;
  @FXML Button translateButton;
  @FXML Button mainButton;
  @FXML Button religiousButton;
  @FXML Button securityButton;

  private static final String HOVERED_BUTTON = "-fx-border-color: #5898DB";

  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    super.initialize(location, resources);

    datePicker = new DatePicker(LocalDate.now());
    DatePickerSkin datePickerSkin = new DatePickerSkin(datePicker);

    Node popupContent = datePickerSkin.getPopupContent();
    datePicker.setVisible(false);
    datePickerPane.getChildren().add(popupContent);
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

  public void openTransport(ActionEvent actionEvent) throws ServiceException, IOException {
    /*
    File folder = new File("cssAPI");
    folder.mkdir();
    InputStream csvEdges =
        Main.class
            .getClassLoader()
            .getResourceAsStream("edu/wpi/cs3733/D22/teamU/css/API/style.css");
    String edges = DBController.copyFile(csvEdges, "cssAPI/style.css");

     */
    DBController.transportApi.run(0, 0, 1920, 1080, "", "CHALL007L2", "CHALL007L2");
  }

  public void toSanitation(ActionEvent actionEvent)
      throws edu.wpi.cs3733.D22.teamD.API.ServiceException {
    DBController.sanitationAPI.run(0, 0, 1920, 1080, "", "CHALL007L2");
  }

  public void toMedicine(ActionEvent actionEvent)
      throws SQLException, IOException, edu.wpi.cs3733.D22.teamU.API.Exception.ServiceException {
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

  public void showText() {
    String initStyle = equipmentButton.getStyle();
    equipmentButton.setOnMouseEntered(e -> equipmentButton.setStyle(initStyle + HOVERED_BUTTON));
    equipmentButton.setOnMouseExited(e -> equipmentButton.setStyle(initStyle));
    mealButton.setOnMouseEntered(e -> mealButton.setStyle(initStyle + HOVERED_BUTTON));
    mealButton.setOnMouseExited(e -> mealButton.setStyle(initStyle));
    labButton.setOnMouseEntered(e -> labButton.setStyle(initStyle + HOVERED_BUTTON));
    labButton.setOnMouseExited(e -> labButton.setStyle(initStyle));
    giftButton.setOnMouseEntered(e -> giftButton.setStyle(initStyle + HOVERED_BUTTON));
    giftButton.setOnMouseExited(e -> giftButton.setStyle(initStyle));
    religiousButton.setOnMouseEntered(e -> religiousButton.setStyle(initStyle + HOVERED_BUTTON));
    religiousButton.setOnMouseExited(e -> religiousButton.setStyle(initStyle));
    translateButton.setOnMouseEntered(e -> translateButton.setStyle(initStyle + HOVERED_BUTTON));
    translateButton.setOnMouseExited(e -> translateButton.setStyle(initStyle));
    mainButton.setOnMouseEntered(e -> mainButton.setStyle(initStyle + HOVERED_BUTTON));
    mainButton.setOnMouseExited(e -> mainButton.setStyle(initStyle));
    securityButton.setOnMouseEntered(e -> securityButton.setStyle(initStyle + HOVERED_BUTTON));
    securityButton.setOnMouseExited(e -> securityButton.setStyle(initStyle));
    medicineButton.setOnMouseEntered(e -> medicineButton.setStyle(initStyle + HOVERED_BUTTON));
    medicineButton.setOnMouseExited(e -> medicineButton.setStyle(initStyle));
    laundryButton.setOnMouseEntered(e -> laundryButton.setStyle(initStyle + HOVERED_BUTTON));
    laundryButton.setOnMouseExited(e -> laundryButton.setStyle(initStyle));
    compButton.setOnMouseEntered(e -> compButton.setStyle(initStyle + HOVERED_BUTTON));
    compButton.setOnMouseExited(e -> compButton.setStyle(initStyle));
  }
}
