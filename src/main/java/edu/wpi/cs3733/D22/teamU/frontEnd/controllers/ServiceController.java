package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import edu.wpi.cs3733.D22.teamU.frontEnd.services.Service;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import javafx.scene.transform.Transform;
import javafx.stage.Stage;

public abstract class ServiceController implements Initializable, Service {
  public Thread masterThread;
  @FXML AnchorPane anchor;
  private Stage stage = Uapp.stage;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    stage.setFullScreen(true);
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
  }

  public void toSettingsPage(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/settingsPage.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
  }

  public void toHome(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/HomePage.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
  }

  public void toEquipmentDelivery(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/equipmentDelivery.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
    masterThread.stop();
  }

  public void toMealDelivery(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/mealDelivery.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
  }

  public void toMaintenanceRequest(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/maintenanceRequest.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
    masterThread.stop();
  }

  // testing religious request
  public void toGiftAndFloral(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/giftFloralService.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
    masterThread.stop();
  }

  public void toMedicineDelivery(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/medRevamp.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
  }

  public void toLabRequest(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/labRequestServices.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
    masterThread.stop();
  }

  public void toLaundry(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/laundryService.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
    masterThread.stop();
  }

  public void toSideView(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/sideView.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
    masterThread.stop();
  }

  public void toAbout(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class.getClassLoader().getResource("edu/wpi/cs3733/D22/teamU/views/about.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
    masterThread.stop();
  }

  public void toAllRequests(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/allRequests.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
  }

  public void toMap(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class.getClassLoader().getResource("edu/wpi/cs3733/D22/teamU/views/map.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
  }

  public void toMapEditor(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class.getClassLoader().getResource("edu/wpi/cs3733/D22/teamU/views/map.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
    masterThread.stop();
  }

  public void toCompService(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/compServRequest.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
  }

  public void toReligious(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/ReligiousRequest.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
    masterThread.stop();
  }

  public void toSecurity(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/securityRequestUI.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
    masterThread.stop();
  }

  public void toTranslate(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/translatorRequest.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
    masterThread.stop();
  }

  public void toCredits(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/creditsPage.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
    masterThread.stop();
  }

  public void toCovidInfo(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/COVIDInfo.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
    masterThread.stop();
  }

  public void toReport(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/reportPage.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
    masterThread.stop();
  }

  public void toLoginTimes(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/loginTimes.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
    masterThread.stop();
  }

  public void toEmployeeReq(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/filterEmployee.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
    masterThread.stop();
  }

  public void toRequestsPage(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/requestsPage.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
    masterThread.stop();
  }

  public void toMapPage(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class.getClassLoader().getResource("edu/wpi/cs3733/D22/teamU/views/mapPage.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
    masterThread.stop();
  }

  public void toEmployeePage(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/employeePage.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
    masterThread.stop();
  }

  public void toAPI(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/APILandingPage.fxml"));
    Uapp.stage.getScene().setRoot(home);
    stage.show();
    masterThread.stop();
  }

  @Override
  public abstract void addRequest() throws SQLException, IOException;

  @Override
  public abstract void removeRequest();

  @Override
  public abstract void updateRequest();
}
