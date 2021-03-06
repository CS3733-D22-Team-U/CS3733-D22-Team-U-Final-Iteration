package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HomePageController extends ServiceController {
  @FXML ImageView TEST;

  public Button logOutButton;
  public Text headerText;
  public Pane turtlePond;
  @FXML Button navButton;
  @FXML ImageView navPaneArrow;

  @FXML Button clockButton;
  @FXML ImageView clockPaneArrow;

  @FXML AnchorPane anchor;

  @FXML ButtonBar topRow;
  @FXML ButtonBar bottomRow;
  @FXML ButtonBar bottomRow1;

  @FXML Pane backgroundPane;

  @FXML Text time;
  @FXML Text date;

  @FXML Text userName;
  @FXML Button allRequestsButton;
  @FXML Text name;

  @FXML ImageView turtle;
  @FXML AnchorPane turtAnchor;
  @FXML DatePicker datePicker;
  @FXML Text timeOfDay;
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

  String userFirstName;
  @FXML ImageView backgroundImage;

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

    try {
      listofEmployees();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    handleDateTime();
    playTurtle();
  }

  public void playTurtle() {
    anchor.setOnKeyPressed(
        e -> {
          double nextX;
          double nextY;

          if (e.getCode() == KeyCode.D) {
            nextX = turtle.getLayoutX() + 10;
            if (nextX >= 0 && nextX <= 220) {
              turtle.setLayoutX(nextX);
              turtle.setRotate(90);
            }
          }

          if (e.getCode() == KeyCode.A) {
            nextX = turtle.getLayoutX() - 10;
            if (nextX >= 0 && nextX <= 220) {
              turtle.setLayoutX(nextX);
              turtle.setRotate(-90);
            }
          }
          if (e.getCode() == KeyCode.W) {
            nextY = turtle.getLayoutY() - 10;
            if (nextY >= 0 && nextY <= 200) {
              turtle.setLayoutY(nextY);
              turtle.setRotate(0);
            }
          }
          if (e.getCode() == KeyCode.S) {
            nextY = turtle.getLayoutY() + 10;
            if (nextY >= 0 && nextY <= 200) {
              turtle.setLayoutY(nextY);
              turtle.setRotate(180);
            }
          }
        });
  }

  private void handleDateTime() {
    Timestamp quickStamp = new Timestamp(System.currentTimeMillis());
    String hour = sdf3.format(quickStamp).substring(11, 13);
    int hourInt = Integer.parseInt(hour);
    if (hourInt >= 0 && hourInt < 12) {
      timeOfDay.setText("Good Morning ");
    } else if (hourInt > 12 && hourInt <= 18) {
      timeOfDay.setText("Good Afternoon ");
    } else {
      timeOfDay.setText("Good Evening ");
    }

    Employee user;

    try {
      user = Udb.getInstance().getUser();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    name.setText(user.getFirstName());

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

  public void toCloseApp(ActionEvent actionEvent) {
    Platform.exit();
  }

  public void toLogOut(ActionEvent actionEvent) throws IOException, SQLException {
    Udb.getInstance().closeConnection();
    Udb.password = "";
    Udb.username = "";
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/logInPage.fxml"));
    Uapp.stage.getScene().setRoot(home);
    Stage stage = Uapp.stage;
    stage.show();
    masterThread.stop();
  }

  public void toSettings(ActionEvent actionEvent) {
    System.out.println("Going to settings");
  }

  public void logOut(ActionEvent actionEvent) {
    System.out.println("Logging out");
  }

  @Override
  public void addRequest() {}

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}

  ObservableList<MenuItem> menuItemsList = FXCollections.observableArrayList();

  public ObservableList<MenuItem> listofEmployees() throws SQLException, IOException {
    for (Employee employee : Udb.getInstance().EmployeeImpl.hList().values()) {
      menuItemsList.add(new MenuItem(employee.toString()));
    }
    return menuItemsList;
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
