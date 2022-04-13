/*package edu.wpi.cs3733.D22.teamU;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import java.io.IOException;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;

public class Testing {

  String locTestCSV;
  String[] CSVfiles = {
    "csvTables/TowerLocations.csv",
    "csvTables/TowerEmployees.csv",
    "csvTables/TowerEquipment.csv",
    "csvTables/TowerEquipmentRequests.csv",
    "csvTables/TESTTowerLocations.csv"
  };
  Udb udb = new Udb("admin", "admin", CSVfiles);

  public String DB_LOC = "jdbc:derby:UDB;";

  public Testing() throws SQLException, IOException {}

  @Test
  public void locCSVToJavaTest() throws IOException, NullPointerException {

    // djd
    // System.out.println("Testing location csv to java");
    udb.locationImpl.CSVToJava();
    String[] answer = {"TEST", "100", "200", "HELL", "jjjj", "jUNIT", "jUNITSUCKS"};
    int count = 0;
    assertEquals(udb.locationImpl.locations.get(0), answer);
    // System.out.println();
    assertEquals(0, 0);
    /*String[] answer = {
          "FDEPT00101", "1617", "825", "1", "Tower", "DEPT", "Center for International Medicine", "CIM"
        };
        assertEquals(udb.locationImpl.locations.get(0).getNodeID(), answer[0]);
        // System.out.println();
      }

      @Test
      public void locJavaToCSVTest() throws IOException {
        /*File file = new File(locTestCSV);
        udb.locationImpl.JavaToCSV(locTestCSV);
        String[] answer = {"TEST", "100", "200", "HELL", "jjjj", "jUNIT", "jUNITSUCKS"};
    <<<<<<< HEAD
    =======
    =======
        File file = new File(CSVfiles[4]);
        udb.locationImpl.JavaToCSV(CSVfiles[4]);
        String[] answer = {
          "FDEPT00101", "1617", "825", "1", "Tower", "DEPT", "Center for International Medicine", "CIM"
        };
    >>>>>>> b44cfc0692bc42026b4a4ed09830c36777e40920
        BufferedReader br = new BufferedReader(new FileReader(file));
        br.readLine();
        String s = br.readLine();
        String[] row = s.split(",");
        assertEquals(answer[0], row[0]);
      }

      @Test
      public void locJavaToSQLTest() throws SQLException {
        // locationImpl.JavaToSQL();
        /*locationImpl.JavaToSQL();
    >>>>>>> 4300e8f597c5995a3cf62c6096b88d3be552f559
        boolean testResults = true;

        Connection connection = null;
        connection = DriverManager.getConnection(DB_LOC);

        Statement locTestStatement = connection.createStatement();

        try {
          locTestStatement.executeQuery("SELECT nodeID FROM Locations WHERE nodeID = 'TEST'");
        } catch (Exception e) {

        }*/
 /* }

   @Test
   public void locSQLToJavaTest() {}

   @Test
   public void locEditTest() {}

   @Test
   public void locAddTest() {}
   /*
   =======
     public void locAddTest() throws SQLException, IOException {
       String testAddLocation = "newTestPoint";
       udb.locationImpl.addLoc(testAddLocation, CSVfiles[4]);
       Location comparingAddedLocation =
           new Location("newTestPoint", 0, 0, "N/A", "N/A", "N/A", "N/A", "N/A");
       ArrayList<Location> locations = new ArrayList<>();

       String s;
       File file = new File(CSVfiles[4]);
       BufferedReader br = new BufferedReader(new FileReader(file));
       br.readLine();
       while ((s = br.readLine()) != null) {
         String[] row = s.split(",");
         if (row.length == 8)
           locations.add(
               new Location(
                   row[0],
                   Integer.parseInt(row[1]),
                   Integer.parseInt(row[2]),
                   row[3],
                   row[4],
                   row[5],
                   row[6],
                   row[7]));
       }

       Location newAddedLocationFromCSV =
           new Location(
               locations.get(locations.size() - 1).getNodeID(),
               locations.get(locations.size() - 1).getXcoord(),
               locations.get(locations.size() - 1).getYcoord(),
               locations.get(locations.size() - 1).getFloor(),
               locations.get(locations.size() - 1).getBuilding(),
               locations.get(locations.size() - 1).getNodeType(),
               locations.get(locations.size() - 1).getLongName(),
               locations.get(locations.size() - 1).getShortName());

       assertEquals(newAddedLocationFromCSV.getNodeID(), comparingAddedLocation.getNodeID());
       assertEquals(newAddedLocationFromCSV.getXcoord(), comparingAddedLocation.getXcoord());
       assertEquals(newAddedLocationFromCSV.getYcoord(), comparingAddedLocation.getYcoord());
     }

   >>>>>>> b44cfc0692bc42026b4a4ed09830c36777e40920
     @Test
     public void locRemoveTest() throws SQLException, IOException {
   <<<<<<<HEAD
               // locationImpl.removeLoc(locTestCSV);
               // ArrayList<Location> testLocationArray = locationImpl.locations;
               =======
       ArrayList<Location> baseLocationArray = udb.locationImpl.locations;
       udb.locationImpl.removeLoc("TEST", CSVfiles[4]);
       ArrayList<Location> testLocationArray = udb.locationImpl.locations;
   >>>>>>>4300e8f 597 c5995a3cf62c6096b88d3be552f559
       // remove TEST node
       assertNotEquals(baseLocationArray, testLocationArray, "hasn't deleted");
     }

     @Test
     public void locMakeCSVTest() {
     }

     // ----------------------- Equipment test
     @Test
   <<<<<<<HEAD

     public void equipmentCSVToJavaTest() throws IOException {
       //// locationImpl.CSVToJava(locTestCSV);
   =======
       public void equipmentCSVToJavaTest () {
         // locationImpl.CSVToJava(locTestCSV);
   >>>>>>>4300e8f 597 c5995a3cf62c6096b88d3be552f559
       }

       @Test
       public void equipmentJavaToCSVTest () {
       }

       @Test
       public void equipmentJavaToSQLTest () throws SQLException {
         // locationImpl.JavaToSQL();
       /*locationImpl.JavaToSQL();
   >>>>>>> 4300e8f597c5995a3cf62c6096b88d3be552f559

        Connection connection = null;
       // connection = DriverManager.getConnection(DB_LOC + "user=" + username + ";password=" + password + ";");

        Statement locTestStatement = connection.createStatement();

        try {
          locTestStatement.executeQuery("SELECT nodeID FROM Locations WHERE nodeID = 'TEST1'");
        } catch (Exception e) {
          //testResults = false;
        }

        //assertEquals(true, testResults);}


       // }
     // }
   // }
 */
