package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MealDeliveryController extends ServiceController {

  @FXML JFXHamburger hamburger;
  @FXML VBox vBoxPane;
  @FXML Button placeMealRequest;
  @FXML Text mealStatus;
  @FXML CheckBox veganCheckbox;
  @FXML CheckBox vegCheckbox;
  @FXML CheckBox glutenCheckbox;
  @FXML CheckBox kosherCheckbox;
  @FXML CheckBox halalCheckbox;
  @FXML CheckBox lactoseCheckbox;
  @FXML CheckBox nutsCheckbox;
  @FXML CheckBox shellfishCheckbox;
  @FXML TextField requestID;
  @FXML TextField patientNameMeal;
  @FXML TextField employeeNameMeal;
  @FXML TextField locationMeal;
  @FXML Pane pane;
  @FXML Pane assistPane;

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

  public void placeMealRequest(ActionEvent actionEvent) {
    mealStatus.setText("Processing...");
    mealStatus.setVisible(true);
    new Thread(
            () -> {
              try {
                Thread.sleep(1500); // milliseconds
                Platform.runLater(
                    () -> {
                      mealStatus.setText("Done");
                    });
                Thread.sleep(1500); // milliseconds
                Platform.runLater(
                    () -> {
                      mealStatus.setVisible(false);
                      veganCheckbox.setSelected(false);
                      vegCheckbox.setSelected(false);
                      glutenCheckbox.setSelected(false);
                      kosherCheckbox.setSelected(false);
                      halalCheckbox.setSelected(false);
                      lactoseCheckbox.setSelected(false);
                      nutsCheckbox.setSelected(false);
                      shellfishCheckbox.setSelected(false);
                      requestID.setText("");
                      patientNameMeal.setText("");
                      employeeNameMeal.setText("");
                      locationMeal.setText("");
                    });
              } catch (InterruptedException ie) {
              }
            })
        .start();
  }

  public void clearMealRequest(ActionEvent actionEvent) {
    veganCheckbox.setSelected(false);
    vegCheckbox.setSelected(false);
    glutenCheckbox.setSelected(false);
    kosherCheckbox.setSelected(false);
    halalCheckbox.setSelected(false);
    lactoseCheckbox.setSelected(false);
    nutsCheckbox.setSelected(false);
    shellfishCheckbox.setSelected(false);
    requestID.setText("");
    patientNameMeal.setText("");
    employeeNameMeal.setText("");
    locationMeal.setText("");
  }

  @Override
  public void addRequest() {}

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}
}
