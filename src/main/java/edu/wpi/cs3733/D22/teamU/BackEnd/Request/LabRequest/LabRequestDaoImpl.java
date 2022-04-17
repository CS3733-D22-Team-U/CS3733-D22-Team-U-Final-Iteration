package edu.wpi.cs3733.D22.teamU.BackEnd.Request.LabRequest;

import edu.wpi.cs3733.D22.teamU.BackEnd.DataDao;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class LabRequestDaoImpl implements DataDao<LabRequest> {
  public Statement statement;
  public String csvFile;
  public HashMap<String, LabRequest> List = new HashMap<String, LabRequest>();
  public ArrayList<LabRequest> list = new ArrayList<LabRequest>();
  private Udb udb;

  public LabRequestDaoImpl(Statement statement, String csvFile) throws SQLException, IOException {
    this.csvFile = csvFile;
    this.statement = statement;
    this.udb = Udb.getInstance();
  }

  @Override
  public ArrayList<LabRequest> list() {
    return null;
  }

  @Override
  public HashMap<String, LabRequest> hList() {
    return List;
  }

  // Checks whether an employee exists
  // Returns Employee if exists
  // Returns empty employee with employee ID = N/A
  public Employee checkEmployee(String employee) {
    if (EmployeeDaoImpl.List.get(employee) != null) {
      return EmployeeDaoImpl.List.get(employee);
    } else {
      Employee empty = new Employee("N/A");
      return empty;
    }
  }

  /**
   * Reads CSV file and puts the Equipment into an array list: EquipmentList
   *
   * @throws IOException
   */
  public void CSVToJava() throws IOException {
    List = new HashMap<String, LabRequest>();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    String[] header = br.readLine().split(",");
    int columns = header.length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == columns) {
        LabRequest r =
            new LabRequest(
                row[0], row[2], row[3], row[4], checkEmployee(row[5]), row[6], row[7], row[8]);
        List.put(row[0], r);
        try {
          Location temp = new Location();
          temp.setNodeID(r.destination);
          Location l = udb.locationImpl.locations.get(udb.locationImpl.locations.indexOf(temp));
          l.addRequest(r);
          r.setLocation(l);
        } catch (Exception exception) {
        }
      }
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
    fw.append("Lab Type");
    fw.append(",");
    fw.append("Patient");
    fw.append(",");
    fw.append("Status");
    fw.append(",");
    fw.append("Staff");
    fw.append(",");
    fw.append("Destination");
    fw.append(",");
    fw.append("Date");
    fw.append(",");
    fw.append("Time");
    fw.append("\n");

    for (LabRequest request : List.values()) {
      fw.append(request.getID());
      fw.append(",");
      fw.append(request.getName());
      fw.append(",");
      fw.append(request.getPatientName());
      fw.append(",");
      fw.append(request.getStatus());
      fw.append(",");
      fw.append(request.getEmployee().getEmployeeID());
      fw.append(",");
      fw.append(request.getDestination());
      fw.append(",");
      fw.append(request.getDate());
      fw.append(",");
      fw.append(request.getTime());
      fw.append("\n");
    }
    fw.close();
  }

  public void JavaToSQL() {

    try {
      statement.execute("Drop table LabRequest");
    } catch (Exception e) {
      System.out.println("didn't drop table");
    }

    try {
      statement.execute(
          "CREATE TABLE LabRequest("
              + "ID varchar(10) not null,"
              + "labType varchar(50),"
              + "patient varchar(50) not null, "
              + "status varchar(15),"
              + "staff varchar(50) not null,"
              + "destination varchar(15),"
              + "date varchar(10) not null,"
              + "time varchar(10) not null)");

      for (LabRequest currLab : List.values()) {
        statement.execute(
            "INSERT INTO LabRequest VALUES("
                + "'"
                + currLab.getID()
                + "','"
                + currLab.getName()
                + "','"
                + currLab.getPatientName()
                + "','"
                + currLab.getStatus()
                + "','"
                + currLab.getEmployee().getEmployeeID()
                + "','"
                + currLab.getDestination()
                + "','"
                + currLab.getDate()
                + "','"
                + currLab.getTime()
                + "')");
      }
    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console.");
    }
  }

  public void SQLToJava() {
    List = new HashMap<String, LabRequest>();
    try {
      ResultSet results;
      results = statement.executeQuery("SELECT * FROM LabRequest");

      while (results.next()) {
        String id = results.getString("ID");
        String labType = results.getString("labType");
        String patient = results.getString("patient");
        String status = results.getString("status");
        String staff = results.getString("staff");
        String destination = results.getString("destination");
        String date = results.getString("date");
        String time = results.getString("time");

        LabRequest SQLRow =
            new LabRequest(
                id, labType, patient, status, checkEmployee(staff), destination, date, time);

        List.put(id, SQLRow);
      }
    } catch (SQLException e) {
      System.out.println("Database does not exist.");
    }
  }

  public void printTable() throws IOException {
    // csv to java
    CSVToJava();
    // display locations and attributes
    System.out.println(
        "ID |\t Lab Type |\t Patient |\t Status |\t Staff |\t Destination |\t Date |\t Time");
    for (LabRequest request : this.List.values()) {
      System.out.println(
          request.ID
              + " | \t"
              + " | \t"
              + request.name
              + request.patientName
              + " | \t"
              + " | \t"
              + request.status
              + request.employee.getEmployeeID()
              + " | \t"
              + request.destination
              + " | \t"
              + request.date
              + " | \t"
              + request.time);
    }
  }

  @Override
  public void edit(LabRequest data) throws IOException {
    // takes entries from SQL table that match input node and updates it with a new floor and
    // location type
    // input ID
    if (List.containsKey(data.ID)) {
      if (EmployeeDaoImpl.List.containsKey(data.getEmployee().getEmployeeID())) {
        data.setEmployee(EmployeeDaoImpl.List.get(data.getEmployee().getEmployeeID()));
        this.List.replace(data.ID, data);
        this.JavaToSQL();
        this.JavaToCSV(csvFile);
      } else {
        System.out.println("NO SUch STAFF");
      }
    } else {
      System.out.println("Doesn't Exist");
    }
  }
  /**
   * Prompts user for the information of a new lab request and then adds it to the csv file and
   * database
   *
   * @param
   * @throws IOException
   */
  @Override
  public void add(LabRequest data) throws IOException {
    if (List.containsKey(data.ID)) {
      System.out.println("A Request With This ID Already Exists");
    } else {
      if (EmployeeDaoImpl.List.containsKey(data.getEmployee().getEmployeeID())) {
        data.setEmployee(EmployeeDaoImpl.List.get(data.getEmployee().getEmployeeID()));
        this.List.put(data.ID, data);
        this.JavaToSQL();
        this.JavaToCSV(csvFile);
      } else {
        System.out.println("NO SUch STAFF");
      }
    }
  }
  // List //
  /**
   * Prompts user for the name of the item they wish to remove and then removes that item from the
   * database and csv file
   *
   * @throws IOException
   */
  @Override
  public void remove(LabRequest data) throws IOException {
    // removes entries from SQL table that match input node
    try {
      this.List.remove(data.ID);
      this.JavaToSQL();
      this.JavaToCSV(csvFile);
    } catch (Exception e) {
      System.out.println("This Data Point Was Not Found");
    }
  }

  @Override
  public int search(String id) {
    return 0;
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

  /*@Override
  public int search(String id) { // TODO search
    int index = -1;
    for (int i = 0; i < list().size(); i++) if (id.equals(list().get(i).ID)) index = i;
    return index;
  }*/

  public LabRequest askUser() throws NullPointerException {
    Scanner labInput = new Scanner(System.in);

    String inputID = "None";
    String inputStatus = "N/A";
    String inputPatient = "N/A";
    String inputStaff = "N/A";
    String inputType = "N/A";
    String inputDate = "N/A";
    String inputTime = "N/A";
    String inputDestination = "N/A";

    System.out.println("Input Lab ID: ");
    inputID = labInput.nextLine();

    System.out.println("Input Type: ");
    inputType = labInput.nextLine();

    System.out.println("Input Staff: ");
    inputStaff = labInput.nextLine();

    System.out.println("Input Destination: ");
    inputDestination = labInput.nextLine();

    Employee empty = new Employee(inputStaff);

    return new LabRequest(
        inputID,
        inputType,
        inputPatient,
        inputStatus,
        empty,
        inputDestination,
        inputDate,
        inputTime);
  }
}
