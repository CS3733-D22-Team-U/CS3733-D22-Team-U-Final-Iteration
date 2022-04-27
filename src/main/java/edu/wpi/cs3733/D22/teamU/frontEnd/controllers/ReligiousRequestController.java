package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.ReligiousRequest.ReligiousRequest;
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
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.SneakyThrows;

public class ReligiousRequestController extends ServiceController {

  public ComboBox<Location> locations;
  public ComboBox<Employee> employees;

  @FXML TableColumn<ReligiousRequest, String> nameID;
  @FXML TableColumn<ReligiousRequest, String> name;
  @FXML TableColumn<ReligiousRequest, String> date;
  @FXML TableColumn<ReligiousRequest, String> newTime;
  @FXML TableColumn<ReligiousRequest, String> patientName;
  @FXML TableColumn<ReligiousRequest, String> religion;
  @FXML TableColumn<ReligiousRequest, String> status;
  @FXML TableColumn<ReligiousRequest, String> destination;
  @FXML TableColumn<ReligiousRequest, String> employee;
  @FXML TableColumn<ReligiousRequest, String> notes;
  @FXML TableView<ReligiousRequest> table;

  @FXML Button clearButton;
  @FXML Button submitButton;

  @FXML Text requestText;
  @FXML StackPane requestsStack;
  @FXML Pane newRequestPane;
  @FXML Pane allActiveRequestsPane;

  @FXML Button newReqButton;
  @FXML Button activeReqButton;
  @FXML TextArea inputName;
  @FXML TextArea inputPatient;
  @FXML TextArea inputReligion;
  @FXML TextArea inputNotes;
  @FXML Text time;
  @FXML AnchorPane sideBarAnchor;
  @FXML Button sideBarButton;

  ObservableList<ReligiousRequest> religiousUIRequests = FXCollections.observableArrayList();
  // Udb udb;
  ArrayList<Location> nodes;
  ArrayList<Employee> staff;
  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  // =========declare buttons, popup pane and controller===========
  RequestEditController newCon;
  AnchorPane EditRequestPopUp;
  @FXML Button editButton;
  @FXML Button closeButton;
  @FXML Button submitEditButton;
  @FXML Button removeButton;
  // ================================================

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    setUpAllReligiousReq();

    requestText.setVisible(false);
    // Displays Locations in Table View
    nodes = new ArrayList<>();
    for (Location l : Udb.getInstance().locationImpl.list()) {
      nodes.add(l);
    }
    locations.setTooltip(new Tooltip());
    locations.getItems().addAll(nodes);
    new ComboBoxAutoComplete<Location>(locations, 650, 290);

    // Displays EMployess in Table View
    staff = new ArrayList<>();
    for (Employee l : Udb.getInstance().EmployeeImpl.hList().values()) {
      staff.add(l);
    }
    employees.setTooltip(new Tooltip());
    employees.getItems().addAll(staff);
    new ComboBoxAutoComplete<Employee>(employees, 675, 380);

    handleTime();
    handleBar();
  }

  private void handleBar() {
    TranslateTransition openNav = new TranslateTransition(new Duration(350), sideBarAnchor);
    openNav.setToY(596);
    TranslateTransition closeNav = new TranslateTransition(new Duration(350), sideBarAnchor);
    sideBarButton.setOnAction(
        (ActionEvent evt) -> {
          if (sideBarAnchor.getTranslateY() != 596) {
            openNav.play();
          } else {
            closeNav.setToY(0);
            closeNav.play();
          }
        });

    // =============initialize fxml and controller=============================
    EditRequestPopUp = new AnchorPane();
    try {
      FXMLLoader loader =
          new FXMLLoader(
              getClass().getResource("/edu/wpi/cs3733/D22/teamU/views/EditRequestPopUp.fxml"));
      EditRequestPopUp = loader.load();
      newCon = (RequestEditController) loader.getController();

      EditRequestPopUp.setLayoutX(100);
      EditRequestPopUp.setLayoutY(200);

    } catch (IOException e) {
      e.printStackTrace();
    }
    // =====================================================================

    // ==============initialize edit stuff visibility ================
    editButton.setVisible(false);
    removeButton.setVisible(false);
    closeButton.setVisible(false);
    submitEditButton.setVisible(false);
    // =========================================
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
    masterThread = timeThread;
  }

  //
  private void setUpAllReligiousReq() throws SQLException, IOException {
    nameID.setCellValueFactory(new PropertyValueFactory("ID"));
    name.setCellValueFactory(new PropertyValueFactory<ReligiousRequest, String>("name"));
    date.setCellValueFactory(new PropertyValueFactory<ReligiousRequest, String>("date"));
    newTime.setCellValueFactory(new PropertyValueFactory<ReligiousRequest, String>("time"));
    patientName.setCellValueFactory(
        new PropertyValueFactory<ReligiousRequest, String>("patientName"));
    religion.setCellValueFactory(new PropertyValueFactory<ReligiousRequest, String>("religion"));
    status.setCellValueFactory(new PropertyValueFactory<ReligiousRequest, String>("status"));
    destination.setCellValueFactory(new PropertyValueFactory<ReligiousRequest, String>("location"));
    employee.setCellValueFactory(new PropertyValueFactory<ReligiousRequest, String>("employee"));
    notes.setCellValueFactory(new PropertyValueFactory<ReligiousRequest, String>("notes"));
    table.setItems(getReligiousList());
  }

  private ObservableList<ReligiousRequest> newRequest(
      String id,
      String name,
      String date,
      String time,
      String patientName,
      String religion,
      String status,
      String destination,
      Employee employee,
      String notes) {
    ReligiousRequest r =
        new ReligiousRequest(
            id, name, date, time, patientName, religion, status, destination, employee, notes);
    r.gettingTheLocation();
    religiousUIRequests.add(r);
    return religiousUIRequests;
  }

  private ObservableList<ReligiousRequest> getReligiousList() throws SQLException, IOException {
    religiousUIRequests.clear();
    for (ReligiousRequest request : Udb.getInstance().religiousRequestImpl.List.values()) {
      ReligiousRequest r =
          new ReligiousRequest(
              request.getID(),
              request.getName(),
              request.getDate(),
              request.getTime(),
              request.getPatientName(),
              request.getReligion(),
              request.getStatus(),
              request.getDestination(),
              request.getEmployee(),
              request.getNotes());
      r.gettingTheLocation();
      religiousUIRequests.add(r);
    }
    return religiousUIRequests;
  }

  public void toReligiousHelp(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/ReligiousHelp.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }

  @Override
  public void addRequest() {
    StringBuilder startRequestString = new StringBuilder("Your request for: ");

    String endRequest = "Has been placed successfully";
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    // String room = locations.getValue().toString();

    // String employ = employees.getValue().toString();

    boolean alreadyHere = true;
    String serviceID = "notWork";

    // makes the id
    while (alreadyHere) {
      double rand = Math.random() * 10000;

      try {
        alreadyHere =
            Udb.getInstance().religiousRequestImpl.hList().containsKey("REL" + (int) rand);
      } catch (Exception e) {
        System.out.println(
            "alreadyHere variable messed up in religious service request controller");
      }

      serviceID = "REL" + (int) rand;
    }

    ReligiousRequest request =
        new ReligiousRequest(
            serviceID,
            inputName.getText().trim(),
            sdf3.format(timestamp).substring(0, 10),
            sdf3.format(timestamp).substring(11),
            inputPatient.getText().trim(),
            inputReligion.getText().trim(),
            "Pending",
            locations.getValue().getNodeID(),
            employees.getValue(),
            inputNotes.getText().trim());

    request.gettingTheLocation();

    table.setItems(
        newRequest(
            request.getID(),
            request.getName(),
            request.getDate(),
            request.getTime(),
            request.getPatientName(),
            request.getReligion(),
            request.getStatus(),
            request.getDestination(),
            request.getEmployee(),
            request.getNotes()));
    try {
      Udb.getInstance().add(request);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // ======remove edit request=============
  @Override
  public void removeRequest() {
    // ---CHANGE---
    ReligiousRequest request = table.getSelectionModel().getSelectedItem();
    religiousUIRequests.remove(request);
    // -----------
    try {
      Udb.getInstance().remove(request);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    closeEdit();
  }
  // ====================================

  // =============Update the request from edit======================
  @Override
  public void updateRequest() {
    // -----change------------
    ReligiousRequest oldRequest = table.getSelectionModel().getSelectedItem();
    newCon.updateRequest();
    ReligiousRequest request = (ReligiousRequest) newCon.getRequest();
    request.gettingTheLocation();
    religiousUIRequests.remove(oldRequest);
    religiousUIRequests.add(request);
    table.setItems(religiousUIRequests);
    // ----------------------------------------------
    try {
      Udb.getInstance().remove(oldRequest);
      Udb.getInstance().add(request);

    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  // ====================================================

  public void clearRequest() {
    requestText.setVisible(true);
    new Thread(
            () -> {
              try {
                Thread.sleep(1500); // milliseconds
                Platform.runLater(
                    () -> {
                      requestText.setVisible(false);
                    });
              } catch (InterruptedException ie) {
              }
            })
        .start();
  }

  public Employee checkEmployee(String employee) throws NullPointerException {
    if (EmployeeDaoImpl.List.get(employee) != null) {
      return EmployeeDaoImpl.List.get(employee);
    } else {
      Employee empty = new Employee("N/A");
      return empty;
    }
  }

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

    // =========edit and remove buttons========
    editButton.setVisible(false);
    removeButton.setVisible(false);
    closeButton.setVisible(false);
    submitEditButton.setVisible(false);
    EditRequestPopUp.setVisible(false);
    // =====================================
  }

  public void switchToActive(ActionEvent actionEvent) {
    ObservableList<Node> stackNodes = requestsStack.getChildren();
    Node active = stackNodes.get(stackNodes.indexOf(allActiveRequestsPane));
    for (Node node : stackNodes) {
      node.setVisible(false);
    }
    active.setVisible(true);
    active.toBack();
    activeReqButton.setUnderline(true);

    newReqButton.setUnderline(false);

    // =====edit and remove buttons=====
    editButton.setVisible(Udb.admin);
    removeButton.setVisible(Udb.admin);
    // ====================================
  }

  public void mouseHovered(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: #E6F6F7");
  }

  public void mouseExit(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: transparent");
  }

  // ==========edit button==========================
  public void editClick(MouseEvent event) {
    if (table.getSelectionModel().getSelectedItem() != null) {

      submitEditButton.setVisible(true);
      closeButton.setVisible(true);
      EditRequestPopUp.setVisible(true);
      Pane pane = (Pane) editButton.getParent();
      if (!pane.getChildren().contains(EditRequestPopUp)) {
        pane.getChildren().add(EditRequestPopUp);
      }
      newCon.setUp(table.getSelectionModel().getSelectedItem());
    }
  }
  // ==============================================

  // =======submit edit button===========
  public void submitEdit(MouseEvent event) {
    this.updateRequest();
    closeEdit();
  }
  // =====================================

  // =====close edit pane===================
  public void closeEdit() {
    table.getSelectionModel().clearSelection();
    EditRequestPopUp.setVisible(false);
    submitEditButton.setVisible(false);
    closeButton.setVisible(false);
  }
  // ======================================

  // ====remove req ======
  public void editRemoveReq() {
    removeRequest();
  }
  // =========================
}
