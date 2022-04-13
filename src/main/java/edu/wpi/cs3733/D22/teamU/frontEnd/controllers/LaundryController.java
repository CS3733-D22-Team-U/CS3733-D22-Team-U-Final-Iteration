package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LaundryController extends ServiceController {
  @FXML JFXHamburger hamburger;
  @FXML VBox vBoxPane;
  @FXML Text laundryStatus;
  @FXML CheckBox hangCB;
  @FXML CheckBox machineCB;
  @FXML CheckBox hypoCB;
  @FXML TextField requestID;
  @FXML TextField patientNameLaundry;
  @FXML TextField employeeNameLaundry;
  @FXML TextField locationLaundry;
  @FXML TextField dropOffTF;
  @FXML TextField pickUpTF;
  @FXML TextArea notesLaundryTA;
  @FXML Pane assistPane;
  @FXML Pane pane;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    HamburgerBasicCloseTransition closeTransition = new HamburgerBasicCloseTransition(hamburger);

    closeTransition.setRate(-1);
    hamburger.addEventHandler(
        MouseEvent.MOUSE_CLICKED,
        e -> {
          closeTransition.setRate(closeTransition.getRate() * -1);
          closeTransition.play();
          vBoxPane.setVisible(!vBoxPane.isVisible());
          pane.setDisable(!pane.isDisable());
          if (pane.isDisable()) {
            hamburger.setPrefWidth(200);
            pane.setEffect(new GaussianBlur(10));
            assistPane.setDisable(true);
          } else {
            pane.setEffect(null);
            hamburger.setPrefWidth(77);
            assistPane.setDisable(false);
          }
        });
  }

  @Override
  public void addRequest() {
    laundryStatus.setText("Processing...");
    laundryStatus.setVisible(true);
    new Thread(
            () -> {
              try {
                Thread.sleep(1500); // milliseconds
                Platform.runLater(
                    () -> {
                      laundryStatus.setText("Done");
                    });
                Thread.sleep(1500); // milliseconds
                Platform.runLater(
                    () -> {
                      laundryStatus.setVisible(false);
                      hangCB.setSelected(false);
                      machineCB.setSelected(false);
                      hypoCB.setSelected(false);
                      requestID.setText("");
                      patientNameLaundry.setText("");
                      employeeNameLaundry.setText("");
                      locationLaundry.setText("");
                      dropOffTF.setText("");
                      pickUpTF.setText("");
                      notesLaundryTA.setText("");
                    });

              } catch (InterruptedException ie) {
              }
            })
        .start();
  }

  public void clearLaundryRequest(ActionEvent actionEvent) {
    hangCB.setSelected(false);
    machineCB.setSelected(false);
    hypoCB.setSelected(false);
    requestID.setText("");
    patientNameLaundry.setText("");
    employeeNameLaundry.setText("");
    locationLaundry.setText("");
    dropOffTF.setText("");
    pickUpTF.setText("");
    notesLaundryTA.setText("");
  }

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}
}
