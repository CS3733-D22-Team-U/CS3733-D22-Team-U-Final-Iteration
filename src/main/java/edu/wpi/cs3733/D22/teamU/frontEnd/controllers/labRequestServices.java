package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.LabRequest.LabRequest;
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
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import lombok.SneakyThrows;

public class labRequestServices extends ServiceController {

  @FXML StackPane requestsStack;
  @FXML Pane newRequestPane;
  @FXML Pane activeRequestPane;
  @FXML Button newReqButton;
  @FXML Button activeReqButton;
  @FXML Button allEquipButton;
  public Button toHelpPage;
  public Button clear;
  public Label submission;
  public ComboBox<Location> locations;
  public ComboBox<Employee> employees;

  @FXML TextField patientNameField;

  @FXML TableColumn<LabRequest, String> activeReqID;
  @FXML TableColumn<LabRequest, String> activeReqType;
  @FXML TableColumn<LabRequest, Integer> activeReqAmount;
  @FXML TableColumn<LabRequest, String> patientNameReq;
  @FXML TableColumn<LabRequest, String> activeReqStatus;
  @FXML TableColumn<LabRequest, String> activeReqStaff;
  @FXML TableColumn<LabRequest, String> activeReqDestination;
  @FXML TableColumn<LabRequest, String> activeDate;
  @FXML TableColumn<LabRequest, String> activeTime;

  @FXML TableView<LabRequest> activeRequestTable;
  @FXML VBox requestHolder;

  // Testing below
  @FXML javafx.scene.text.Text time;
  @FXML VBox inputFields;
  @FXML Button clearButton;
  @FXML Button submitButton;
  @FXML AnchorPane sideBarAnchor;
  @FXML Button sideBarButton;

  ObservableList<LabRequest> labUIRequests = FXCollections.observableArrayList();
  ObservableList<JFXCheckBox> checkBoxes = FXCollections.observableArrayList();
  ObservableList<JFXTextArea> checkBoxesInput = FXCollections.observableArrayList();
  // Udb udb = DBController.udb;
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
      for (Employee e : Udb.getInstance().EmployeeImpl.hList().values()) {
        staff.add(e);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    employees.setTooltip(new Tooltip());
    employees.getItems().addAll(staff);
    new ComboBoxAutoComplete<Employee>(employees, 367, 243);

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
    openNav.setToY(670);
    TranslateTransition closeNav = new TranslateTransition(new Duration(350), sideBarAnchor);
    sideBarButton.setOnAction(
        (ActionEvent evt) -> {
          if (sideBarAnchor.getTranslateY() != 670) {
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

  private void setUpActiveRequests() throws SQLException, IOException {
    activeReqID.setCellValueFactory(new PropertyValueFactory<>("ID"));
    activeReqType.setCellValueFactory(new PropertyValueFactory<LabRequest, String>("name"));
    activeReqAmount.setCellValueFactory(new PropertyValueFactory<LabRequest, Integer>("amount"));
    patientNameReq.setCellValueFactory(new PropertyValueFactory<LabRequest, String>("patientName"));
    activeReqStatus.setCellValueFactory(new PropertyValueFactory<LabRequest, String>("status"));
    activeReqStaff.setCellValueFactory(new PropertyValueFactory<LabRequest, String>("employee"));
    activeReqDestination.setCellValueFactory(
        new PropertyValueFactory<LabRequest, String>("location"));
    activeDate.setCellValueFactory(new PropertyValueFactory<LabRequest, String>("date"));
    activeTime.setCellValueFactory(new PropertyValueFactory<LabRequest, String>("time"));
    activeRequestTable.setItems(getActiveRequestList());
  }

  private ObservableList<LabRequest> newRequest(
      String id,
      String name,
      int amount,
      String patient,
      String status,
      Employee employee,
      String destination,
      String date,
      String time) {

    LabRequest r =
        new LabRequest(id, name, amount, patient, status, employee, destination, date, time);
    r.gettingTheLocation();
    labUIRequests.add(r);
    return labUIRequests;
  }

  private ObservableList<LabRequest> getActiveRequestList() throws SQLException, IOException {
    for (LabRequest request : Udb.getInstance().labRequestImpl.hList().values()) {
      LabRequest r =
          new LabRequest(
              request.getID(),
              request.getName(),
              request.getAmount(),
              request.getPatientName(),
              request.getStatus(),
              request.getEmployee(),
              request.getDestination(),
              request.getDate(),
              request.getTime());

      r.gettingTheLocation();
      labUIRequests.add(r);
    }
    return labUIRequests;
  }

  @Override
  public void addRequest() {

    String patientInput = patientNameField.getText().trim();
    // String staffInput = staffMemberField.getText().trim();
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    for (int i = 0; i < checkBoxes.size(); i++) {
      if (checkBoxes.get(i).isSelected()) {

        boolean alreadyHere = true;
        String serviceID = "notWork";

        // makes the id
        while (alreadyHere) {
          double rand = Math.random() * 10000;

          try {
            alreadyHere =
                Udb.getInstance().compServRequestImpl.hList().containsKey("LAB" + (int) rand);
          } catch (Exception e) {
            System.out.println("alreadyHere variable messed up in lab service request controller");
          }

          serviceID = "LAB" + (int) rand;
        }

        String inputString = checkBoxesInput.get(i).getText().trim();
        String room = locations.getValue().getNodeID();
        Employee staffInput = employees.getValue();
        LabRequest request =
            new LabRequest(
                serviceID,
                checkBoxes.get(i).getText().trim(),
                Integer.parseInt(inputString),
                patientInput,
                "In progress",
                staffInput,
                room,
                sdf3.format(timestamp).substring(0, 10),
                sdf3.format(timestamp).substring(11));

        request.gettingTheLocation();

        activeRequestTable.setItems(
            newRequest(
                request.getID(),
                request.getName(),
                request.getAmount(),
                request.getPatientName(),
                request.getStatus(),
                request.getEmployee(),
                request.getLocation().getNodeID(),
                request.getDate(),
                request.getTime()));
        try {
          request.gettingTheLocation();
          Udb.getInstance()
              .add(
                  new LabRequest(
                      request.getID(),
                      request.getName(),
                      request.getAmount(),
                      request.getPatientName(),
                      request.getStatus(),
                      request.getEmployee(),
                      request.getDestination(),
                      request.getDate(),
                      request.getTime()));
          //          submission.setText("Request for " + checkBoxes.get(i).getText() + "
          // successfully sent.");
        } catch (IOException e) {
          e.printStackTrace();
          //          submission.setText("Request for " + checkBoxes.get(i).getText() + " failed.");
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
    clear();
  }

  /*private ObservableList<LabUI> newRequest(
      String id,
      String patientName,
      String staffName,
      int amount,
      String labType,
      String location,
      String date,
      String time) {
    labUIRequests.add(new LabUI(id, patientName, staffName, amount, labType, location, date, time));
    return labUIRequests;
  }*/

  // =============Update the request from edit======================
  @Override
  public void updateRequest() {
    LabRequest oldRequest = activeRequestTable.getSelectionModel().getSelectedItem();
    newCon.updateRequest();
    LabRequest request = (LabRequest) newCon.getRequest();
    request.gettingTheLocation();
    labUIRequests.remove(oldRequest);
    labUIRequests.add(request);
    activeRequestTable.setItems(labUIRequests);
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

  // ======remove edit request=============
  @Override
  public void removeRequest() {
    LabRequest request = activeRequestTable.getSelectionModel().getSelectedItem();
    labUIRequests.remove(request);
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

  public void clear(ActionEvent actionEvent) {
    clear();
  }

  private void clear() {

    for (int i = 0; i < checkBoxes.size(); i++) {
      checkBoxes.get(i).setSelected(false);
    }
    patientNameField.setText("");
    // staffMemberField.setText("");
  }

  public void mouseHovered(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: #E6F6F7");
  }

  public void mouseExit(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: transparent");
  }

  public void clearRequest() {
    for (int i = 0; i < checkBoxes.size(); i++) {
      checkBoxes.get(i).setSelected(false);
      checkBoxesInput.get(i).clear();
    }
    //    requestText.setText("Cleared Requests!");
    //    requestText.setVisible(true);
    new Thread(
            () -> {
              try {
                Thread.sleep(1500); // milliseconds
                Platform.runLater(
                    () -> {
                      //                      requestText.setVisible(false);
                    });
              } catch (InterruptedException ie) {
              }
            })
        .start();
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

  public void editRemoveReq() {
    removeRequest();
  }
}
