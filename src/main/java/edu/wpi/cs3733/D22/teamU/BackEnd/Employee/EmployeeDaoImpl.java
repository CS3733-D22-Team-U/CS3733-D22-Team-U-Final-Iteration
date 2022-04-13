package edu.wpi.cs3733.D22.teamU.BackEnd.Employee;

import edu.wpi.cs3733.D22.teamU.BackEnd.DataDao;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class EmployeeDaoImpl implements DataDao<Employee> {

  // make constant in locationDao
  public Statement statement;
  public String CSVfile;
  public static HashMap<String, Employee> List = new HashMap<String, Employee>();
  public ArrayList<Employee> list = new ArrayList<Employee>();

  public EmployeeDaoImpl(Statement statement, String CSVfile) {
    this.CSVfile = CSVfile;
    this.statement = statement;
  }

  @Override
  public ArrayList<Employee> list() {
    return null;
  }

  public HashMap<String, Employee> hList() {
    return this.List;
  }

  /**
   * CSVToJava: converts the CSV file at the given filepath into a list of Java objects
   *
   * @throws IOException
   */
  public void CSVToJava() throws IOException {
    List = new HashMap<String, Employee>();
    String s;
    File file = new File(CSVfile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    br.readLine();
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == 6)
        List.put(
            row[0].trim(),
            new Employee(
                row[0].trim(),
                row[1].trim(),
                Integer.parseInt(row[2].trim()),
                Boolean.parseBoolean(row[3].trim()),
                row[4].trim(),
                row[5].trim()));
    }
  }

  /** JavaToSQL: takes the global list of Java objects and translates them into a SQL database */
  public void JavaToSQL() {

    try {
      statement.execute("Drop table Employees");
    } catch (Exception e) {
      System.out.println("didn't drop table");
    }
    try {
      statement.execute(
          "CREATE TABLE Employees(employeeID varchar(18) not null, "
              + "occupation varchar(200) not null,"
              + "reports int not null,"
              + "onDuty boolean not null,"
              + "username varchar(20) not null,"
              + "password varchar(20) not null)");

      Set<String> keys = List.keySet();
      for (String j : keys) {
        Employee currEmp = List.get(j);
        statement.execute(
            "INSERT INTO Employees VALUES('"
                + currEmp.employeeID
                + "','"
                + currEmp.occupation
                + "',"
                + currEmp.reports
                + ","
                + currEmp.onDuty
                + ",'"
                + currEmp.username
                + "','"
                + currEmp.password
                + "')");
      }
    } catch (SQLException e) {
      System.out.println("hsbd");
    }
  }

  /** SQLToJava: takes the SQL database and overwrites the global list of Java objects */
  public void SQLToJava() {
    List = new HashMap<String, Employee>();

    try {
      ResultSet results;
      results = statement.executeQuery("SELECT * FROM Employees");

      while (results.next()) {
        String employeeID = results.getString("employeeID");
        String occupation = results.getString("occupation");
        int reports = results.getInt("reports");
        boolean onDuty = results.getBoolean("onDuty");
        String username = results.getString("username");
        String password = results.getString("password");

        Employee SQLRow = new Employee(employeeID, occupation, reports, onDuty, username, password);

        List.put(employeeID, SQLRow);
      }
    } catch (SQLException e) {
      System.out.println("employee does not exist.");
    }
  }

  /**
   * JavaToCSV: takes the global list of Java objects and writes them into a CSV document with the
   * given filepath string
   *
   * @param csvFile
   * @throws IOException
   */
  public void JavaToCSV(String csvFile) throws IOException {
    PrintWriter fw = new PrintWriter(new File(csvFile));

    fw.append("employeeID");
    fw.append(",");
    fw.append("occupation");
    fw.append(",");
    fw.append("reports");
    fw.append(",");
    fw.append("onDuty");
    fw.append(",");
    fw.append("username");
    fw.append(",");
    fw.append("password");
    fw.append("\n");

    Set<String> keys = List.keySet();
    for (String i : keys) {
      fw.append(List.get(i).employeeID);
      fw.append(",");
      fw.append(List.get(i).occupation);
      fw.append(",");
      fw.append(Integer.toString(List.get(i).reports));
      fw.append(",");
      fw.append(Boolean.toString(List.get(i).onDuty));
      fw.append(",");
      fw.append(List.get(i).username);
      fw.append(",");
      fw.append(List.get(i).password);
      fw.append("\n");
    }
    fw.close();
  }

  /**
   * printEmployeeTableInTerm: prints out the Employees from the given CSV filepath, printing a
   * table with each Employee's attributes
   *
   * @throws IOException
   */
  public void printTable() throws IOException {
    // csv to java
    CSVToJava();
    // display locations and attributes
    System.out.println(
        "Employee ID |\t Occupation |\t Reports |\t On Duty |\t Username |\t Password");
    for (Employee employee : List.values()) {
      System.out.println(
          employee.employeeID
              + " | \t"
              + employee.occupation
              + " | \t"
              + employee.reports
              + " | \t"
              + employee.onDuty
              + " | \t"
              + employee.username
              + " | \t"
              + employee.password
              + " | \t");
    }
  }

  /**
   * editEmployee: makes a SQL table from the given CSV filepath, then prompts for an ID and updates
   * the employee with the matching ID, then converts the modified SQL table into a CSV, storing at
   * the original filepath
   *
   * @param data
   * @throws IOException
   */
  public void edit(Employee data) throws IOException {

    CSVToJava(); // t
    Set<String> keys = List.keySet();

    if (List.containsKey(data.getEmployeeID())) {
      List.replace(data.getEmployeeID(), data);
    } else {
      System.out.println("Doesn't Exist");
    }
    this.JavaToSQL(); // t
    this.JavaToCSV(CSVfile); // t
  }

  /**
   * addEmployee: adds a new employee with the prompted ID to the global Java list, then updates the
   * SQL table and the CSV file at the given filepath
   *
   * @param data
   * @throws IOException
   */
  public void add(Employee data) throws IOException {
    // add a new entry to the SQL table
    // prompt for ID

    // Employee newEmployee = new Employee(data.getEmployeeID());
    if (List.containsKey(data.employeeID)) {
      System.out.println("An Employee With This ID Already Exists");
    } else {
      List.put(data.employeeID, data);
      this.JavaToSQL();
      this.JavaToCSV(CSVfile);
    }
  }

  /**
   * removeEmployee: removes the new employee with the prompted ID from the global Java list, then
   * updates the SQL table and the CSV file at the given filepath
   *
   * @param data
   * @throws IOException
   */
  public void remove(Employee data) throws IOException {
    // removes entries from SQL table that match input node
    // prompt for ID

    if (this.List.containsKey(data.getEmployeeID())) {
      this.List.remove(data.getEmployeeID());
    } else {
      System.out.println("Doesn't exist");
    }
    this.JavaToSQL();
    ;
    this.JavaToCSV(CSVfile);
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

  /**
   * Search for index with the employee ID
   *
   * @return int
   */
  /*public int search(String id) {
    int index = -1;
    for (int i = 0; i < list().size(); i++) if (id.equals(list().get(i).getEmployeeID())) index = i;
    return index;
  }*/

  public Employee askUser() {
    Scanner employeeInput = new Scanner(System.in);

    String inputEmployeeID = "None";
    String inputOccupation = "N/A";
    int inputReports = 0;
    boolean inputOnDuty = false;
    String inputUsername = "N/A";
    String inputPassword = "N/A";

    System.out.println("Input Employee ID: ");
    inputEmployeeID = employeeInput.nextLine();

    System.out.println("Input occupation: ");
    inputOccupation = employeeInput.nextLine();

    System.out.println("Input Employee Username: ");
    inputUsername = employeeInput.nextLine();
    //
    System.out.println("Input Password: ");
    inputPassword = employeeInput.nextLine();

    return new Employee(
        inputEmployeeID, inputOccupation, inputReports, inputOnDuty, inputUsername, inputPassword);
  }
}
