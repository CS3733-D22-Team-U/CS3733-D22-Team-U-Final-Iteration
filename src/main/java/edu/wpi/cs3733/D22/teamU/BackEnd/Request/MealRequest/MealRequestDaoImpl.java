package edu.wpi.cs3733.D22.teamU.BackEnd.Request.MealRequest;

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

public class MealRequestDaoImpl implements DataDao<MealRequest> {
  public Statement statement;
  public HashMap<String, MealRequest> List = new HashMap<String, MealRequest>();
  public String csvFile;

  public MealRequestDaoImpl(Statement statement, String csvFile) throws SQLException, IOException {
    this.csvFile = csvFile;
    this.statement = statement;
  }

  public Employee checkEmployee(String employee) throws NullPointerException {
    if (EmployeeDaoImpl.List.get(employee) != null) {
      return EmployeeDaoImpl.List.get(employee);
    } else {
      Employee empty = new Employee("N/A");
      return empty;
    }
  }

  @Override
  public ArrayList<MealRequest> list() {
    return null;
  }

  @Override
  public HashMap<String, MealRequest> hList() {
    return this.List;
  }

  public void CSVToJava() throws IOException {
    List = new HashMap<String, MealRequest>();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    int size = br.readLine().split(",").length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == size) {
        MealRequest r =
            new MealRequest(
                row[0],
                row[1],
                row[2],
                row[3],
                checkEmployee(row[4]),
                row[5],
                row[6],
                row[7],
                row[8]);
        List.put(row[0], r);

        try {
          Location temp = new Location();
          temp.setNodeID(r.destination);
          Location l =
              Udb.getInstance()
                  .locationImpl
                  .locations
                  .get(Udb.getInstance().locationImpl.locations.indexOf(temp));
          l.addRequest(r);
          r.setLocation(l);
        } catch (Exception exception) {
        }
      }
    }
  }

  public void CSVToJava(ArrayList<Location> locations) throws IOException {
    List = new HashMap<String, MealRequest>();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    int size = br.readLine().split(",").length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == size) {
        MealRequest r =
            new MealRequest(
                row[0],
                row[1],
                row[2],
                row[3],
                checkEmployee(row[4]),
                row[5],
                row[6],
                row[7],
                row[8]);
        List.put(row[0], r);

        try {
          Location temp = new Location();
          temp.setNodeID(r.destination);
          Location l = locations.get(locations.indexOf(temp));
          l.addRequest(r);
          r.setLocation(l);
        } catch (Exception exception) {
        }
      }
    }
  }

  @Override
  public void JavaToSQL() {
    try {
      statement.execute("Drop table MealRequest");
    } catch (Exception e) {
      System.out.println("didn't drop table");
    }

    try {
      statement.execute(
          "CREATE TABLE MealRequest("
              + "ID varchar(10) not null,"
              + "patientName varchar(20) not null,"
              + "dietRest varchar(100) not null,"
              + "status varchar(15) not null,"
              + "employee varchar(20) not null,"
              + "destination varchar(15) not null,"
              + "addNotes varchar(200) not null,"
              + "date varchar(10) not null,"
              + "time varchar(10) not null)");
      for (MealRequest currMeal : List.values()) {
        statement.execute(
            "INSERT INTO MealRequest VALUES("
                + "'"
                + currMeal.getID()
                + "','"
                + currMeal.getPatientName()
                + "','"
                + currMeal.getDietRest()
                + "','"
                + currMeal.getStatus()
                + "','"
                + currMeal.getEmployee().getEmployeeID()
                + "','"
                + currMeal.getDestination()
                + "','"
                + currMeal.getAddNotes()
                + "','"
                + currMeal.getDate()
                + "','"
                + currMeal.getTime()
                + "')");
      }
    } catch (SQLException e) {
      System.out.println("JavaToSQL error in MealRequestImp");
    }
  }

  @Override
  public void SQLToJava() {
    List = new HashMap<String, MealRequest>();
    try {
      ResultSet results;
      results = statement.executeQuery("SELECT * FROM MealRequest");
      while (results.next()) {
        String id = results.getString("ID");
        String patientName = results.getString("patientName");
        String dietRest = results.getString("dietRest");
        String status = results.getString("status");
        String employee = results.getString("employee");
        String destination = results.getString("destination");
        String addNotes = results.getString("addNotes");
        String date = results.getString("date");
        String time = results.getString("time");

        MealRequest SQLRow =
            new MealRequest(
                id,
                patientName,
                dietRest,
                status,
                checkEmployee(employee),
                destination,
                addNotes,
                date,
                time);

        List.put(id, SQLRow);
      }
    } catch (SQLException e) {
      System.out.println("request not found");
    }
  }

  @Override
  public void JavaToCSV(String csvFile) throws IOException {
    PrintWriter fw = new PrintWriter(new File(csvFile));

    fw.append("ID");
    fw.append(",");
    fw.append("Patient");
    fw.append(",");
    fw.append("Diet Restrictions");
    fw.append(",");
    fw.append("Status");
    fw.append(",");
    fw.append("Staff");
    fw.append(",");
    fw.append("Destination");
    fw.append(",");
    fw.append("Additional Notes");
    fw.append(",");
    fw.append("Date");
    fw.append(",");
    fw.append("Time");
    fw.append("\n");

    for (MealRequest request : List.values()) {
      fw.append(request.getID());
      fw.append(",");
      fw.append(request.getPatientName());
      fw.append(",");
      fw.append(request.getDietRest());
      fw.append(",");
      fw.append(request.getStatus());
      fw.append(",");
      fw.append(request.getEmployee().getEmployeeID());
      fw.append(",");
      fw.append(request.getDestination());
      fw.append(",");
      fw.append(request.getAddNotes());
      fw.append(",");
      fw.append(request.getDate());
      fw.append(",");
      fw.append(request.getTime());
      fw.append("\n");
    }
    fw.close();
  }

  @Override
  public void printTable() throws IOException { // csv to java
    CSVToJava();
    // display locations and attributes
    System.out.println(
        "ID |\t Patient |\t Diet Restrictions |\t Status |\t Staff |\t Destination |\t Additional Notes |\t Date |\t Time");
    for (MealRequest request : this.List.values()) {
      System.out.println(
          request.ID
              + " | \t"
              + request.patientName
              + " | \t"
              + request.dietRest
              + " | \t"
              + request.dietRest
              + " | \t"
              + request.status
              + " | \t"
              + request.employee.getEmployeeID()
              + " | \t"
              + request.destination
              + " | \t"
              + request.addNotes
              + " | \t"
              + request.date
              + " | \t"
              + request.time
              + " | \t");
    }
  }

  @Override
  public void edit(MealRequest data) throws IOException, SQLException {
    if (List.containsKey(data.ID)) {
      if (EmployeeDaoImpl.List.containsKey(data.getEmployee().getEmployeeID())) {
        data.updateLocation(data.destination, Udb.getInstance().locationImpl.list());
        data.setEmployee(EmployeeDaoImpl.List.get(data.getEmployee().getEmployeeID()));
        this.List.replace(data.ID, data);
        this.JavaToSQL();
        this.JavaToCSV(csvFile);
      } else {
        System.out.println("No Such Employee Exists in Database");
      }
    } else {
      System.out.println("A Request With This ID Already Does Not Exist");
    }
  }

  @Override
  public void add(MealRequest data) throws IOException, SQLException {
    if (List.containsKey(data.ID)) {
      System.out.println("A Request With This ID Already Exists");
    } else {
      if (EmployeeDaoImpl.List.containsKey(data.getEmployee().getEmployeeID())) {
        data.updateLocation(data.destination, Udb.getInstance().locationImpl.list());
        data.setEmployee(EmployeeDaoImpl.List.get(data.getEmployee().getEmployeeID()));
        this.List.put(data.ID, data);
        this.JavaToSQL();
        this.JavaToCSV(csvFile);
      } else {
        System.out.println("No Such Employee Exists in Database");
      }
    }
  }

  @Override
  public void remove(MealRequest data) throws IOException {

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

  @Override
  public void saveTableAsCSV(String CSVName) throws SQLException {
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
  public MealRequest askUser() {
    Scanner reqInput = new Scanner(System.in);

    String inputID;
    String inputPatient = "N/A";
    String inputDietRest = "N/A";
    String inputStatus = "N/A";
    String inputStaff = "N/A";
    String inputDestination = "FDEPT00101";
    String inputAddNotes = "N/A";
    String inputDate = "N/A";
    String inputTime = "N/A";

    System.out.println("Input Request ID: ");
    inputID = reqInput.nextLine();

    System.out.println("Input Patient Name: ");
    inputPatient = reqInput.nextLine();

    System.out.println("Input Diet Restriction: ");
    inputDietRest = reqInput.nextLine();

    System.out.println("Input Location Node: ");
    inputDestination = reqInput.nextLine();

    System.out.println("Input Staff name: ");
    inputStaff = reqInput.nextLine();

    Employee empty = new Employee(inputStaff);

    return new MealRequest(
        inputID,
        inputPatient,
        inputDietRest,
        inputStatus,
        empty,
        inputDestination,
        inputAddNotes,
        inputDate,
        inputTime);
  }
}
