package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXHamburger;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
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



  @Override
  public void addRequest() {}

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}

  @Override
  public void initialize(URL location, ResourceBundle resources) {}


  public void toHarsh(){
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/HarshIcon.jpeg");
  }
  public void toMarko(ActionEvent actionEvent){
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/MarkoIcon.jpeg");
  }
  public void toJoselin(ActionEvent actionEvent){
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/JoselinIcon.jpeg");
  }
  public void toDeepti(ActionEvent actionEvent){
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/DeeptiIcon.jpeg");
  }
  public void toNick(ActionEvent actionEvent){
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/NickIcon.jpeg");
  }
  public void toKody(ActionEvent actionEvent){
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/KodyIcon.jpeg");
  }
  public void toTim(ActionEvent actionEvent){
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/TimIcon.jpeg");
  }
  public void toWill(ActionEvent actionEvent){
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/WillIcon.jpeg");
  }
  public void toMike(ActionEvent actionEvent){
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/MikeIcon.jpeg");
  }
  public void toIain(ActionEvent actionEvent){
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/IAINIcon.jpeg");
  }
  public void toBelisha(ActionEvent actionEvent){
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/BelishaIcon.jpeg");
  }

  private void addTeamImage(String resource) {
    URL a = Uapp.class.getClassLoader().getResource(resource);
    imageHover.setImage(new Image(String.valueOf(a)));
    //imageHover.setScaleX(1.75);
    //imageHover.setScaleY(1.75);
    imageHover.setFitHeight(200);
    imageHover.setFitWidth(150);
    //imageHover.setX((imageHover.getFitWidth()));
    //imageHover.setY((imageHover.getFitHeight()));

  }
}
