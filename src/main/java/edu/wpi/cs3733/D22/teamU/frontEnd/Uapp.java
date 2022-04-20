package edu.wpi.cs3733.D22.teamU.frontEnd;

import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Uapp extends Application {

  public static boolean running = false;

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    Scene scene = getScene("edu/wpi/cs3733/D22/teamU/views/loginPage.fxml");
    running = true;
    URL a =
        Uapp.class.getClassLoader().getResource("edu/wpi/cs3733/D22/teamU/icons/hospitalIcon.png");
    primaryStage.getIcons().add(new Image(String.valueOf(a)));
    primaryStage.setTitle("Mass General Brigham");
    primaryStage.setScene(scene);
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  @Override
  public void stop() {
    running = false;
    log.info("Shutting Down");
  }

  public static Scene getScene(String pathFromResources) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader();
    // InputStream is = AppController.class.getClassLoader().getResourceAsStream(pathFromResources);

    Parent root = fxmlLoader.load(Uapp.class.getClassLoader().getResource(pathFromResources));
    return new Scene(root);
  }
}
