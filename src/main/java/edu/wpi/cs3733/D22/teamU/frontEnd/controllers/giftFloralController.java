package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXCheckBox;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.GiftRequest.GiftRequest;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.SneakyThrows;

public class giftFloralController extends ServiceController {

  public ComboBox<String> locations;
  public ComboBox<String> employees;
  @FXML Button clearButton;
  @FXML Button submitButton;
  @FXML VBox requestHolder;
  @FXML Text requestText;
  @FXML StackPane requestsStack;
  @FXML Pane newRequestPane;
  @FXML Pane activeRequestPane;
  @FXML Button newReqButton;
  @FXML Button activeReqButton;
  @FXML Text time;
  @FXML TableView<GiftRequest> activeRequestTable;
  @FXML TableColumn<GiftRequest, String> activeReqID;
  @FXML TableColumn<GiftRequest, String> activeReqName;
  @FXML TableColumn<GiftRequest, String> activePatientName;
  @FXML TableColumn<GiftRequest, String> activeGifts;
  @FXML TableColumn<GiftRequest, String> activeMessage;
  @FXML TableColumn<GiftRequest, String> activeStatus;
  @FXML TableColumn<GiftRequest, String> activeEmployee;
  @FXML TableColumn<GiftRequest, String> activeDestination;
  @FXML TableColumn<GiftRequest, String> activeDate;
  @FXML TableColumn<GiftRequest, String> activeTime;
  @FXML TextField patientName;
  @FXML TextField senderName;
  @FXML TextArea message;
  @FXML ScrollPane miniView;
  @FXML ScrollPane expandedView;
  @FXML Button arrow;
  @FXML Button pushButton;
  @FXML AnchorPane sideBarAnchor;
  @FXML Button sideBarButton;

  ObservableList<JFXCheckBox> checkBoxes = FXCollections.observableArrayList();
  ObservableList<GiftRequest> giftRequests = FXCollections.observableArrayList();
  ArrayList<Location> nodeIDs;
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
    try {
      setUpActiveRequests();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    nodeIDs = new ArrayList<>();
    try {
      for (Location l : Udb.getInstance().locationImpl.list()) {
        nodeIDs.add(l);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    locations.setTooltip(new Tooltip());
    locations.getItems().addAll(nodeIDs);
    new ComboBoxAutoComplete<Location>(locations, 650, 290);

    staff = new ArrayList<>();
    try {
      for (Employee l : Udb.getInstance().EmployeeImpl.hList().values()) {
        staff.add(l);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    employees.setTooltip(new Tooltip());
    employees.getItems().addAll(staff);
    new ComboBoxAutoComplete<Employee>(employees, 675, 380);

    for (Node checkBox : requestHolder.getChildren()) {
      checkBoxes.add((JFXCheckBox) checkBox);
    }

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

    // BooleanBinding submit =locations.idProperty().isEmpty().and(
    // Bindings.createBooleanBinding(checkBoxes.stream().noneMatch(JFXCheckBox::isSelected)));

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

  private void setUpActiveRequests() throws SQLException, IOException {
    activeReqID.setCellValueFactory(new PropertyValueFactory<>("ID"));
    activeReqName.setCellValueFactory(new PropertyValueFactory<>("name"));
    activePatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
    activeGifts.setCellValueFactory(new PropertyValueFactory<>("gifts"));
    activeMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
    activeStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    activeEmployee.setCellValueFactory(new PropertyValueFactory<>("employee"));
    activeDestination.setCellValueFactory(new PropertyValueFactory<>("location"));
    activeDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    activeTime.setCellValueFactory(new PropertyValueFactory<>("time"));
    activeRequestTable.setItems(getActiveRequestList());
  }

  private ObservableList<GiftRequest> newRequest(
      String ID,
      String name, // sender name
      String patientName,
      String gifts,
      String message,
      String status,
      Employee employee,
      String destination,
      String date,
      String time) {
    GiftRequest r =
        new GiftRequest(
            ID, name, patientName, gifts, message, status, employee, destination, date, time);

    r.gettingTheLocation();
    giftRequests.add(r);

    return giftRequests;
  }

  private ObservableList<GiftRequest> getActiveRequestList() throws SQLException, IOException {
    for (GiftRequest giftRequest : Udb.getInstance().giftRequestImpl.hList().values()) {
      GiftRequest r =
          new GiftRequest(
              giftRequest.ID,
              giftRequest.name,
              giftRequest.patientName,
              giftRequest.gifts,
              giftRequest.message,
              giftRequest.status,
              giftRequest.employee,
              giftRequest.destination,
              giftRequest.date,
              giftRequest.time);

      r.gettingTheLocation();
      giftRequests.add(r);
    }
    return giftRequests;
  }

  @Override
  public void addRequest() {
    StringBuilder startRequestString = new StringBuilder("Your request for : ");

    String endRequest = " has been placed successfully";
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    String inputString = "";
    for (int i = 0; i < checkBoxes.size(); i++) {
      if (checkBoxes.get(i).isSelected()) {
        inputString += checkBoxes.get(i).getText() + ":";
      }
    }

    boolean alreadyHere = true;
    String serviceID = "notWork";

    while (alreadyHere) {
      double rand = Math.random() * 10000;

      try {
        alreadyHere = Udb.getInstance().compServRequestImpl.hList().containsKey("GIF" + (int) rand);
      } catch (Exception e) {
        System.out.println(
            "alreadyHere variable messed up in gift and floral service request controller");
      }

      serviceID = "GIF" + (int) rand;
    }

    GiftRequest request =
        new GiftRequest(
            serviceID,
            senderName.getText(),
            patientName.getText(),
            inputString,
            message.getText(),
            "In Progress",
            employees.getValue(),
            locations.getValue().getNodeID(),
            sdf3.format(timestamp).substring(0, 10),
            sdf3.format(timestamp).substring(11));

    request.gettingTheLocation();

    activeRequestTable.setItems(
        newRequest(
            request.getID(),
            request.getName(),
            request.getPatientName(),
            request.getGifts(),
            request.getMessage(),
            request.getStatus(),
            request.getEmployee(),
            request.getDestination(),
            request.getDate(),
            request.getTime()));
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
    GiftRequest request = activeRequestTable.getSelectionModel().getSelectedItem();
    giftRequests.remove(request);
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
    GiftRequest oldRequest = activeRequestTable.getSelectionModel().getSelectedItem();
    newCon.updateRequest();
    GiftRequest request = (GiftRequest) newCon.getRequest();
    request.gettingTheLocation();
    giftRequests.remove(oldRequest);
    giftRequests.add(request);
    activeRequestTable.setItems(giftRequests);
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
    for (int i = 0; i < checkBoxes.size(); i++) {
      checkBoxes.get(i).setSelected(false);
    }
    requestText.setText("Cleared Requests!");
    requestText.setVisible(true);
    new Thread(
            () -> {
              try {
                Thread.sleep(1500); // milliseconds
                Platform.runLater(
                    () -> {
                      message.setText("");
                      senderName.setText("");
                      patientName.setText("");
                    });
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
    Node active = stackNodes.get(stackNodes.indexOf(activeRequestPane));
    for (Node node : stackNodes) {
      node.setVisible(false);
    }
    active.setVisible(true);
    active.toBack();
    activeReqButton.setUnderline(true);
    newReqButton.setUnderline(false);

    // =====edit and remove buttons=====
    editButton.setVisible(true);
    removeButton.setVisible(true);
    // ====================================
  }

  public void toHelp(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/giftFloralHelp.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
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
