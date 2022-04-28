package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import com.jfoenix.controls.JFXHamburger;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;
import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class AboutController extends ServiceController {

  private static final SimpleDateFormat sdf3 = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");

  @FXML Pane pane;
  @FXML JFXHamburger hamburger;
  @FXML ImageView imageHover;
  private double x, y;
  @FXML TextField quote;

  @FXML DatePicker datePicker;
  @FXML Pane datePickerPane;
  @FXML javafx.scene.text.Text time;
  @FXML Text date;
  @FXML Text quoteText;

  @Override
  public void addRequest() {}

  @Override
  public void removeRequest() {}

  @Override
  public void updateRequest() {}

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  private void addTeamImage(String resource) {
    URL a = Uapp.class.getClassLoader().getResource(resource);
    imageHover.setImage(new Image(String.valueOf(a)));
    // imageHover.setScaleX(1.75);
    // imageHover.setScaleY(1.75);
    // imageHover.setX((imageHover.getFitWidth()));
    // imageHover.setY((imageHover.getFitHeight()));

  }

  public void toHarsh() {
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/harshRESIZE.jpeg");
    quoteText.setVisible(true);
    quoteText.setText(" 'We're all miserable' - Harsh Patel");
  }

  public void toMarko(ActionEvent actionEvent) {
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/markoRESIZE.jpeg");
    quoteText.setVisible(true);
    quoteText.setText(" 'You're a broken turtle' - Marko Vila");
  }

  public void toJoselin(ActionEvent actionEvent) {
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/joselin2RESIZE.jpeg");
    quoteText.setVisible(true);
    quoteText.setText(" 'Make the UI hot' - Joselin Barbosa");
  }

  public void toDeepti(ActionEvent actionEvent) {
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/deeptiRESIZE.jpeg");
    quoteText.setVisible(true);
    quoteText.setText(" *INTERNAL SCREAMING* - Deepti Gosukonda");
  }

  public void toNick(ActionEvent actionEvent) {
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/nickRESIZE.jpeg");
    quoteText.setVisible(true);
    quoteText.setText(" 'I ruptured my ear at Scuba, should be there in 45' - Nick Biliouris");
  }

  public void toKody(ActionEvent actionEvent) {
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/kodyRESIZE.jpeg");
    quoteText.setVisible(true);
    quoteText.setText(" 'I am retiring from SceneBuilder' - Kody Robinson");
  }

  public void toTim(ActionEvent actionEvent) {
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/timRESIZE.jpeg");
    quoteText.setVisible(true);
    quoteText.setText(
        " 'Gonna be honest with you team, I've been bringing you guys down from the get-go' - Tim Klein");
  }

  public void toWill(ActionEvent actionEvent) {
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/willRESIZE.jpeg");
    quoteText.setVisible(true);
    quoteText.setText(" 'I love Wong' - William Doyle");
  }

  public void toMike(ActionEvent actionEvent) {
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/mikeRESIZE.jpeg");
    quoteText.setVisible(true);
    quoteText.setText(" 'Can front end update my UI?' - Mike Akstin");
  }

  public void toIain(ActionEvent actionEvent) {
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/iainRESIZE.jpeg");
    quoteText.setVisible(true);
    quoteText.setText(" ¯\\_(ツ)_/¯ - Iain McEwen");
  }

  public void toBelisha(ActionEvent actionEvent) {
    addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/belishaRESIZE.jpeg");
    quoteText.setVisible(true);
    quoteText.setText(" 'If it's Wong, it's right' - Belisha Genin");
  }
}
