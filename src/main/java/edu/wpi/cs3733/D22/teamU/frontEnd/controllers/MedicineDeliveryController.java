package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextArea;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MedicineRequest.MedicineRequest;
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

public class MedicineDeliveryController extends ServiceController {

  @FXML JFXCheckBox Advil;
  @FXML JFXCheckBox Alprozalam;
  @FXML JFXCheckBox AmphetamineSalt;
  @FXML JFXCheckBox Atorvastatin;
  @FXML JFXCheckBox Lisinopril;
  @FXML JFXCheckBox Metformin;
  @FXML JFXCheckBox specialCheck;
  @FXML Button clearButton;
  @FXML Button submitButton;
  @FXML TextField patientName;
  @FXML TextField staffName;
  @FXML TextField advilTxt;
  // @FXML TextField IDtxt;
  // @FXML TextField amount;
  @FXML TextField alproTxt;
  @FXML TextField saltTxt;
  @FXML TextField atorvTxt;
  @FXML TextField lisinTxt;
  @FXML TextField metTxt;
  @FXML TextArea specialReqTxt;
  @FXML Text reset;
  @FXML Text processText;
  @FXML JFXHamburger hamburger;
  @FXML VBox medVbox;
  @FXML VBox nameVbox;
  @FXML VBox vBoxPane;
  @FXML Pane pane;
  @FXML Pane assistPane;
  @FXML AnchorPane bigPane;
  @FXML TabPane tab;
  @FXML TextField destination;
  @FXML AnchorPane sideBarAnchor;
  @FXML Button sideBarButton;

  @FXML Text time;

  @FXML TableColumn<MedicineRequest, String> activeReqID;
  @FXML TableColumn<MedicineRequest, String> activeReqName;
  @FXML TableColumn<MedicineRequest, Integer> activeReqAmount;
  @FXML TableColumn<MedicineRequest, String> activeReqPatientName;
  @FXML TableColumn<MedicineRequest, String> activeReqType;
  @FXML TableColumn<MedicineRequest, String> activeReqDestination;
  @FXML TableColumn<MedicineRequest, String> activeDate;
  @FXML TableColumn<MedicineRequest, String> activeReqStatus;
  @FXML TableColumn<MedicineRequest, String> activeReqEmployee;
  @FXML TableColumn<MedicineRequest, String> activeTime;

  @FXML TableColumn<MedicineRequest, String> nameCol;
  @FXML TableColumn<MedicineRequest, Integer> inUse;
  @FXML TableColumn<MedicineRequest, Integer> available;
  @FXML TableColumn<MedicineRequest, Integer> total;
  @FXML TableColumn<MedicineRequest, String> location;

  @FXML Text requestText;

  @FXML TableView<MedicineRequest> activeRequestTable;
  @FXML VBox requestHolder;

  @FXML StackPane requestsStack;
  @FXML Pane newRequestPane;
  @FXML Pane allEquipPane;
  @FXML Pane activeRequestPane;

  @FXML Button newReqButton;
  @FXML Button activeReqButton;
  public ComboBox<Location> locations;
  public ComboBox<Employee> employees;
  ArrayList<Location> nodeIDs;
  @FXML VBox inputFields;

  ArrayList<Employee> staff;

  ObservableList<MedicineRequest> medicineRequests = FXCollections.observableArrayList();

  // ObservableList<MedicineRequest> medUIRequests = FXCollections.observableArrayList();
  ObservableList<JFXCheckBox> checkBoxes = FXCollections.observableArrayList();
  ObservableList<JFXTextArea> checkBoxesInput = FXCollections.observableArrayList();

  // Udb udb = DBController.udb;

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

    submitButton
        .disableProperty()
        .bind(
            Bindings.createBooleanBinding(
                () -> checkBoxes.stream().noneMatch(JFXCheckBox::isSelected),
                checkBoxes.stream().map(JFXCheckBox::selectedProperty).toArray(Observable[]::new)));
    handleTime();

    // BooleanBinding submit =locations.idProperty().isEmpty().and(
    // Bindings.createBooleanBinding(checkBoxes.stream().noneMatch(JFXCheckBox::isSelected)));
    // handleTime();
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

  private void setUpActiveRequests() throws SQLException, IOException {
    activeReqID.setCellValueFactory(new PropertyValueFactory<>("ID"));
    activeReqName.setCellValueFactory(new PropertyValueFactory<MedicineRequest, String>("name"));
    activeReqAmount.setCellValueFactory(
        new PropertyValueFactory<MedicineRequest, Integer>("amount"));
    activeReqPatientName.setCellValueFactory(
        new PropertyValueFactory<MedicineRequest, String>("patientName"));
    activeReqStatus.setCellValueFactory(
        new PropertyValueFactory<MedicineRequest, String>("status"));
    activeReqEmployee.setCellValueFactory(
        new PropertyValueFactory<MedicineRequest, String>("employee"));
    activeReqDestination.setCellValueFactory(
        new PropertyValueFactory<MedicineRequest, String>("location"));
    activeDate.setCellValueFactory(new PropertyValueFactory<MedicineRequest, String>("date"));
    activeTime.setCellValueFactory(new PropertyValueFactory<MedicineRequest, String>("time"));
    activeRequestTable.setItems(getActiveRequestList());
  }

  private ObservableList<MedicineRequest> newRequest(
      String id,
      String name,
      int amount,
      String patientName,
      String status,
      Employee employee,
      String destination,
      String date,
      String time) {
    MedicineRequest r =
        new MedicineRequest(
            id, name, amount, patientName, status, employee, destination, date, time);
    r.gettingTheLocation();
    System.out.println("YOOOO");
    medicineRequests.add(r);
    return medicineRequests;
  }

  private ObservableList<MedicineRequest> getActiveRequestList() throws SQLException, IOException {
    medicineRequests.clear();
    for (MedicineRequest y : Udb.getInstance().medicineRequestImpl.List.values()) {
      System.out.println(y.getID() + " ");
    }

    for (MedicineRequest request : Udb.getInstance().medicineRequestImpl.List.values()) {
      MedicineRequest r =
          new MedicineRequest(
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
      medicineRequests.add(r);
    }

    return medicineRequests;
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

  public Employee checkEmployee(String employee) throws NullPointerException {
    if (EmployeeDaoImpl.List.get(employee) != null) {
      return EmployeeDaoImpl.List.get(employee);
    } else {
      Employee empty = new Employee("N/A");
      return empty;
    }
  }

  public void mouseHovered(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: #E6F6F7");
  }

  public void mouseExit(MouseEvent mouseEvent) {
    Button button = (Button) mouseEvent.getSource();
    button.setStyle("-fx-border-color: transparent");
  }

  @Override
  public void addRequest() {
    StringBuilder startRequestString = new StringBuilder("Your request for : ");

    String endRequest = " has been placed successfully";
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    String patientInput = patientName.getText().trim();
    String room = locations.getValue().getNodeID();
    Employee staff = employees.getValue();
    int requestAmount = 0;

    for (int i = 0; i < checkBoxes.size(); i++) {
      if (checkBoxes.get(i).isSelected()) {
        String inputString = "";

        if (checkBoxesInput.get(i).getText().trim().equals("")) {
          inputString = "0";
        } else {
          inputString = checkBoxesInput.get(i).getText().trim();
        }
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

        // makes the id
        while (alreadyHere) {
          double rand = Math.random() * 10000;

          try {
            alreadyHere =
                Udb.getInstance().compServRequestImpl.hList().containsKey("MED" + (int) rand);
          } catch (Exception e) {
            System.out.println(
                "alreadyHere variable messed up in Medicine service request controller");
          }

          serviceID = "MED" + (int) rand;
        }
        // String patient = "BRUH";

        MedicineRequest request =
            new MedicineRequest(
                serviceID,
                checkBoxes.get(i).getText(),
                requestAmount,
                patientInput,
                "In progress",
                staff,
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

  public void enableTxt() {
    if (Advil.isSelected()) {
      advilTxt.setDisable(false);
    }
    if (Alprozalam.isSelected()) {
      alproTxt.setDisable(false);
    }
    if (AmphetamineSalt.isSelected()) {
      saltTxt.setDisable(false);
    }
    if (Atorvastatin.isSelected()) {
      atorvTxt.setDisable(false);
    }
    if (Lisinopril.isSelected()) {
      lisinTxt.setDisable(false);
    }
    if (Metformin.isSelected()) {
      metTxt.setDisable(false);
    }
    if (specialCheck.isSelected()) {
      specialReqTxt.setDisable(false);
    }
  }

  public void clear() {
    Advil.setSelected(false);
    Alprozalam.setSelected(false);
    AmphetamineSalt.setSelected(false);
    Atorvastatin.setSelected(false);
    Lisinopril.setSelected(false);
    Metformin.setSelected(false);
    specialCheck.setSelected(false);
    patientName.setText("");
    staffName.setText("");
    // IDtxt.setText("");
    advilTxt.setText("");
    alproTxt.setText("");
    saltTxt.setText("");
    atorvTxt.setText("");
    lisinTxt.setText("");
    metTxt.setText("");
    specialReqTxt.setText("");
    reset.setText("Cleared requests!");
    reset.setVisible(true);
    new Thread(
            () -> {
              try {
                Thread.sleep(1500); // milliseconds
                Platform.runLater(
                    () -> {
                      reset.setVisible(false);
                    });
              } catch (InterruptedException ie) {
              }
            })
        .start();
  }
  //    lisinTxt.equals("") && metTxt.equals("") && specialReqTxt.equals(""))
  public void reqFields() {
    if (staffName.getText().equals("")
        || patientName.getText().equals("")
        // IDtxt.getText().equals("")
        || (advilTxt.getText().equals("")
                && alproTxt.getText().equals("")
                && saltTxt.getText().equals(""))
            && atorvTxt.getText().equals("")
            && lisinTxt.getText().equals("")
            && metTxt.getText().equals("")
            && specialReqTxt.getText().equals("")) {
      processText.setText("Please fill out all required fields!");
      processText.setVisible(true);
    } else {
      process();
    }
  }

  public void process() {
    processText.setText("Processing...");
    processText.setVisible(true);
    new Thread(
            () -> {
              try {
                Thread.sleep(2000); // milliseconds
                Platform.runLater(
                    () -> {
                      processText.setText(
                          "Staff Name: "
                              + staffName.getText()
                              + "\n"
                              + "Patient Name: "
                              + patientName.getText()
                              + "\n"
                              + "Order ID: "
                              // IDtxt.getText()
                              + "\n"
                              + ""
                              + "\n"
                              + "Medicine Order: "
                              + "\n"
                              + ""
                              + "\n"
                              + "Advil: "
                              + advilTxt.getText()
                              + "\n"
                              + "Alprozalam: "
                              + alproTxt.getText()
                              + "\n"
                              + "Amphetamine Salt: "
                              + saltTxt.getText()
                              + "\n"
                              + "Atorvastatin: "
                              + atorvTxt.getText()
                              + "\n"
                              + "Lisinopril: "
                              + lisinTxt.getText()
                              + "\n"
                              + "Metformin: "
                              + metTxt.getText()
                              + "\n"
                              + "Special Request: "
                              + request());
                    });
              } catch (InterruptedException ie) {
              }
            })
        .start();
  }

  private String request() {
    String request = "";
    if (specialReqTxt.equals("")) {
      request = "No response";
    } else {
      request = specialReqTxt.getText();
    }
    return request;
  }

  public void toMedHelp(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/medHelp.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }

  // ======remove edit request=============
  @Override
  public void removeRequest() {
    // ---CHANGE---
    MedicineRequest request = activeRequestTable.getSelectionModel().getSelectedItem();
    medicineRequests.remove(request);
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
    MedicineRequest oldRequest = activeRequestTable.getSelectionModel().getSelectedItem();
    newCon.updateRequest();
    MedicineRequest request = (MedicineRequest) newCon.getRequest();
    request.gettingTheLocation();
    medicineRequests.remove(oldRequest);
    medicineRequests.add(request);
    activeRequestTable.setItems(medicineRequests);
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
