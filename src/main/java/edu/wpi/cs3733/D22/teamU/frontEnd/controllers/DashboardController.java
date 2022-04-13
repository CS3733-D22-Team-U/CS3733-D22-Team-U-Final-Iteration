package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class DashboardController extends ServiceController {

  public Button logOutButton;
  @FXML Button navButton;
  @FXML AnchorPane animatePane;
  @FXML ImageView navPaneArrow;

  @FXML Button clockButton;
  @FXML AnchorPane clockPane;
  @FXML ImageView clockPaneArrow;

  @FXML AnchorPane anchor;

  @FXML ButtonBar topRow;
  @FXML ButtonBar bottomRow;

  @FXML Pane backgroundPane;

  @FXML Text time;
  @FXML Text date;

  @FXML Text userName;

  @FXML Pane turtlePane;
  @FXML Circle apple;
  @FXML AnchorPane turtAnchor;
  @FXML Button turtButton;
  private static final String HOVERED_BUTTON = "-fx-border-color: #029ca6";

  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    userName.setText("Dr." + "____");
    handleNavPaneAnimation();
    handleClockPaneAnimation();
    handleNavButtons();
    handeDateTime();
    handleTurtle();
    playTurtle();
  }

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

  private void handeDateTime() {
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
  }

  private void handleNavPaneAnimation() {
    TranslateTransition openNav = new TranslateTransition(new Duration(350), animatePane);
    openNav.setToX(0);
    TranslateTransition closeNav = new TranslateTransition(new Duration(350), animatePane);
    navButton.setOnAction(
        (ActionEvent evt) -> {
          navPaneArrow.setRotate(navPaneArrow.getRotate() * -1);
          if (animatePane.getTranslateX() != 0) {
            openNav.play();
          } else {
            closeNav.setToX(846);
            closeNav.play();
            TranslateTransition closeOther = new TranslateTransition(new Duration(350), clockPane);
            closeOther.setToX(0);
            closeOther.play();
            clockPaneArrow.setRotate(clockPaneArrow.getRotate() * -1);
          }
        });
  }

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
  }

  private void handleClockPaneAnimation() {
    TranslateTransition openNav = new TranslateTransition(new Duration(350), clockPane);
    openNav.setToX(-846);
    TranslateTransition closeNav = new TranslateTransition(new Duration(350), clockPane);

    clockButton.setOnAction(
        (ActionEvent evt) -> {
          clockPaneArrow.setRotate(clockPaneArrow.getRotate() * -1);
          if (clockPane.getTranslateX() != -846) {
            openNav.play();
            TranslateTransition closeOther =
                new TranslateTransition(new Duration(350), animatePane);
            closeOther.setToX(0);
            closeOther.play();
            navPaneArrow.setRotate(navPaneArrow.getRotate() * -1);
          } else {
            closeNav.setToX(0);
            closeNav.play();
          }
        });
  }

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

  public void closeNavTabs(MouseEvent mouseEvent) {
    if (mouseEvent.getY() < animatePane.getLayoutY()
        || mouseEvent.getY() > animatePane.getLayoutY() + animatePane.getHeight()) {
      if (animatePane.getTranslateX() != 0) {
        TranslateTransition closeNav = new TranslateTransition(new Duration(350), animatePane);
        closeNav.setToX(0);
        closeNav.play();
        navPaneArrow.setRotate(navPaneArrow.getRotate() * -1);
      }
      if (clockPane.getTranslateX() == -846) {
        TranslateTransition closeNav = new TranslateTransition(new Duration(350), clockPane);
        closeNav.setToX(0);
        closeNav.play();
        clockPaneArrow.setRotate(navPaneArrow.getRotate() * -1);
      }
    }
  }
}
