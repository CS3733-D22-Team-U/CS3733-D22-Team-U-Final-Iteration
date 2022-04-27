package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Equipment.Equipment;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.EquipRequest.EquipRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import edu.wpi.cs3733.D22.teamU.frontEnd.javaFXObjects.ComboBoxAutoComplete;
import edu.wpi.cs3733.D22.teamU.frontEnd.services.equipmentDelivery.EquipmentUI;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.SneakyThrows;

public class EquipmentDeliverySystemController extends ServiceController {

  public ComboBox<Location> locations;

  public ComboBox<Employee> employees;

  @FXML TableColumn<EquipmentUI, String> nameCol;

  @FXML TableColumn<EquipmentUI, Integer> inUse;

  @FXML TableColumn<EquipmentUI, Integer> available;

  @FXML TableColumn<EquipmentUI, Integer> total;

  @FXML TableColumn<EquipmentUI, String> location;

  @FXML TableView<EquipmentUI> table;

  @FXML VBox requestHolder;

  @FXML Text requestText;

  @FXML Button clearButton;

  @FXML Button submitButton;

  @FXML TableColumn<EquipRequest, String> activeReqID;

  @FXML TableColumn<EquipRequest, String> activeReqName;

  @FXML TableColumn<EquipRequest, Integer> activeReqAmount;

  @FXML TableColumn<EquipRequest, String> activeReqStatus;

  @FXML TableColumn<EquipRequest, String> activeReqEmployee;

  @FXML TableColumn<EquipRequest, String> activeReqDestination;

  @FXML TableColumn<EquipRequest, String> activeDate;

  @FXML TableColumn<EquipRequest, String> activeTime;

  @FXML TableColumn<EquipRequest, Integer> activePriority;

  @FXML TableView<EquipRequest> activeRequestTable;

  @FXML VBox inputFields;

  @FXML StackPane requestsStack;

  @FXML Pane newRequestPane;

  @FXML Pane allEquipPane;

  @FXML Pane activeRequestPane;

  @FXML Button newReqButton;

  @FXML Button activeReqButton;

  @FXML Button allEquipButton;

  @FXML Text time;
  @FXML AnchorPane sideBarAnchor;
  @FXML Button sideBarButton;

  ObservableList<EquipmentUI> equipmentUI = FXCollections.observableArrayList();

  ObservableList<JFXCheckBox> checkBoxes = FXCollections.observableArrayList();

  ObservableList<JFXTextArea> checkBoxesInput = FXCollections.observableArrayList();

  ObservableList<EquipRequest> equipmentRequests = FXCollections.observableArrayList();

  // Udb udb;

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

    // super.initialize(location, resources);

    // udb = Udb.getInstance();

    setUpAllEquipment();

    setUpActiveRequests();

    nodeIDs = new ArrayList<>();

    for (Location l : Udb.getInstance().locationImpl.list()) {

      nodeIDs.add(l);
    }

    locations.setTooltip(new Tooltip());

    locations.getItems().addAll(nodeIDs);

    new ComboBoxAutoComplete<Location>(locations, 650, 290);

    staff = new ArrayList<>();

    for (Employee l : Udb.getInstance().EmployeeImpl.hList().values()) {

      staff.add(l);
    }

    employees.setTooltip(new Tooltip());

    employees.getItems().addAll(staff);

    new ComboBoxAutoComplete<Employee>(employees, 675, 380);

    for (Node checkBox : requestHolder.getChildren()) {

      checkBoxes.add((JFXCheckBox) checkBox);
    }

    for (Node textArea : inputFields.getChildren()) {

      checkBoxesInput.add((JFXTextArea) textArea);
    }

    for (int i = 0; i < checkBoxesInput.size(); i++) {

      int finalI = i;

      checkBoxesInput
          .get(i)
          .disableProperty()
          .bind(
              Bindings.createBooleanBinding(
                  () -> !checkBoxes.get(finalI).isSelected(),
                  checkBoxes.stream().map(CheckBox::selectedProperty).toArray(Observable[]::new)));
    }

    clearButton
        .disableProperty()
        .bind(
            Bindings.createBooleanBinding(
                () -> checkBoxes.stream().noneMatch(JFXCheckBox::isSelected),
                checkBoxes.stream().map(JFXCheckBox::selectedProperty).toArray(Observable[]::new)));

    // BooleanBinding submit =locations.idProperty().isEmpty().and(

    // Bindings.createBooleanBinding(checkBoxes.stream().noneMatch(JFXCheckBox::isSelected)));

    submitButton
        .disableProperty()
        .bind(
            Bindings.createBooleanBinding(
                () -> checkBoxes.stream().noneMatch(JFXCheckBox::isSelected),
                checkBoxes.stream().map(JFXCheckBox::selectedProperty).toArray(Observable[]::new)));

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

  public void toEquipHelp(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/equipmentHelp.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }

  private void setUpAllEquipment() throws SQLException, IOException {

    nameCol.setCellValueFactory(new PropertyValueFactory<EquipmentUI, String>("equipmentName"));

    inUse.setCellValueFactory(new PropertyValueFactory<EquipmentUI, Integer>("amountInUse"));

    available.setCellValueFactory(
        new PropertyValueFactory<EquipmentUI, Integer>("amountAvailable"));

    total.setCellValueFactory(new PropertyValueFactory<EquipmentUI, Integer>("totalAmount"));

    location.setCellValueFactory(new PropertyValueFactory<EquipmentUI, String>("location"));

    table.setItems(getEquipmentList());
  }

  private void setUpActiveRequests() throws SQLException, IOException {

    activeReqID.setCellValueFactory(new PropertyValueFactory<EquipRequest, String>("ID"));

    activeReqName.setCellValueFactory(new PropertyValueFactory<EquipRequest, String>("name"));

    activeReqAmount.setCellValueFactory(new PropertyValueFactory<EquipRequest, Integer>("amount"));

    activeReqStatus.setCellValueFactory(new PropertyValueFactory<EquipRequest, String>("status"));

    activeReqEmployee.setCellValueFactory(
        new PropertyValueFactory<EquipRequest, String>("employee"));

    activeReqDestination.setCellValueFactory(
        new PropertyValueFactory<EquipRequest, String>("location"));

    activeDate.setCellValueFactory(new PropertyValueFactory<EquipRequest, String>("date"));

    activeTime.setCellValueFactory(new PropertyValueFactory<EquipRequest, String>("time"));

    activePriority.setCellValueFactory(new PropertyValueFactory<EquipRequest, Integer>("priority"));

    activeRequestTable.setItems(getActiveRequestList());
  }

  private ObservableList<EquipRequest> newRequest(
      String id,
      String name,
      int amount,
      String typeOfRequest,
      String status,
      Employee employee,
      String destination,
      String date,
      String time,
      int priority) {

    EquipRequest r =
        new EquipRequest(
            id, name, amount, typeOfRequest, status, employee, destination, date, time, priority);

    r.gettingTheLocation();
    equipmentRequests.add(r);

    return equipmentRequests;
  }

  private ObservableList<EquipmentUI> getEquipmentList() throws SQLException, IOException {

    equipmentUI.clear();

    for (Equipment equipment : Udb.getInstance().EquipmentImpl.EquipmentList) {

      equipmentUI.add(
          new EquipmentUI(
              equipment.getName(),
              equipment.getInUse(),
              equipment.getAvailable(),
              equipment.getAmount(),
              equipment.getLocationID()));
    }

    return equipmentUI;
  }

  private ObservableList<EquipRequest> getActiveRequestList() throws SQLException, IOException {

    for (EquipRequest equipRequest : Udb.getInstance().equipRequestImpl.hList().values()) {

      EquipRequest r =
          new EquipRequest(
              equipRequest.getID(),
              equipRequest.getName(),
              equipRequest.getAmount(),
              equipRequest.getTypeOfRequest(),
              equipRequest.getStatus(),
              equipRequest.getEmployee(),
              equipRequest.getDestination(),
              equipRequest.getDate(),
              equipRequest.getTime(),
              equipRequest.getPriority());

      r.gettingTheLocation();

      equipmentRequests.add(r);
    }

    return equipmentRequests;
  }

  @Override
  public void addRequest() {

    StringBuilder startRequestString = new StringBuilder("Your request for : ");

    String endRequest = " has been placed successfully";

    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    int requestAmount = 0;

    for (int i = 0; i < checkBoxes.size(); i++) {

      if (checkBoxes.get(i).isSelected()) {

        String inputString = "";

        if (checkBoxesInput.get(i).getText().trim().equals("")) {

          inputString = "0";

        } else {

          inputString = checkBoxesInput.get(i).getText().trim();
        }

        String room = locations.getValue().getNodeID();

        requestAmount = Integer.parseInt(inputString);

        startRequestString
            .append(requestAmount)
            .append(" ")
            .append(checkBoxes.get(i).getText())
            .append("(s) to room ")
            .append(room)
            .append(", ");

        boolean alreadyHere = true;
        String serviceID = "notWork";

        while (alreadyHere) {
          double rand = Math.random() * 10000;

          try {
            alreadyHere =
                Udb.getInstance().compServRequestImpl.hList().containsKey("EQU" + (int) rand);
          } catch (Exception e) {
            System.out.println(
                "alreadyHere variable messed up in equip service request controller");
          }

          serviceID = "EQU" + (int) rand;
        }

        EquipRequest request =
            new EquipRequest(
                serviceID,
                checkBoxes.get(i).getText(),
                requestAmount,
                null,
                "In Progress",
                employees.getValue(),
                room,
                sdf3.format(timestamp).substring(0, 10),
                sdf3.format(timestamp).substring(11),
                1);

        request.gettingTheLocation();

        activeRequestTable.setItems(
            newRequest(
                request.getID(),
                request.getName(),
                request.getAmount(),
                request.getTypeOfRequest(),
                request.getStatus(),
                request.getEmployee(),
                request.getDestination(),
                request.getDate(),
                request.getTime(),
                request.getPriority()));

        try {

          Udb.getInstance()
              .add( // TODO Have random ID and enter Room Destination
                  new EquipRequest(
                      request.getID(),
                      request.getName(),
                      request.getAmount(),
                      request.getTypeOfRequest(),
                      request.getStatus(),
                      request.getEmployee(),
                      request.getDestination(),
                      request.getDate(),
                      request.getTime(),
                      request.getPriority()));

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

  // ======remove edit request=============
  @Override
  public void removeRequest() {
    // ---CHANGE---
    EquipRequest request = activeRequestTable.getSelectionModel().getSelectedItem();
    equipmentRequests.remove(request);
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
    EquipRequest oldRequest = activeRequestTable.getSelectionModel().getSelectedItem();
    newCon.updateRequest();
    EquipRequest request = (EquipRequest) newCon.getRequest();
    request.gettingTheLocation();
    equipmentRequests.remove(oldRequest);
    equipmentRequests.add(request);
    activeRequestTable.setItems(equipmentRequests);
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

      checkBoxesInput.get(i).clear();
    }

    requestText.setText("Cleared Requests!");

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

    allEquipButton.setUnderline(false);
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

    allEquipButton.setUnderline(false);
    // =====edit and remove buttons=====
    editButton.setVisible(true);
    removeButton.setVisible(true);
    // ====================================
  }

  public void switchToEquipment(ActionEvent actionEvent) {

    ObservableList<Node> stackNodes = requestsStack.getChildren();

    Node active = stackNodes.get(stackNodes.indexOf(allEquipPane));

    for (Node node : stackNodes) {

      node.setVisible(false);
    }

    active.setVisible(true);

    active.toBack();

    activeReqButton.setUnderline(false);

    newReqButton.setUnderline(false);

    allEquipButton.setUnderline(true);
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
