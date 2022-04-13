package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.DBController;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class loginPageController extends ServiceController {
  public PasswordField password;
  public TextField username;
  public Text feedback;
  @FXML Circle loadingCircle;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    RotateTransition rt = new RotateTransition(new Duration(2500), loadingCircle);
    rt.setByAngle(360);
    rt.setCycleCount(RotateTransition.INDEFINITE);
    rt.setInterpolator(Interpolator.LINEAR);
    rt.play();
  }

  public void toHomeExtraSteps(ActionEvent actionEvent) throws IOException, InterruptedException {
    loginGroup.setVisible(false);
    new Thread(
            () -> {
              try {
                Thread.sleep(1800); // milliseconds
                Platform.runLater(
                    () -> {
                      try {
                        DBController.main(new String[] {username.getText(), password.getText()});
                        Scene scene = null;
                        try {
                          scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/Dashboard.fxml");
                        } catch (IOException e) {
                          feedback.setText("Wrong Username/Password");
                        }
                        Stage appStage =
                            (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        appStage.setScene(scene);
                        appStage.show();
                      } catch (IOException e) {
                        e.printStackTrace();
                      } catch (SQLException throwables) {
                        feedback.setText("Wrong Username/Password");
                        loginGroup.setVisible(true);
                      }
                    });
              } catch (InterruptedException ie) {
              }
            })
        .start();
  }

  @Override
  public void addRequest() {}

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}

  public void toCloseApp(ActionEvent actionEvent) {
    Platform.exit();
  }
}
