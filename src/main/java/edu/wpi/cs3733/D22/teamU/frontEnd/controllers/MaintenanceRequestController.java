package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MaintenanceRequest.MaintenanceRequest;
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

public class MaintenanceRequestController extends ServiceController {

  public ComboBox<Location> locations;
  public ComboBox<Employee> staffDropDown;
  @FXML Text requestText;
  @FXML Button clearButton;
  @FXML Button submitButton;

  // these are for the table attributes shown to the user
  @FXML TableColumn<MaintenanceRequest, String> activeReqID;
  @FXML TableColumn<MaintenanceRequest, String> activeReqStatus;
  @FXML TableColumn<MaintenanceRequest, String> activeReqDestination;
  @FXML TableColumn<MaintenanceRequest, String> activeReqDescription;
  @FXML TableColumn<MaintenanceRequest, String> activeStaff;
  @FXML TableColumn<MaintenanceRequest, String> activeDate;
  @FXML TableColumn<MaintenanceRequest, String> activeTime;

  @FXML TableView<MaintenanceRequest> activeRequestTable;

  @FXML StackPane requestsStack;
  @FXML Pane newRequestPane;
  @FXML Pane activeRequestPane;
  @FXML Button newReqButton;
  @FXML Button activeReqButton;
  @FXML Text time;
  @FXML Text sucessRequest;
  @FXML Text clearRequest;
  @FXML Text missingDescription;
  @FXML AnchorPane sideBarAnchor;
  @FXML Button sideBarButton;

  @FXML TextArea textInput;

  // =========declare buttons, popup pane and controller===========
  RequestEditController newCon;
  AnchorPane EditRequestPopUp;
  @FXML Button editButton;
  @FXML Button closeButton;
  @FXML Button submitEditButton;
  @FXML Button removeButton;
  // ================================================

  ObservableList<MaintenanceRequest> maintenanceRequests = FXCollections.observableArrayList();
  ArrayList<Location> nodeIDs;
  ArrayList<Employee> staff;
  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  public void fillDestinations() throws SQLException, IOException {
    nodeIDs = new ArrayList<>();
    for (Location l : Udb.getInstance().locationImpl.list()) {
      nodeIDs.add(l);
    }
    locations.setTooltip(new Tooltip());
    locations.getItems().addAll(nodeIDs);
    new ComboBoxAutoComplete<Location>(locations, 650, 290);
  }

  public void fillStaff() throws SQLException, IOException {
    staff = new ArrayList<>();
    for (Employee l : Udb.getInstance().EmployeeImpl.hList().values()) {
      staff.add(l);
    }

    staffDropDown.setTooltip(new Tooltip());
    staffDropDown.getItems().addAll(staff);
    new ComboBoxAutoComplete<Employee>(staffDropDown, 675, 400);
  }

  @SneakyThrows
  @Override
  public void initialize(URL location, ResourceBundle resources) {
    sucessRequest.setVisible(false);
    clearRequest.setVisible(false);
    missingDescription.setVisible(false);

    setUpAllMaintenance();

    fillDestinations();
    fillStaff();

    handleTime();
    handleBar();
  }

  public void toMaintenanceHelp(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/maintenanceHelp.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
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

  private void setUpAllMaintenance() throws SQLException, IOException {

    activeReqID.setCellValueFactory(new PropertyValueFactory<MaintenanceRequest, String>("ID"));
    activeReqStatus.setCellValueFactory(
        new PropertyValueFactory<MaintenanceRequest, String>("status"));
    activeReqDestination.setCellValueFactory(
        new PropertyValueFactory<MaintenanceRequest, String>("location"));
    activeReqDescription.setCellValueFactory(
        new PropertyValueFactory<MaintenanceRequest, String>("description"));
    activeStaff.setCellValueFactory(
        new PropertyValueFactory<MaintenanceRequest, String>("employee"));
    activeDate.setCellValueFactory(new PropertyValueFactory<MaintenanceRequest, String>("date"));
    activeTime.setCellValueFactory(new PropertyValueFactory<MaintenanceRequest, String>("time"));

    activeRequestTable.setItems(getMaintenanceRequestsList());
  }

  private ObservableList<MaintenanceRequest> newRequest(
      String id,
      String name,
      String status,
      String destination,
      Employee employee,
      String typeOfMaintenanceRequest,
      String description,
      String date,
      String time) {
    MaintenanceRequest r =
        new MaintenanceRequest(
            id,
            name,
            status,
            destination,
            employee,
            typeOfMaintenanceRequest,
            description,
            date,
            time);
    r.gettingTheLocation();
    maintenanceRequests.add(r);

    return maintenanceRequests;
  }

  private ObservableList<MaintenanceRequest> getMaintenanceRequestsList()
      throws SQLException, IOException {
    maintenanceRequests.clear();
    for (MaintenanceRequest maintenanceReq :
        Udb.getInstance().maintenanceRequestImpl.List.values()) {

      MaintenanceRequest r =
          new MaintenanceRequest(
              maintenanceReq.getID(),
              maintenanceReq.getName(),
              maintenanceReq.getStatus(),
              maintenanceReq.getDestination(),
              maintenanceReq.getEmployee(),
              maintenanceReq.getTypeOfMaintenance(),
              maintenanceReq.getDescription(),
              maintenanceReq.getDate(),
              maintenanceReq.getTime());

      r.gettingTheLocation();
      maintenanceRequests.add(r);
    }

    return maintenanceRequests;
  }

  private ObservableList<MaintenanceRequest> getActiveMaintenanceRequestList()
      throws SQLException, IOException {
    for (MaintenanceRequest maintenanceReq :
        Udb.getInstance().maintenanceRequestImpl.List.values()) {
      maintenanceRequests.add(
          new MaintenanceRequest(
              maintenanceReq.getID(),
              maintenanceReq.getName(),
              maintenanceReq.getStatus(),
              maintenanceReq.getDestination(),
              maintenanceReq.getEmployee(),
              maintenanceReq.getTypeOfMaintenance(),
              maintenanceReq.getDescription(),
              maintenanceReq.getDate(),
              maintenanceReq.getTime()));
    }
    return maintenanceRequests;
  }

  @Override
  public void addRequest() {
    if (textInput.getText().equals("")) {
      missingDescription.setVisible(true);
      new Thread(
              () -> {
                try {
                  Thread.sleep(3500); // milliseconds
                  Platform.runLater(
                      () -> {
                        missingDescription.setVisible(false);
                      });
                } catch (InterruptedException ie) {
                }
              })
          .start();
      return;
    }

    clearRequest.setVisible(false);
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    boolean alreadyHere = true;
    String serviceID = "notWork";

    // makes the id
    while (alreadyHere) {
      double rand = Math.random() * 10000;

      try {
        alreadyHere = Udb.getInstance().compServRequestImpl.hList().containsKey("MAI" + (int) rand);
      } catch (Exception e) {
        System.out.println(
            "alreadyHere variable messed up in maintenance service request controller");
      }

      serviceID = "MAI" + (int) rand;
    }

    // String empty = staffDropDown.getValue();

    MaintenanceRequest request =
        new MaintenanceRequest(
            serviceID,
            "N/A",
            "Pending",
            locations.getValue().getNodeID(),
            staffDropDown.getValue(),
            "N/A",
            textInput.getText().trim(),
            sdf3.format(timestamp).substring(0, 10),
            sdf3.format(timestamp).substring(11));

    request.gettingTheLocation();
    activeRequestTable.setItems(
        newRequest(
            request.getID(),
            request.getName(),
            request.getStatus(),
            request.getDestination(),
            request.getEmployee(),
            request.getTypeOfMaintenance(),
            request.getDescription(),
            request.getDate(),
            request.getTime()));
    try {

      Udb.getInstance().add(request);
    } catch (IOException e) {
      e.printStackTrace();
      System.out.printf("print first catch");
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.printf("print second catch");
    }

    sucessRequest.setVisible(true);
    new Thread(
            () -> {
              try {
                Thread.sleep(3500); // milliseconds
                Platform.runLater(
                    () -> {
                      sucessRequest.setVisible(false);
                    });
              } catch (InterruptedException ie) {
              }
            })
        .start();
  }

  // ======remove edit request=============
  @Override
  public void removeRequest() {
    // ---CHANGE---
    MaintenanceRequest request = activeRequestTable.getSelectionModel().getSelectedItem();
    maintenanceRequests.remove(request);
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
    MaintenanceRequest oldRequest = activeRequestTable.getSelectionModel().getSelectedItem();
    newCon.updateRequest();
    MaintenanceRequest request = (MaintenanceRequest) newCon.getRequest();
    request.gettingTheLocation();
    maintenanceRequests.remove(oldRequest);
    maintenanceRequests.add(request);
    activeRequestTable.setItems(maintenanceRequests);
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
    sucessRequest.setVisible(false);
    clearRequest.setVisible(true);
    textInput.setText("");
    staffDropDown.getSelectionModel().clearSelection();
    locations.getSelectionModel().clearSelection();
    new Thread(
            () -> {
              try {
                Thread.sleep(1500); // milliseconds
                Platform.runLater(
                    () -> {
                      clearRequest.setVisible(false);
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
    Node active = stackNodes.get(stackNodes.indexOf(activeRequestPane));
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
    if (activeRequestTable.getSelectionModel().getSelectedItem() != null) {

      submitEditButton.setVisible(true);
      closeButton.setVisible(true);
      EditRequestPopUp.setVisible(true);
      Pane pane = (Pane) editButton.getParent();
      if (!pane.getChildren().contains(EditRequestPopUp)) {
        pane.getChildren().add(EditRequestPopUp);
      }
      newCon.setUp(activeRequestTable.getSelectionModel().getSelectedItem());
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
    activeRequestTable.getSelectionModel().clearSelection();
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
