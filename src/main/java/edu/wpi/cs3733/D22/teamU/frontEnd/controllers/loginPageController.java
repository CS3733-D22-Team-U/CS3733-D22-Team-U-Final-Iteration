package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class loginPageController extends ServiceController {
  Firestore db = FirestoreClient.getFirestore();

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
  double startTime;
  double endTime;
  double elapsedTime;
  @FXML ImageView backgroundImage;

  public void firebaseInit(double time, String userName)
      throws ExecutionException, InterruptedException {
    DocumentReference docRef = db.collection("loginTimes").document(userName);
    ApiFuture<QuerySnapshot> future = db.collection("loginTimes").get();
    List<QueryDocumentSnapshot> documents = future.get().getDocuments();
    for (QueryDocumentSnapshot document : documents) {
      if (document.getId().equals(userName)) {
        if (document.getDouble("Time") < elapsedTime) {
          return;
        }
      }
    }
    docRef.collection("loginTimes");
    HashMap<String, Object> data = new HashMap<>();
    data.put("Time", time);
    docRef.set(data);
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    if (Uapp.isFullScreen) {
      backgroundImage.setFitWidth(1920);
      backgroundImage.setFitHeight(1080);
      backgroundImage.setPreserveRatio(false);
    }
    RotateTransition rt = new RotateTransition(new Duration(2500), loadingCircle);
    rt.setByAngle(360);
    rt.setCycleCount(RotateTransition.INDEFINITE);
    rt.setInterpolator(Interpolator.LINEAR);
    rt.play();
    this.startTime = System.currentTimeMillis();
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
                            Udb.getInstance().setUser(a);
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

                        Parent home =
                            FXMLLoader.load(
                                Uapp.class
                                    .getClassLoader()
                                    .getResource("edu/wpi/cs3733/D22/teamU/views/HomePage.fxml"));
                        Uapp.stage.getScene().setRoot(home);

                        this.endTime = System.currentTimeMillis();

                        elapsedTime = (endTime - startTime) / 1000;

                        firebaseInit(elapsedTime, username.getText().trim());
                      } catch (IOException e) {
                        e.printStackTrace();
                      } catch (SQLException throwables) {
                        feedback.setText("Wrong Username/Password");
                        loginGroup.setVisible(true);
                      } catch (ExecutionException e) {
                        e.printStackTrace();
                      } catch (InterruptedException e) {
                        e.printStackTrace();
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

  public void makeBig(ActionEvent actionEvent) {}
}
