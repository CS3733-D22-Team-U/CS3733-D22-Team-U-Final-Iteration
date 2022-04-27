package edu.wpi.cs3733.D22.teamU.API;

import edu.wpi.cs3733.D22.teamU.API.Employee.DefaultEmployee;
import edu.wpi.cs3733.D22.teamU.API.Location.DefaultLocation;
import edu.wpi.cs3733.D22.teamU.API.MedicineData.DefaultImplementation;
import edu.wpi.cs3733.D22.teamU.API.MedicineData.MedicineDao;
import java.util.HashMap;

public abstract class Settings {
  static int xCoord = 0, yCoord = 0, windowWidth = 0, windowLength = 0;
  static String cssPath =
      String.valueOf(
          MedicineRequest.class
              .getClassLoader()
              .getResource("edu/wpi/cs3733/D22/teamU/cssFiles/default.css"));
  static String destLocationID;

  static DefaultEmployee current = new DefaultEmployee("");

  public static DefaultEmployee getCurrent() {
    return current;
  }

  static MedicineDao medicineActions = new DefaultImplementation();

  static HashMap<String, DefaultEmployee> authorizedEmployees = new HashMap<>();

  public static int getxCoord() {
    return xCoord;
  }

  public static int getyCoord() {
    return yCoord;
  }

  public static int getWindowWidth() {
    return windowWidth;
  }

  public static int getWindowLength() {
    return windowLength;
  }

  public static String getCssPath() {
    return cssPath;
  }

  public static String getDestLocationID() {
    return destLocationID;
  }

  public static MedicineDao getMedicineActions() {
    return medicineActions;
  }

  public static HashMap<String, DefaultEmployee> getAuthorizedEmployees() {
    return authorizedEmployees;
  }

  static HashMap<String, DefaultLocation> locations = new HashMap<>();

  public static HashMap<String, DefaultLocation> getLocations() {
    return locations;
  }
}
