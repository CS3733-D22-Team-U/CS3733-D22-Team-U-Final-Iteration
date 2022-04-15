package edu.wpi.cs3733.D22.teamU.BackEnd.Request.MedicineRequest;

import edu.wpi.cs3733.D22.teamU.BackEnd.DataDao;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MedicineRequestDaoImpl implements DataDao<MedicineRequest> {
  public Statement statement;
  public String csvFile;
  public HashMap<String, MedicineRequest> List = new HashMap<String, MedicineRequest>();
  public ArrayList<MedicineRequest> list = new ArrayList<MedicineRequest>();

  public MedicineRequestDaoImpl(Statement statement, String csvfile) {
    this.csvFile = csvfile;
    this.statement = statement;
  }

  @Override
  public ArrayList<MedicineRequest> list() {
    return null;
  }

  @Override
  public HashMap<String, MedicineRequest> hList() {
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

  public void CSVToJava() throws IOException {
    List = new HashMap<String, MedicineRequest>();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    br.readLine();
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == 8) {
        List.put(
            row[0],
            new MedicineRequest(
                row[0], row[1], row[2], row[3], checkEmployee(row[4]), row[5], row[6], row[7]));
      }
    }
  }

  public void JavaToCSV(String csvFile) throws IOException {
    PrintWriter fw = new PrintWriter(new File(csvFile));

    fw.append("ID");
    fw.append(",");
    fw.append("Name");
    fw.append(",");
    fw.append("PatientName");
    fw.append(",");
    fw.append("Status");
    fw.append(",");
    fw.append("EmployeeName");
    fw.append(",");
    fw.append("Location");
    fw.append(",");
    fw.append("Date");
    fw.append(",");
    fw.append("Time");
    fw.append("\n");

    for (MedicineRequest request : List.values()) {
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
      statement.execute("Drop table MedicineRequest");
    } catch (Exception e) {
      System.out.println("didn't drop table");
    }

    try {
      statement.execute(
          "CREATE TABLE MedicineRequest("
              + "ID varchar(10) not null,"
              + "name varchar(50) not null, "
              + "patientName varchar(50) not null, "
              + "status varchar(50) not null, "
              + "staff varchar(50) not null, "
              + "location varchar(50) not null, "
              + "date varchar(10) not null, "
              + "time varchar(10) not null)");
      for (MedicineRequest currReq : List.values()) {
        statement.execute(
            "INSERT INTO MedicineRequest VALUES("
                + "'"
                + currReq.getID()
                + "','"
                + currReq.getName()
                + "','"
                + currReq.getPatientName()
                + "','"
                + currReq.getStatus()
                + "','"
                + currReq.getEmployee().getEmployeeID()
                + "','"
                + currReq.getLocation()
                + "','"
                + currReq.getDate()
                + "','"
                + currReq.getTime()
                + "')");
      }
    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console.");
    }
  }

  public void SQLToJava() {
    List = new HashMap<String, MedicineRequest>();
    try {
      ResultSet results;
      results = statement.executeQuery("SELECT * FROM MedicineRequest");

      while (results.next()) {
        String id = results.getString("ID");
        String name = results.getString("name");
        String patientName = results.getString("patientName");
        String status = results.getString("status");
        String staff = results.getString("staff");
        String location = results.getString("location");
        String date = results.getString("date");
        String time = results.getString("time");

        MedicineRequest SQLRow =
            new MedicineRequest(
                id, name, patientName, status, checkEmployee(staff), location, date, time);

        List.put(id, SQLRow);
      }
    } catch (SQLException e) {
      System.out.println("request not found");
    }
  }

  public void printTable() throws IOException {
    // csv to java
    CSVToJava();
    // display locations and attributes
    System.out.println(
        "ID |\t Name |\t Patient Name |\t Status |\t Employee Name |\t Location |\t Date |\t Time");
    for (MedicineRequest request : this.List.values()) {
      System.out.println(
          request.ID
              + " | \t"
              + request.name
              + " | \t"
              + request.patientName
              + " | \t"
              + request.status
              + " | \t"
              + request.employee.getEmployeeID()
              + " | \t"
              + request.location
              + " | \t"
              + request.date
              + " | \t"
              + request.time);
    }
  }

  @Override
  public void edit(MedicineRequest data) throws IOException {
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
  public void add(MedicineRequest data) throws IOException {
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
  public void remove(MedicineRequest data) throws IOException {
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

  public MedicineRequest askUser() {
    Scanner reqInput = new Scanner(System.in);

    String inputID = "None";
    String inputName = "N/A";
    String inputPatientName = "N/A";
    String inputStatus = "N/A";
    String inputStaff = "N/A";
    String inputLocation = "N/A";
    String inputDate = "N/A";
    String inputTime = "N/A";

    System.out.println("Input request ID: ");
    inputID = reqInput.nextLine();

    System.out.println("Input name: ");
    inputName = reqInput.nextLine();

    System.out.println("Input Staff name: ");
    inputStaff = reqInput.nextLine();

    Employee empty = new Employee(inputStaff);

    return new MedicineRequest(
        inputID,
        inputName,
        inputPatientName,
        inputStatus,
        empty,
        inputLocation,
        inputDate,
        inputTime);
  }
}
