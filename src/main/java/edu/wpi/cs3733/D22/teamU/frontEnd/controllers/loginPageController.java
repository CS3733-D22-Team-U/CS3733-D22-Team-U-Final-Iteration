package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
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
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
  public TextField activeID;
  public TextField activeUsername;
  public TextField newPassword;
  @FXML Button changeButton;
  @FXML Button backButton;
  @FXML Circle loadingCircle;
  @FXML Group loginGroup;
  @FXML Group passwordGroup;

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
                        // DBController.main(new String[] {username.getText(), password.getText()});
                        boolean foundUser = false;

                        for (Employee a : Udb.getInstance().EmployeeImpl.hList().values()) {
                          if (a.getUsername().equals(username.getText().trim())
                              && a.getPassword().equals(password.getText().trim())) {
                            foundUser = true;
                            if (a.getOccupation().equals("Administrator")) {
                              Udb.admin = true;
                            } else {
                              Udb.admin = false;
                            }
                          }
                        }

                        if (foundUser) {
                        } else {
                          throw new SQLException();
                        }

                        Scene scene = null;
                        try {
                          scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/Dashboard.fxml");
                        } catch (IOException e) {
                          e.printStackTrace();
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

  public void callChangePasswordFunction(ActionEvent actionEvent) {
    try {
      Udb.getInstance()
          .EmployeeImpl
          .changePassword(activeID.getText(), activeUsername.getText(), newPassword.getText());
    } catch (Exception e) {

    }
    activeID.setText("");
    activeUsername.setText("");
    newPassword.setText("");
  }

  public void goBackToLogIn(ActionEvent actionEvent) {
    loginGroup.setVisible(true);
    passwordGroup.setVisible(false);
  }

  public void goToChangePassword(ActionEvent actionEvent) {
    loginGroup.setVisible(false);
    passwordGroup.setVisible(true);
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
