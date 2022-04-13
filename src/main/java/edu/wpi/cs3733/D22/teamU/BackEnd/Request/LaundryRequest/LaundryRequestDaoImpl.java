package edu.wpi.cs3733.D22.teamU.BackEnd.Request.LaundryRequest;

import edu.wpi.cs3733.D22.teamU.BackEnd.DataDao;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class LaundryRequestDaoImpl implements DataDao<LaundryRequest> {
  public Statement statement;
  public String csvFile;
  public HashMap<String, LaundryRequest> List = new HashMap<String, LaundryRequest>();
  public ArrayList<LaundryRequest> list = new ArrayList<LaundryRequest>();

  public LaundryRequestDaoImpl(Statement statement, String csvFile) {
    this.csvFile = csvFile;
    this.statement = statement;
  }

  @Override
  public ArrayList<LaundryRequest> list() {
    return null;
  }

  @Override
  public HashMap<String, LaundryRequest> hList() {
    return this.List;
  }

  // CHecks whether an employee exists
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

  @Override
  public void CSVToJava() throws IOException {
    List = new HashMap<String, LaundryRequest>();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    String[] header = br.readLine().split(",");
    int columns = header.length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == columns) {
        List.put(
            row[0],
            new LaundryRequest(
                row[0], row[1], row[2], checkEmployee(row[3]), row[4], row[5], row[6], row[7]));
      }
    }
  }

  // string
  // map.get(row
  @Override
  public void JavaToSQL() {
    try {
      statement.execute("Drop table LaundryRequest");
    } catch (Exception e) {
      System.out.println("didn't drop table");
    }

    try {
      statement.execute(
          "CREATE TABLE LaundryRequest("
              + "ID varchar(10) not null,"
              + "name varchar(20) not null,"
              + "patientName varchar(20) not null,"
              + "staff varchar(20) not null,"
              + "status varchar(20) not null,"
              + "location varchar(15) not null,"
              + "date varchar(10) not null,"
              + "time varchar(10) not null)");

      for (LaundryRequest currLaud : List.values()) {
        statement.execute(
            "INSERT INTO LaundryRequest VALUES("
                + "'"
                + currLaud.getID()
                + "','"
                + currLaud.getName()
                + "','"
                + currLaud.getPatientName()
                + "','"
                + currLaud.getEmployee().getEmployeeID()
                + "','"
                + currLaud.getStatus()
                + "','"
                + currLaud.getLocation()
                + "','"
                + currLaud.getDate()
                + "','"
                + currLaud.getTime()
                + "')");
      }
    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console.");
    }
  }

  @Override
  public void SQLToJava() {
    List = new HashMap<String, LaundryRequest>();
    try {
      ResultSet results;
      results = statement.executeQuery("SELECT * FROM LaundryRequest");

      while (results.next()) {
        String ID = results.getString("ID");
        String name = results.getString("name");
        String patientName = results.getString("patientName");
        String staff = results.getString("staff");
        String status = results.getString("status");
        String location = results.getString("location");
        String date = results.getString("date");
        String time = results.getString("time");

        LaundryRequest SQLRow =
            new LaundryRequest(
                ID, name, patientName, checkEmployee(staff), status, location, date, time);

        List.put(ID, SQLRow);
      }
    } catch (SQLException e) {
      System.out.println("Database does not exist.");
    }
  }

  @Override
  public void JavaToCSV(String csvFile) throws IOException {
    PrintWriter fw = new PrintWriter(new File(csvFile));

    fw.append("ID");
    fw.append(",");
    fw.append("Type");
    fw.append(",");
    fw.append("PatientName");
    fw.append(",");
    fw.append("Staff");
    fw.append(",");
    fw.append("Status");
    fw.append(",");
    fw.append("Location");
    fw.append(",");
    fw.append("Date");
    fw.append(",");
    fw.append("Time");
    fw.append(",");
    fw.append("\n");

    for (LaundryRequest request : List.values()) {
      fw.append(request.getID());
      fw.append(",");
      fw.append(request.getName());
      fw.append(",");
      fw.append(request.getPatientName());
      fw.append(",");
      fw.append(request.getEmployee().getEmployeeID());
      fw.append(",");
      fw.append(request.getStatus());
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

  @Override
  public void printTable() throws IOException {
    CSVToJava();
    System.out.println(
        "ID |\t Type |\t Patient Name |\t Staff |\t Status |\t Location |\t Date |\t Time");
    for (LaundryRequest request : this.List.values()) {
      System.out.println(
          request.ID
              + " | \t"
              + request.name
              + " | \t"
              + request.patientName
              + " | \t"
              + request.getEmployee().getEmployeeID()
              + " | \t"
              + request.status
              + " | \t"
              + request.location
              + " | \t"
              + request.date
              + " | \t"
              + request.time);
    }
  }

  @Override
  public void edit(LaundryRequest data) throws IOException {
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

  @Override
  public void add(LaundryRequest data) throws IOException {
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

  @Override
  public void remove(LaundryRequest data) throws IOException {
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

  @Override
  public void saveTableAsCSV(String nameOfCSV) throws SQLException {
    String csvFilePath = "./" + nameOfCSV + ".csv";

    try {
      new File(csvFilePath);
      this.SQLToJava();
      this.JavaToCSV(csvFilePath);

    } catch (IOException e) {
      System.out.println(e.fillInStackTrace());
    }
  }

  @Override
  public LaundryRequest askUser() {
    Scanner labInput = new Scanner(System.in);

    String inputID = "None";
    String inputName = "none";
    String inputPatient = "N/A";
    String inputStaff = "N/A";
    String inputStatus = "N/A";
    String inputLocation = "N/A";
    String inputDate = "N/A";
    String inputTime = "N/A";

    System.out.println("Input ID: ");
    inputID = labInput.nextLine();

    System.out.println("Input type: ");
    inputName = labInput.nextLine();

    System.out.println("Staff Name: ");
    inputStaff = labInput.nextLine();

    Employee empty = new Employee(inputStaff);

    return new LaundryRequest(
        inputID, inputName, inputPatient, empty, inputStatus, inputLocation, inputDate, inputTime);
  }
}
