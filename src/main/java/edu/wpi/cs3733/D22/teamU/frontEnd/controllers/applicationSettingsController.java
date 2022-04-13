package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class applicationSettingsController {
  public void toSettingsPage(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/settingsPage.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }

  public void embedderDriver(ActionEvent actionEvent) throws SQLException, IOException {

    Udb.getInstance().changeDriver(true);
  }

  public void clientServerDriver(ActionEvent actionEvent) throws SQLException, IOException {

    Udb.getInstance().changeDriver(false);
  }
}
