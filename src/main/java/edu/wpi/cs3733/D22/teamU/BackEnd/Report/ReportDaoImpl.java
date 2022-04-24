package edu.wpi.cs3733.D22.teamU.BackEnd.Report;

import edu.wpi.cs3733.D22.teamU.BackEnd.DataDao;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class ReportDaoImpl implements DataDao<Report> {
  public Statement statement;
  public String CSVfile;
  public static HashMap<String, Report> List = new HashMap<String, Report>();
  public ArrayList<Report> list = new ArrayList<Report>();

  public ReportDaoImpl(Statement statement, String CSVfile) {
    this.statement = statement;
    this.CSVfile = CSVfile;
  }

  @Override
  public ArrayList<Report> list() {
    return null;
  }

  @Override
  public HashMap<String, Report> hList() {
    return this.List;
  }

  private Employee checkEmployee(String employee) {
    if (EmployeeDaoImpl.List.get(employee) != null) {
      return EmployeeDaoImpl.List.get(employee);
    } else {
      Employee empty = new Employee("00000");
      return empty;
    }
  }

  @Override
  public void CSVToJava() throws IOException, SQLException {
    List = new HashMap<String, Report>();
    String s;
    File file = new File(CSVfile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    br.readLine();
    String[] header = br.readLine().split(",");
    int columns = header.length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == columns) {
        Report r =
            new Report(
                row[0].trim(),
                checkEmployee(row[1].trim()),
                row[2].trim(),
                row[3].trim(),
                Boolean.parseBoolean(row[4].trim()),
                row[5].trim(),
                row[6].trim());
        List.put(row[0].trim(), r);
        try {
          Employee e =
              Udb.getInstance()
                  .EmployeeImpl
                  .List
                  .get(Udb.getInstance().EmployeeImpl.List.get(row[1].trim()));
          e.addReport(r);
          r.setEmployee(e);
        } catch (Exception exception) {
          System.out.println("Employee Not Found" + r.employee.getEmployeeID() + "Comp Request");
        }
      }
    }
  }

  public void CSVToJava(ArrayList<Location> locations, HashMap<String, Employee> employees)
      throws IOException, SQLException {
    List = new HashMap<String, Report>();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    String[] header = br.readLine().split(",");
    int columns = header.length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == columns) {
        Employee emp = checkEmployee(row[1].trim());
        Report r =
            new Report(
                row[0].trim(),
                emp,
                row[2].trim(),
                row[3].trim(),
                Boolean.parseBoolean(row[4].trim()),
                row[5].trim(),
                row[6].trim());
        List.put(row[0].trim(), r);
        try {
          emp.addReport(r);
          r.setEmployee(emp);
        } catch (Exception exception) {
          System.out.println(
              "Employee Not Found "
                  + r.getEmployee().getEmployeeID()
                  + " Report Request"
                  + r.getId());
        }
      }
    }
  }

  @Override
  public void JavaToSQL() {
    try {
      statement.execute("Drop table Reports");
    } catch (SQLException ex) {
      System.out.println("Didn't drop table - reports");
    }
    try {
      statement.execute(
          "CREATE TABLE Reports("
              + "id varchar(15) not null,"
              + "employee varchar(15) not null,"
              + "type varchar(20) not null,"
              + "description varchar(20) not null,"
              + "status boolean not null,"
              + "date varchar(10) not null,"
              + "time varchar(10) not null)");
      for (Report currReport : List.values()) {
        statement.execute(
            "INSERT INTO Reports VALUES("
                + "'"
                + currReport.id
                + "','"
                + currReport.getEmployee().getEmployeeID()
                + "','"
                + currReport.type
                + "','"
                + currReport.description
                + "','"
                + currReport.status
                + "','"
                + currReport.date
                + "','"
                + currReport.time
                + "')");
      }
    } catch (Exception e) {
      System.out.println("JavaToSQL error in ReportImp");
      System.out.println(e);
    }
  }

  @Override
  public void SQLToJava() {
    List = new HashMap<String, Report>();

    try {
      ResultSet results;
      results = statement.executeQuery("SELECT * FROM Reports");

      while (results.next()) {
        String reportID = results.getString("id");
        String employee = results.getString("employee");
        String type = results.getString("type");
        String description = results.getString("description");
        Boolean status = results.getBoolean("status");
        String date = results.getString("date");
        String time = results.getString("time");

        Report SQLRow =
            new Report(reportID, checkEmployee(employee), type, description, status, date, time);
      }
    } catch (SQLException e) {
      System.out.println("error in SQLToJava - Reports");
    }
  }

  @Override
  public void JavaToCSV(String csvFile) throws IOException {
    PrintWriter fw = new PrintWriter(new File(csvFile));

    fw.append("ID");
    fw.append(",");
    fw.append("Employee");
    fw.append(",");
    fw.append("Type");
    fw.append(",");
    fw.append("Description");
    fw.append(",");
    fw.append("Reports");
    fw.append(",");
    fw.append("Status");
    fw.append(",");
    fw.append("Date");
    fw.append(",");
    fw.append("Time");
    fw.append("\n");

    for (Report report : List.values()) {
      fw.append(report.getId());
      fw.append(",");
      fw.append(report.getEmployee().getEmployeeID());
      fw.append(",");
      fw.append(report.getType());
      fw.append(",");
      fw.append(report.getDescription());
      fw.append(",");
      fw.append(Boolean.toString(report.getStatus()));
      fw.append(",");
      fw.append(report.getDate());
      fw.append(",");
      fw.append(report.getTime());
      fw.append("\n");
    }
    fw.close();
  }

  @Override
  public void printTable() throws IOException, SQLException {
    CSVToJava();
    // display locations and attributes
    System.out.println(
        "Report ID |\t Employee |\t Type |\t Description |\t Status |\t Date |\t Time");
    for (Report report : List.values()) {
      System.out.println(
          report.id
              + " | \t"
              + report.getEmployee().getEmployeeID()
              + " | \t"
              + report.getType()
              + " | \t"
              + report.description
              + " | \t"
              + report.status
              + " | \t"
              + report.date
              + " | \t"
              + report.time
              + " | \t");
    }
  }

  @Override
  public void edit(Report data) throws IOException, SQLException {
    CSVToJava(); // t
    Set<String> keys = List.keySet();

    if (List.containsKey(data.getId())) {
      List.replace(data.getId(), data);
    } else {
      System.out.println("Doesn't Exist");
    }
    this.JavaToSQL(); // t
    this.JavaToCSV(CSVfile); // t
  }

  @Override
  public void add(Report data) throws IOException, SQLException {
    // add a new entry to the SQL table
    // prompt for ID

    // Employee newEmployee = new Employee(data.getEmployeeID());
    if (List.containsKey(data.getId())) {
      System.out.println("An Employee With This ID Already Exists");
    } else {
      List.put(data.id, data);
      this.JavaToSQL();
      this.JavaToCSV(CSVfile);
    }
  }

  @Override
  public void remove(Report data) throws IOException {

    if (this.List.containsKey(data.getId())) {
      this.List.remove(data.getId());
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

  @Override
  public void saveTableAsCSV(String csvName) throws SQLException {
    String csvFilePath = "./" + csvName + ".csv";

    try {
      new File(csvFilePath);
      this.SQLToJava();
      this.JavaToCSV(csvFilePath);

    } catch (IOException e) {
      System.out.println(e.fillInStackTrace());
    }
  }

  @Override
  public Report askUser() {
    Scanner reportInput = new Scanner(System.in);

    String inputID = "None";
    String inputType = "N/A";
    String inputDesc = "N/A";
    Boolean inputStatus = true;
    String inputDate = "00-00-00";
    String inputTime = "00:00:00";

    System.out.println("Input Employee ID: ");
    inputID = reportInput.nextLine();

    System.out.println("Input Employee Username: ");
    inputType = reportInput.nextLine();
    //
    System.out.println("Input Status (true/false): ");
    inputStatus = Boolean.parseBoolean(reportInput.nextLine());

    return new Report(
        inputID, checkEmployee("00000"), inputType, inputDesc, inputStatus, inputDate, inputTime);
  }
}
