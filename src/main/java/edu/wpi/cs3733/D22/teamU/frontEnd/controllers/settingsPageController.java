package edu.wpi.cs3733.D22.teamU.frontEnd.controllers;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.frontEnd.Uapp;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.TextField;



public class settingsPageController implements Initializable {

  ObservableList<String> URLList = FXCollections.observableArrayList();

  @FXML TextField changeText;
  @FXML ComboBox<String> comboImages;
  @FXML ImageView changeImage;
  private ArrayList<String> URLs;

  public void initialize(URL location, ResourceBundle resources) {
    URLList.add("Belisha Image");
    URLList.add("Deepti Image");
    URLList.add("Harsh Image");
    URLList.add("Iain Image");
    URLList.add("Joselin Image");
    URLList.add("Kody Image");
    URLList.add("Marko Image");
    URLList.add("Mike Image");
    URLList.add("Nick Image");
    URLList.add("Tim Image");
    URLList.add("Will Image");
    comboImages.setItems(URLList);
  }



  public void toApplicationSettings(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/applicationSettings.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }

  public void changeImage(ActionEvent actionEvent) throws IOException{
    if(comboImages.getValue().equals("Belisha Image")){
      addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/belishaRESIZE.jpeg");
    }
    else if(comboImages.getValue().equals("Deepti Image")){
      addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/deeptiRESIZE.jpeg");
    }
    else if(comboImages.getValue().equals("Harsh Image")){
      addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/harshRESIZE.jpeg");
    }
    else if(comboImages.getValue().equals("Iain Image")){
      addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/iainRESIZE.jpeg");
    }
    else if(comboImages.getValue().equals("Joselin Image")){
      addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/joselin2RESIZE.jpeg");
    }
    else if(comboImages.getValue().equals("Kody Image")){
      addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/kodyRESIZE.jpeg");
    }
    else if(comboImages.getValue().equals("Marko Image")){
      addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/markoRESIZE.jpeg");
    }
    else if(comboImages.getValue().equals("Mike Image")){
      addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/mikeRESIZE.jpeg");
    }
    else if(comboImages.getValue().equals("Nick Image")){
      addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/nickRESIZE.jpeg");
    }
    else if(comboImages.getValue().equals("Tim Image")){
      addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/timRESIZE.jpeg");
    }
    else if(comboImages.getValue().equals("Will Image")){
      addTeamImage("edu/wpi/cs3733/D22/teamU/images/groupMemberPics/willRESIZE.jpeg");
    }
  }
  private void addTeamImage(String resource) {
    URL a = Uapp.class.getClassLoader().getResource(resource);
    changeImage.setImage(new Image(String.valueOf(a)));
    // imageHover.setScaleX(1.75);
    // imageHover.setScaleY(1.75);
    // imageHover.setX((imageHover.getFitWidth()));
    // imageHover.setY((imageHover.getFitHeight()));

  }

  public void changeName(ActionEvent actionEvent) throws IOException{
    Employee user;
    try{
      user = Udb.getInstance().getUser();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    String newFirst = changeText.getText();
    user.setFirstName(newFirst);
    changeText.clear();

    //possibly add so username saves to CSV?
  }

  public void toHome(ActionEvent actionEvent) throws IOException {
    Scene scene = Uapp.getScene("edu/wpi/cs3733/D22/teamU/views/HomePage.fxml");
    Stage appStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    appStage.setScene(scene);
    appStage.show();
  }
}
