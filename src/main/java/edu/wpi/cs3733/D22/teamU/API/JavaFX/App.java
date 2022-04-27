package edu.wpi.cs3733.D22.teamU.API.JavaFX;

import edu.wpi.cs3733.D22.teamU.API.Exception.ServiceException;
import edu.wpi.cs3733.D22.teamU.API.MedicineRequest;
import edu.wpi.cs3733.D22.teamU.API.Settings;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException, ServiceException {
    MedicineRequest.run(
        Settings.getxCoord(), Settings.getyCoord(), 1280, 720, Settings.getCssPath(), "");
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
