package edu.wpi.cs3733.D22.teamU.BackEnd.Location;

import edu.wpi.cs3733.D22.teamU.BackEnd.DataDao;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.EquipRequest.EquipRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.Request;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import edu.wpi.cs3733.D22.teamU.DBController;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class LocationDaoImpl implements DataDao<Location> {

  // make constant in locationDao
  public Statement statement;
  public ArrayList<Location> locations = new ArrayList<Location>();
  public String csvFile;

  /**
   * Contructor for LocationDaoImpl
   *
   * @param
   */
  public LocationDaoImpl(Statement statement, String csvFile) {
    this.csvFile = csvFile;
    this.statement = statement;
  }

  // Takes in a CSV file and converts it to java objects

  /**
   * Reads CSV file and puts the Locations into an array list: locations
   *
   * @throws IOException
   */
  @Override
  public void CSVToJava() throws IOException {
    locations = new ArrayList<Location>();
    String s;
    File file = new File(csvFile);
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
  }

  // This function converts all of the CSV information that is stored in Java objects and
  // puts them into the the SQL database

  @Override
  public ArrayList<Location> list() {
    return locations;
  }

  @Override
  public HashMap<String, Location> hList() {
    return null;
  }

  /**
   * Reads the array list: locations, then opens up a connection to the UDB database, then it
   * creates a new table called in the UDB database table: Locations. It then inserts the array
   * list: Locations into the UDB database table: Locations
   */
  public void JavaToSQL() {

    try {
      statement.execute("Drop table Locations");
    } catch (Exception e) {
      System.out.println("didn't drop table");
    }

    try {
      statement.execute(
          "CREATE TABLE Locations(nodeID varchar(18) not null, "
              + "xcoord int not null,"
              + "ycoord int not null,"
              + "floor varchar(3) not null,"
              + "building varchar(6),"
              + "nodeType varchar(6),"
              + "longName varchar(900) not null,"
              + "shortName varchar(600))");

      for (int j = 0; j < locations.size(); j++) {
        Location currLoc = locations.get(j);
        statement.execute(
            "INSERT INTO Locations VALUES("
                + "'"
                + currLoc.nodeID
                + "',"
                + currLoc.xcoord
                + ","
                + currLoc.ycoord
                + ",'"
                + currLoc.floor
                + "','"
                + currLoc.building
                + "','"
                + currLoc.nodeType
                + "','"
                + currLoc.longName
                + "','"
                + currLoc.shortName
                + "')");
      }
    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console.");
    }
  }

  // This function takes all of the SQL database information into java objects

  /**
   * Clears the array list: locations and then reads the UDB database table: Locations then copies
   * the to the cleared array list
   *
   * @throws SQLException
   */
  @Override
  public void SQLToJava() {
    locations = new ArrayList<Location>();

    try {
      ResultSet results;
      results = statement.executeQuery("SELECT * FROM Locations");

      while (results.next()) {
        String nodeID = results.getString("nodeID");
        int xcoord = results.getInt("xcoord");
        int ycoord = results.getInt("ycoord");
        String floor = results.getString("floor");
        String building = results.getString("building");
        String nodeType = results.getString("nodeType");
        String longName = results.getString("longName");
        String shortName = results.getString("shortName");

        Location SQLRow = new Location();
        SQLRow.nodeID = nodeID;
        SQLRow.xcoord = xcoord;
        SQLRow.ycoord = ycoord;
        SQLRow.floor = floor;
        SQLRow.building = building;
        SQLRow.nodeType = nodeType;
        SQLRow.longName = longName;
        SQLRow.shortName = shortName;

        locations.add(SQLRow);
      }
    } catch (SQLException e) {
      System.out.println("location does not exist.");
    }
  }

  // This function converts the java objects of our CSV data into a new CSV file

  /**
   * Copies the array list: locations and writes it into the CSV file
   *
   * @param csvFile
   * @throws IOException
   */
  @Override
  public void JavaToCSV(String csvFile) throws IOException {
    PrintWriter fw = new PrintWriter(new File(csvFile));

    fw.append("nodeID");
    fw.append(",");
    fw.append("xcoord");
    fw.append(",");
    fw.append("ycoord");
    fw.append(",");
    fw.append("floor");
    fw.append(",");
    fw.append("building");
    fw.append(",");
    fw.append("nodeType");
    fw.append(",");
    fw.append("longName");
    fw.append(",");
    fw.append("shortName");
    fw.append("\n");

    for (int i = 0;
        i < locations.size();
        i++) { // ask about how this was working without and = sign
      fw.append(locations.get(i).nodeID);
      fw.append(",");
      fw.append(Integer.toString(locations.get(i).xcoord));
      fw.append(",");
      fw.append(Integer.toString(locations.get(i).ycoord));
      fw.append(",");
      fw.append(locations.get(i).floor);
      fw.append(",");
      fw.append(locations.get(i).building);
      fw.append(",");
      fw.append(locations.get(i).nodeType);
      fw.append(",");
      fw.append(locations.get(i).longName);
      fw.append(",");
      fw.append(locations.get(i).shortName);
      fw.append("\n");
    }
    fw.close();
  }

  /** Prints out the Contents of the CSV file TowerLocations.csv */
  @Override
  public void printTable() throws IOException {
    // csv to java
    CSVToJava();
    // display locations and attributes
    System.out.println(
        "Node |\t X |\t Y |\t Level |\t Building |\t Type |\t Long Name |\t Short Name");
    for (Location location : this.locations) {
      System.out.println(
          location.nodeID
              + " | \t"
              + location.xcoord
              + " | \t"
              + location.ycoord
              + " | \t"
              + location.floor
              + " | \t"
              + location.building
              + " | \t"
              + location.nodeType
              + " | \t"
              + location.longName
              + " | \t"
              + location.shortName);
    }
  }

  /**
   * Asks user for nodeID they wish to edit and then ask to change the floor and the node type, it
   * then changes the values in the database and csv file
   *
   * @throws IOException
   * @throws SQLException
   */
  @Override
  public void edit(Location data) throws IOException {
    // takes entries from SQL table that match input node and updates it with a new floor and
    // location type
    // input ID
    try {
      list().set(search(data.nodeID), data);
      this.JavaToSQL(); // t
      this.JavaToCSV(csvFile); // t
    } catch (Exception e) {
      System.out.println("This Object Does Not Exist");
    }
  }

  /**
   * Prompts user for the nodeID of a new room and then adds it to the csv file and database
   *
   * @throws IOException
   * @throws SQLException
   */
  @Override
  public void add(Location data) throws IOException {
    // add a new entry to the SQL table
    try {
      locations.get(search(data.nodeID));
      System.out.println("An Object With This ID Already Exists");
    } catch (Exception e) {
      Location newLocation = data;
      this.locations.add(newLocation);
      this.JavaToSQL();
      this.JavaToCSV(csvFile);
    }
  }

  /**
   * Prompts user for the nodeID of the room they wish to remove and then removes that room from the
   * database and csv file
   *
   * @throws IOException
   * @throws SQLException
   */
  @Override
  public void remove(Location data) throws IOException {
    // removes entries from SQL table that match input node
    Udb udb = DBController.udb;
    try {
      Location temp = locations.get(search(data.nodeID));
      System.out.println(data.getRequests().size());
      for (Request e : temp.getRequests()) {
        if (e instanceof EquipRequest) udb.equipRequestImpl.hList().remove(e.getID());
      }
      udb.equipRequestImpl.JavaToCSV(udb.equipRequestImpl.csvFile);
      udb.equipRequestImpl.JavaToSQL();
      this.locations.remove(search(data.nodeID));
      this.JavaToSQL();
      this.JavaToCSV(csvFile);
    } catch (Exception e) {
      System.out.println("This Data Point Was Not Found");
    }
  }

  @Override
  public int search(String id) { // TODO search
    int index = -1;
    for (int i = 0; i < list().size(); i++) if (id.equals(list().get(i).nodeID)) index = i;
    return index;
  }

  /**
   * Prompts user for the name of a new file and then creates the new file in the project folder
   * then it copies the database table: EquipmentList into the CSV file
   *
   * @throws SQLException
   */
  public void saveTableAsCSV(String CSVName) throws SQLException {
    // takes entries from SQL table and an input name, from there it makes a new CSV file

    String csvFilePath = "./" + CSVName + ".csv";

    try {
      new File(csvFilePath);
      this.SQLToJava();
      this.JavaToCSV(csvFilePath);

    } catch (IOException e) {
      System.out.println(e.fillInStackTrace());
    }
  }

  public Location askUser() {
    Scanner locationInput = new Scanner(System.in);

    String nodeID = "None";
    int xcoord = 0;
    int ycoord = 0;
    String floor = "N/A";
    String building = "N/A";
    String nodeType = "N/A";
    String longName = "N/A";
    String shortName = "N/A";

    System.out.println("Input node ID: ");
    nodeID = locationInput.nextLine();

    System.out.println("Input floor: ");
    floor = locationInput.nextLine();

    return new Location(nodeID, xcoord, ycoord, floor, building, nodeType, longName, shortName);
  }
}
