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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
  ArrayList<String> nodeIDs;
  ArrayList<String> staff;
  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
        nodeIDs.add(l.getNodeID());
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    locations.setTooltip(new Tooltip());
    locations.getItems().addAll(nodeIDs);
    new ComboBoxAutoComplete<String>(locations, 650, 290);

    staff = new ArrayList<>();
    try {
      for (Employee l : Udb.getInstance().EmployeeImpl.hList().values()) {
        staff.add(l.getEmployeeID());
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    employees.setTooltip(new Tooltip());
    employees.getItems().addAll(staff);
    new ComboBoxAutoComplete<String>(employees, 675, 380);

    for (Node checkBox : requestHolder.getChildren()) {
      checkBoxes.add((JFXCheckBox) checkBox);
    }

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
  }

  private void setUpActiveRequests() throws SQLException, IOException {
    activeReqID.setCellValueFactory(new PropertyValueFactory<>("ID"));
    activeReqName.setCellValueFactory(new PropertyValueFactory<>("name"));
    activePatientName.setCellValueFactory(new PropertyValueFactory<>("patientName"));
    activeGifts.setCellValueFactory(new PropertyValueFactory<>("gifts"));
    activeMessage.setCellValueFactory(new PropertyValueFactory<>("message"));
    activeStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    activeEmployee.setCellValueFactory(new PropertyValueFactory<>("employee"));
    activeDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));
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
    giftRequests.add(
        new GiftRequest(
            ID, name, patientName, gifts, message, status, employee, destination, date, time));
    return giftRequests;
  }

  private ObservableList<GiftRequest> getActiveRequestList() throws SQLException, IOException {
    for (GiftRequest giftRequest : Udb.getInstance().giftRequestImpl.hList().values()) {
      giftRequests.add(
          new GiftRequest(
              giftRequest.ID,
              giftRequest.name,
              giftRequest.patientName,
              giftRequest.getGifts(),
              giftRequest.getMessage(),
              giftRequest.status,
              giftRequest.employee,
              giftRequest.destination,
              giftRequest.date,
              giftRequest.time));
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
        inputString += checkBoxes.get(i).getText() + ": ";
      }
    }
    String room = locations.getValue().toString();

    String em = (employees.getValue().toString());

    double rand = Math.random() * 10000;

    GiftRequest request =
        new GiftRequest(
            (int) rand + "",
            senderName.getText(),
            patientName.getText(),
            message.getText(),
            inputString,
            "pending",
            checkEmployee(em),
            room,
            sdf3.format(timestamp).substring(0, 10),
            sdf3.format(timestamp).substring(11));

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
      Udb.getInstance()
          .add( // TODO Have random ID and enter Room Destination
              new GiftRequest(
                  request.getID(),
                  request.getName(),
                  request.getPatientName(),
                  request.getGifts(),
                  request.getMessage(),
                  request.getStatus(),
                  checkEmployee(employees.getValue().toString()),
                  request.getDestination(),
                  request.getDate(),
                  request.getTime()));

    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

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

  public void toHelp(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/giftFloralHelp.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }

  public void pushBack(ActionEvent actionEvent) {
    miniView.setVisible(false);
    expandedView.setVisible(true);
  }

  public void pushOut(ActionEvent actionEvent) {
    miniView.setVisible(true);
    expandedView.setVisible(false);
  }

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}
}
