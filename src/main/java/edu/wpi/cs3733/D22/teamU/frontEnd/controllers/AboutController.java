package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXHamburger;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class AboutController extends ServiceController {

  @FXML Pane pane;
  @FXML JFXHamburger hamburger;
  @FXML ImageView imageHover;
  private double x, y;
  @FXML TextField quote;

  @Override
  public void addRequest() {}

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  public void toHarsh() {
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/harshRESIZE.jpeg");
  }

  public void toMarko(ActionEvent actionEvent) {
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/markoRESIZE.jpeg");
  }

  public void toJoselin(ActionEvent actionEvent) {
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/joselin2RESIZE.jpeg");
  }

  public void toDeepti(ActionEvent actionEvent) {
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/deeptiRESIZE.jpeg");
  }

  public void toNick(ActionEvent actionEvent) {
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/nickRESIZE.jpeg");
  }

  public void toKody(ActionEvent actionEvent) {
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/kodyRESIZE.jpeg");
  }

  public void toTim(ActionEvent actionEvent) {
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/timRESIZE.jpeg");
  }

  public void toWill(ActionEvent actionEvent) {
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/willRESIZE.jpeg");
  }

  public void toMike(ActionEvent actionEvent) {
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/mikeRESIZE.jpeg");
  }

  public void toIain(ActionEvent actionEvent) {
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/iainRESIZE.jpeg");
  }

  public void toBelisha(ActionEvent actionEvent) {
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/belishaRESIZE.jpeg");
  }

  private void addTeamImage(String resource) {
    URL a = Uapp.class.getClassLoader().getResource(resource);
    imageHover.setImage(new Image(String.valueOf(a)));
    // imageHover.setScaleX(1.75);
    // imageHover.setScaleY(1.75);
    // imageHover.setX((imageHover.getFitWidth()));
    // imageHover.setY((imageHover.getFitHeight()));

  }
}
