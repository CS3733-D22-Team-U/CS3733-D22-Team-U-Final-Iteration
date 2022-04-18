package edu.wpi.cs3733.D22.teamU.BackEnd.Equipment;

import edu.wpi.cs3733.D22.teamU.BackEnd.DataDao;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class EquipmentDaoImpl implements DataDao<Equipment> {
  public Statement statement;
  public HashMap<String, Equipment> EquipmentList = new HashMap<String, Equipment>();
  public String csvFile;
  // private Udb udb = DBController.udb;

  /**
   * Constructor for EquipmentDaoImpl
   *
   * @param
   */
  public EquipmentDaoImpl(Statement statement, String csvFile) {
    this.statement = statement;
    this.csvFile = csvFile;
  }

  @Override
  public ArrayList<Equipment> list() {
    return null;
  }

  @Override
  public HashMap<String, Equipment> hList() {
    return EquipmentList;
  }

  /**
   * Reads CSV file and puts the Equipment into an array list: EquipmentList
   *
   * @throws IOException
   */
  public void CSVToJava(ArrayList<Location> locations) throws IOException {
    EquipmentList = new HashMap<String, Equipment>();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    br.readLine();
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == 5) {
        Equipment e =
            new Equipment(
                row[0], row[1], Integer.parseInt(row[2]), Integer.parseInt(row[3]), row[5]);
        try {
          Location temp = new Location();
          temp.setNodeID(e.locationID);
          Location l = locations.get(locations.indexOf(temp));
          l.addEquipment(e);
          e.setLocation(l);
        } catch (Exception exception) {

        }
        EquipmentList.put(e.ID, e);
      }
    }
  }

  @Override
  public void CSVToJava() throws IOException, SQLException {
    EquipmentList = new HashMap<String, Equipment>();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    int size = br.readLine().split(",").length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == size) {
        Equipment e =
            new Equipment(
                row[0], row[1], Integer.parseInt(row[2]), Integer.parseInt(row[3]), row[5]);
        Location l =
            Udb.getInstance()
                .locationImpl
                .list()
                .get(Udb.getInstance().locationImpl.list().indexOf(e.locationID));
        l.addEquipment(e);
        EquipmentList.put(e.ID, e);
      }
    }
  }

  /**
   * Reads the array list: EquipmentList, then opens up a connection to the UDB database, then it
   * creates a new table called in the UDB database table: EquipmentList. It then inserts the array
   * list: EquipmentList into the UDB database table: EquipmentList
   */
  public void JavaToSQL() {

    try {
      statement.execute("Drop table EquipmentList");
    } catch (Exception e) {
      System.out.println("didn't drop table");
    }
    try {
      statement.execute(
          "CREATE TABLE EquipmentList(name varchar(18) not null, "
              + "amount int not null,"
              + "inUse int not null,"
              + "available int not null,"
              + "locationID varchar(18) not null)");

      for (Equipment currLoc : EquipmentList.values()) {
        statement.execute(
            "INSERT INTO EquipmentList VALUES("
                + "'"
                + currLoc.getID()
                + "','"
                + currLoc.getName()
                + "',"
                + currLoc.getAmount()
                + ","
                + currLoc.getInUse()
                + ","
                + currLoc.getAvailable()
                + ",'"
                + currLoc.getLocationID()
                + "')");
      }
    } catch (SQLException e) {
      Udb.admin = false;
      System.out.println("Connection failed. Check output console.");
      e.printStackTrace();
    }
  }

  /**
   * Clears the array list: EquipmentList and then reads the UDB database table: EquipmentList then
   * copies the to the cleared array list
   *
   * @throws SQLException
   */
  @Override
  public void SQLToJava() {
    EquipmentList = new HashMap<String, Equipment>();

    try {
      ResultSet results;
      results = statement.executeQuery("SELECT * FROM EquipmentList");

      while (results.next()) {
        String ID = results.getString("ID");
        String name = results.getString("Name");
        int amount = results.getInt("Amount");
        int inUse = results.getInt("InUse");
        String locationID = results.getString("locationID");

        Equipment SQLRow = new Equipment(ID, name, amount, inUse, locationID);

        EquipmentList.put(ID, SQLRow);
      }
    } catch (SQLException e) {
      System.out.println(e);
    }
  }

  /**
   * Copies the array list: EquipmentList and writes it into the CSV file
   *
   * @param csvFile
   * @throws IOException
   */
  public void JavaToCSV(String csvFile) throws IOException {
    PrintWriter fw = new PrintWriter(new File(csvFile));

    fw.append("ID");
    fw.append(",");
    fw.append("Name");
    fw.append(",");
    fw.append("Amount");
    fw.append(",");
    fw.append("In Use");
    fw.append(",");
    fw.append("Available");
    fw.append("\n");

    for (Equipment equipment : EquipmentList.values()) {
      fw.append(equipment.getID());
      fw.append(",");
      fw.append(equipment.getName());
      fw.append(",");
      fw.append(Integer.toString(equipment.getAmount()));
      fw.append(",");
      fw.append(Integer.toString(equipment.getInUse()));
      fw.append(",");
      fw.append(Integer.toString(equipment.getAvailable()));
      fw.append("\n");
    }
    fw.close();
  }

  @Override
  public void printTable() throws IOException, SQLException {
    // csv to java
    CSVToJava();
    // display equipment and attributes
    System.out.println("ID |\t Name |\t Amount |\t In Use |\t Available");
    for (Equipment equipment : EquipmentList.values()) {
      System.out.println(
          equipment.getID()
              + " | \t"
              + equipment.getName()
              + " | \t"
              + equipment.getAmount()
              + " | \t"
              + equipment.getInUse()
              + " | \t"
              + equipment.getAvailable()
              + " | \t");
    }
  }

  /**
   * Prompts user for the name of a new item and then adds it to the csv file and database
   *
   * @throws IOException
   * @throws SQLException
   */
  @Override
  public void add(Equipment data) throws IOException {
    // adds entry to SQL table
    if (EquipmentList.containsKey(data.ID)) {
      // EquipmentList.get(search(data.ID));
      System.out.println("An Equipment With This Name Already Exists");
    } else {
      Equipment newEquipment = data;
      this.EquipmentList.put(newEquipment.ID, newEquipment);
      this.JavaToSQL();
      this.JavaToCSV(csvFile);
    }
  }

  /**
   * Prompts user for the name of the item they wish to remove and then removes that item from the
   * database and csv file
   *
   * @throws IOException
   * @throws SQLException
   */
  @Override
  public void remove(Equipment data) throws IOException {
    if (EquipmentList.containsKey(data.ID)) {
      this.EquipmentList.remove(data.ID);
      this.JavaToSQL();
      this.JavaToCSV(csvFile);
    } else {
      System.out.println("This Equipment Was Not Found");
    }
  }

  /**
   * Asks user for name of item they wish to edit and then ask to change the total amount and the
   * amount in use, Then changes the values in the database and csv file
   *
   * @throws IOException
   * @throws SQLException
   */
  @Override
  public void edit(Equipment data) throws IOException {
    // takes entries from SQL table that match input node and updates it amount and it's use
    if (EquipmentList.containsKey(data.ID)) {
      hList().replace(data.ID, data);
      this.JavaToSQL();
      this.JavaToCSV(csvFile);
    } else {
      System.out.println("This Equipment Does Not Exist");
    }
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

  @Override
  public int search(String name) { // Searches for Equipment w/ matching String Name
    /*int index = -1;
    for (int i = 0; i < list().size(); i++) if (name.equals(list().get(i).Name)) index = i;
    return index;*/
    return -1;
  }

  public Equipment askUser() {
    Scanner equipInput = new Scanner(System.in);

    String inputID = "None";
    String inputName = "None";
    String inputLocationID = "test";

    System.out.println("Input equipment ID: ");
    inputID = equipInput.nextLine();

    System.out.println("Input equipment name: ");
    inputName = equipInput.nextLine();

    System.out.println("Input valid locationID: ");
    inputLocationID = equipInput.nextLine();

    return new Equipment(inputID, inputName, inputLocationID);
  }
}
