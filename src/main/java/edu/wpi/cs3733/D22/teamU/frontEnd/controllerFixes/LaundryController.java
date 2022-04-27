package edu.wpi.cs3733.D22.teamU.frontEnd.controllerFixes;

import com.jfoenix.controls.JFXCheckBox;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.LaundryRequest.LaundryRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import edu.wpi.cs3733.D22.teamU.frontEnd.controllers.ServiceController;
import edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects.ComboBoxAutoComplete;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.SneakyThrows;

public class LaundryController extends ServiceController {
  @FXML VBox requestHolder;
  @FXML Pane activeRequestPane;
  @FXML StackPane requestsStack;
  @FXML Pane newRequestPane;
  @FXML Button newReqButton;
  @FXML Button activeReqButton;
  @FXML ComboBox<Location> locations;
  @FXML ComboBox<Employee> employees;
  @FXML Button submitButton;
  @FXML Button clearButton;
  @FXML Text time;
  @FXML DatePicker pickupDateInput;
  @FXML DatePicker dropOffDateInput;
  @FXML Text requestText;
  @FXML TableColumn<LaundryRequest, String> activeReqID;
  @FXML TableColumn<LaundryRequest, String> patientName;
  @FXML TableColumn<LaundryRequest, String> staffName;
  @FXML TableColumn<LaundryRequest, String> serviceType;
  @FXML TableColumn<LaundryRequest, String> location;
  @FXML TableColumn<LaundryRequest, String> pickUp;
  @FXML TableColumn<LaundryRequest, String> dropOff;

  @FXML TableView<LaundryRequest> activeRequestTable;

  @FXML TextField patientNameInput;
  ObservableList<LaundryRequest> laundryRequests = FXCollections.observableArrayList();
  ObservableList<JFXCheckBox> checkBoxes = FXCollections.observableArrayList();
  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  ArrayList<String> nodeIDs;
  ArrayList<String> staff;

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    setUpActiveRequests();
    for (Node checkBox : requestHolder.getChildren()) {
      checkBoxes.add((JFXCheckBox) checkBox);
    }

    locations.setTooltip(new Tooltip());
    locations.getItems().addAll(Udb.getInstance().locationImpl.locations);
    new ComboBoxAutoComplete<Location>(locations, 650, 290);

    employees.setTooltip(new Tooltip());
    employees.getItems().addAll(Udb.getInstance().EmployeeImpl.hList().values());
    new ComboBoxAutoComplete<Employee>(employees, 675, 380);

    clearButton
        .disableProperty()
        .bind(
            Bindings.createBooleanBinding(
                () -> checkBoxes.stream().noneMatch(JFXCheckBox::isSelected),
                checkBoxes.stream().map(JFXCheckBox::selectedProperty).toArray(Observable[]::new)));
    submitButton
        .disableProperty()
        .bind(
            Bindings.createBooleanBinding(
                () -> checkBoxes.stream().noneMatch(JFXCheckBox::isSelected),
                checkBoxes.stream().map(JFXCheckBox::selectedProperty).toArray(Observable[]::new)));
    handleTime();

    pickupDateInput.setDayCellFactory(
        picker ->
            new DateCell() {
              public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0);
              }
            });

    dropOffDateInput.setDayCellFactory(
        picker ->
            new DateCell() {
              public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();

                setDisable(empty || date.compareTo(today) < 0);
              }
            });
  }

  private void setUpActiveRequests() throws SQLException, IOException {
    activeReqID.setCellValueFactory(new PropertyValueFactory<>("ID"));
    patientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
    staffName.setCellValueFactory(new PropertyValueFactory<>("employee"));
    serviceType.setCellValueFactory(new PropertyValueFactory<>("services"));
    location.setCellValueFactory(new PropertyValueFactory<>("location"));
    pickUp.setCellValueFactory(new PropertyValueFactory<>("pickUpDate"));
    dropOff.setCellValueFactory(new PropertyValueFactory<>("dropOffDate"));

    activeRequestTable.setItems(getActiveRequestList());
  }

  @SneakyThrows
  private ObservableList<LaundryRequest> getActiveRequestList() {
    for (LaundryRequest e : Udb.getInstance().laundryRequestImpl.hList().values()) {
      e.gettingTheLocation();
    }
    laundryRequests.addAll(Udb.getInstance().laundryRequestImpl.hList().values());
    return laundryRequests;
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

  @Override
  public void addRequest() {
    StringBuilder startRequestString = new StringBuilder("Your request for : ");

    String endRequest = " has been placed successfully";
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    for (int i = 0; i < checkBoxes.size(); i++) {
      if (checkBoxes.get(i).isSelected()) {
        String inputString = "";

        String room = locations.getValue().toString();

        startRequestString
            .append(" ")
            .append(checkBoxes.get(i).getText())
            .append("(s) to room ")
            .append(room)
            .append(", ");

        boolean alreadyHere = true;
        String serviceID = "notWork";

        // makes the id
        while (alreadyHere) {
          double rand = Math.random() * 10000;

          try {
            alreadyHere =
                Udb.getInstance().compServRequestImpl.hList().containsKey("LAU" + (int) rand);
          } catch (Exception e) {
            System.out.println(
                "alreadyHere variable messed up in laundry service request controller");
          }

          serviceID = "LAU" + (int) rand;
        }

        LaundryRequest request =
            new LaundryRequest(
                serviceID,
                patientNameInput.getText().trim(),
                employees.getValue(),
                "done",
                locations.getValue().getNodeID(),
                pickupDateInput.getValue().toString(),
                dropOffDateInput.getValue().toString(),
                sdf3.format(timestamp).substring(11),
                checkBoxes.get(i).getText().trim(),
                "N/A");
        request.gettingTheLocation();
        laundryRequests.add(request);
        activeRequestTable.setItems(laundryRequests);
        try {
          Location l =
              Udb.getInstance()
                  .locationImpl
                  .list()
                  .get(Udb.getInstance().locationImpl.search(request.getDestination()));
          LaundryRequest e =
              new LaundryRequest(
                  request.getID(),
                  request.getPatientName(),
                  request.getEmployee(),
                  request.getStatus(),
                  request.getDestination(),
                  request.getPickUpDate(),
                  request.getDropOffDate(),
                  request.getTime(),
                  request.getServices(),
                  request.getNotes());
          e.setLocation(l);
          Udb.getInstance().add(e);

        } catch (IOException e) {
          e.printStackTrace();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }

    requestText.setText(startRequestString + endRequest);
    requestText.setVisible(true);
    new Thread(
            () -> {
              try {
                Thread.sleep(3500); // milliseconds
                Platform.runLater(
                    () -> {
                      requestText.setVisible(false);
                    });
              } catch (InterruptedException ie) {
              }
            })
        .start();
  }

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}

  public void switchToNewRequest(ActionEvent actionEvent) {
    ObservableList<Node> stackNodes = requestsStack.getChildren();
    Node newReq = stackNodes.get(stackNodes.indexOf(newRequestPane));
    for (Node node : stackNodes) {
      node.setVisible(false);
    }
    newReq.setVisible(true);
    newReq.toBack();
    activeReqButton.setUnderline(false);
    newReqButton.setUnderline(true);
  }

  public void switchToActive(ActionEvent actionEvent) {
    ObservableList<Node> stackNodes = requestsStack.getChildren();
    Node active = stackNodes.get(stackNodes.indexOf(activeRequestPane));
    for (Node node : stackNodes) {
      node.setVisible(false);
    }
    active.setVisible(true);
    active.toBack();
    activeReqButton.setUnderline(true);
    newReqButton.setUnderline(false);
  }

  public void mouseHovered(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: #E6F6F7");
  }

  public void mouseExit(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: transparent");
  }

  public Employee checkEmployee(String employee) throws NullPointerException {
    if (EmployeeDaoImpl.List.get(employee) != null) {
      return EmployeeDaoImpl.List.get(employee);
    } else {
      Employee empty = new Employee("N/A");
      return empty;
    }
  }

  public void clearRequest(ActionEvent actionEvent) {}
}
