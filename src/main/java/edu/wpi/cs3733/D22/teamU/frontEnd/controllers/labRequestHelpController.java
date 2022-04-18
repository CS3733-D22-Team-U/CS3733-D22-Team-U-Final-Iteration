package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;

public class labRequestHelpController extends ServiceController {
  @Override
  public void initialize(URL location, ResourceBundle resources) {}

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
