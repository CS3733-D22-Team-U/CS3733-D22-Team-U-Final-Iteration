package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class MedHelpController extends ServiceController {
  @Override
  public void initialize(URL locations, ResourceBundle resources) {
    super.initialize(locations, resources);
  }

  public void toMedicineDelivery(ActionEvent actionEvent) throws IOException {
    Parent home =
        FXMLLoader.load(
            Uapp.class
                .getClassLoader()
                .getResource("edu/wpi/cs3733/D22/teamU/views/medRevamp.fxml"));
    Uapp.stage.getScene().setRoot(home);
    Uapp.stage.show();
  }

  @Override
  public void addRequest() throws SQLException, IOException {}

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}
}
