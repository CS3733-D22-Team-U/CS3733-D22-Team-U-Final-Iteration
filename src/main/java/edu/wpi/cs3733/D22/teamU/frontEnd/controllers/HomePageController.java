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
import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

public class HomePageController extends ServiceController {

  public Button logOutButton;
  public Text headerText;
  public Pane turtlePond;
  public ImageView turtle;
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

  @FXML Pane turtlePane;
  @FXML Circle apple;
  @FXML AnchorPane turtAnchor;
  @FXML Button turtButton;
  @FXML Text message;
  @FXML DatePicker datePicker;
  @FXML Text timeOfDay;

  private static final String HOVERED_BUTTON = "-fx-border-color: #029ca6";

  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    datePicker = new DatePicker(LocalDate.now());
    DatePickerSkin datePickerSkin = new DatePickerSkin(datePicker);
    datePickerSkin.getDisplayNode().setLayoutY(datePicker.getLayoutY());
    datePickerSkin.getDisplayNode().setLayoutX(datePicker.getLayoutX());
    datePickerSkin.getPopupContent();
    datePickerSkin.show();
    Node popupContent = datePickerSkin.getPopupContent();
    // [...]
    LocalDate selectedDate = datePicker.getValue();

    try {
      listofEmployees();
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.anchor
        .heightProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              double yScale = this.anchor.getHeight() / this.anchor.getPrefHeight();
              double xScale = this.anchor.getWidth() / this.anchor.getPrefWidth();
              Math.min(yScale, xScale);
              Scale scale = new Scale(xScale, yScale);
              scale.setPivotX(0.0D);
              scale.setPivotY(0.0D);
              this.anchor.getScene().getRoot().getTransforms().setAll(new Transform[] {scale});
            });
    this.anchor
        .widthProperty()
        .addListener(
            (obs, oldVal, newVal) -> {
              double yScale = this.anchor.getHeight() / this.anchor.getPrefHeight();
              double xScale = this.anchor.getWidth() / this.anchor.getPrefWidth();
              Math.min(yScale, xScale);
              Scale scale = new Scale(xScale, yScale);
              scale.setPivotX(0.0D);
              scale.setPivotY(0.0D);
              this.anchor.getScene().getRoot().getTransforms().setAll(new Transform[] {scale});
            });

    // userName.setText("Dr." + "____");

    // handleNavButtons();
    handeDateTime();
    // handleTurtle();
    // playTurtle();
  }

  /*
   private void handleTurtle() {
     TranslateTransition openNav = new TranslateTransition(new Duration(350), turtAnchor);
     openNav.setToY(-415);
     TranslateTransition closeNav = new TranslateTransition(new Duration(350), turtAnchor);
     turtButton.setOnAction(
         (ActionEvent evt) -> {
           if (turtAnchor.getTranslateY() != -415) {
             openNav.play();
           } else {
             closeNav.setToY(0);
             closeNav.play();
           }
         });
   }

   public void playTurtle() {
     anchor.setOnKeyPressed(
         e -> {
           double nextX;
           double nextY;

           if (e.getCode() == KeyCode.D) {
             nextX = turtlePane.getLayoutX() + 10;
             if (nextX >= 0 && nextX <= 363) {
               turtlePane.setLayoutX(nextX);
               turtlePane.setRotate(90);
             }
           }

           if (e.getCode() == KeyCode.A) {
             nextX = turtlePane.getLayoutX() - 10;
             if (nextX >= 0 && nextX <= 363) {
               turtlePane.setLayoutX(nextX);
               turtlePane.setRotate(-90);
             }
           }
           if (e.getCode() == KeyCode.W) {
             nextY = turtlePane.getLayoutY() - 10;
             if (nextY >= 0 && nextY <= 271) {
               turtlePane.setLayoutY(nextY);
               turtlePane.setRotate(0);
             }
           }
           if (e.getCode() == KeyCode.S) {
             nextY = turtlePane.getLayoutY() + 10;
             if (nextY >= 0 && nextY <= 271) {
               turtlePane.setLayoutY(nextY);
               turtlePane.setRotate(180);
             }
           }
         });
   }

  */

  private void handeDateTime() {
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

  /*
  private void handleNavButtons() {
    for (Node node : topRow.getButtons()) {
      Button button = (Button) node;
      String initStyle = button.getStyle();
      button.setStyle(initStyle);
      button.setOnMouseEntered(e -> button.setStyle(initStyle + HOVERED_BUTTON));
      button.setOnMouseExited(e -> button.setStyle(initStyle));
    }
    for (Node node : bottomRow.getButtons()) {
      Button button = (Button) node;
      String initStyle = button.getStyle();
      button.setStyle(initStyle);
      button.setOnMouseEntered(e -> button.setStyle(initStyle + HOVERED_BUTTON));
      button.setOnMouseExited(e -> button.setStyle(initStyle));
    }

    for (Node node : bottomRow1.getButtons()) {
      Button button = (Button) node;
      String initStyle = button.getStyle();
      button.setStyle(initStyle);
      button.setOnMouseEntered(e -> button.setStyle(initStyle + HOVERED_BUTTON));
      button.setOnMouseExited(e -> button.setStyle(initStyle));
    }
  }

   */

  public void toCloseApp(ActionEvent actionEvent) {
    Platform.exit();
  }

  public void toLogOut(ActionEvent actionEvent) throws IOException, SQLException {

    Udb.getInstance().closeConnection();
    Udb.password = "";
    Udb.username = "";
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/logInPage.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }

  public void toSettingsPage(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/settingsPage.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
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
}
