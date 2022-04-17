package edu.wpi.cs3733.D22.teamU.BackEnd.Request.GiftRequest;

import edu.wpi.cs3733.D22.teamU.BackEnd.DataDao;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.LabRequest.LabRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MedicineRequest.MedicineRequest;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class GiftRequestDaoImpl implements DataDao<GiftRequest> {
  public Statement statement;
  public String csvFile;
  public HashMap<String, GiftRequest> List = new HashMap<String, GiftRequest>();
  public ArrayList<GiftRequest> list = new ArrayList<GiftRequest>();

  public GiftRequestDaoImpl(Statement statement, String csvFile) {
    this.statement = statement;
    this.csvFile = csvFile;
  }

  @Override
  public ArrayList<GiftRequest> list() {
    return null;
  }

  @Override
  public HashMap<String, GiftRequest> hList() {
    return this.List;
  }

  @Override
  public void CSVToJava() throws IOException, SQLException {
    List = new HashMap<String, GiftRequest>();
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
            new GiftRequest(
                row[0],
                row[1],
                row[2],
                row[3],
                row[4],
                row[5],
                checkEmployee(row[6]),
                row[7],
                row[8],
                row[9]));
      }
    }
  }

  private Employee checkEmployee(String employee) {
    if (EmployeeDaoImpl.List.get(employee) != null) {
      return EmployeeDaoImpl.List.get(employee);
    } else {
      Employee empty = new Employee("N/A");
      return empty;
    }
  }

  @Override
  public void JavaToSQL() {
    try {
      statement.execute("Drop table GiftRequest");
    } catch (SQLException e) {
      System.out.println("didn't drop table");
    }
    try {
      statement.execute
              ("CREATE TABLE GiftRequest("
                      + "ID varchar (10) not null,"
                      + "name varchar (20) not null,"
                      + "patientName varchar (20) not null,"
                      + "gifts varchar (35) not null,"
                      + "message varchar (200) not null,"
                      + "status varchar (15) not null,"
                      + "employee varchar (20) not null,"
                      + "destination varchar(15) not null,"
                      + "date varchar (10) not null,"
                      + "time varchar (10) not null)");

      for (GiftRequest currGift : List.values()) {
        statement.execute(
                "INSERT INTO GiftRequest VALUES("
                        + "'"
                        + currGift.getID()
                        + "','"
                        + currGift.getName()
                        + "','"
                        + currGift.getPatientName()
                        + "','"
                        + currGift.getGifts()
                        + "','"
                        + currGift.getMessage()
                        + "','"
                        + currGift.getStatus()
                        + "','"
                        + currGift.getEmployee().getEmployeeID()
                        + "','"
                        + currGift.getDestination()
                        + "','"
                        + currGift.getDate()
                        + "','"
                        + currGift.getTime()
                        + "')");
      }
    }
    catch(SQLException e)
      {
        System.out.println("Connection failed. Check output console.");
      }
    }

  @Override
  public void SQLToJava() {
    List = new HashMap<String, GiftRequest>();
    try {
      ResultSet results;
      results = statement.executeQuery("SELECT * FROM GiftRequest");

      while (results.next()) {
        String id = results.getString("ID");
        String name = results.getString("name");
        String patientName = results.getString("patientName");
        String gifts = results.getString("gifts");
        String message = results.getString("message");
        String status = results.getString("status");
        String employee = results.getString("employee");
        String destination = results.getString("destination");
        String date = results.getString("date");
        String time = results.getString("time");

        GiftRequest SQLRow = new GiftRequest(id, name, patientName,gifts,message,status,checkEmployee(employee),destination,date,time);
        List.put(id, SQLRow);
      }
    }
    catch (SQLException e)
    {
      System.out.println("Database does not exist.");
    }
  }

  @Override
  public void JavaToCSV(String csvFile) throws IOException {
    PrintWriter fw = new PrintWriter(new File(csvFile));

    fw.append("ID");
    fw.append(",");
    fw.append("Sender");
    fw.append(",");
    fw.append("Patient");
    fw.append(",");
    fw.append("Gifts");
    fw.append(",");
    fw.append("Message");
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

    for (GiftRequest request : List.values()) {
      fw.append(request.getID());
      fw.append(",");
      fw.append(request.getName());
      fw.append(",");
      fw.append(request.getPatientName());
      fw.append(",");
      fw.append(request.getGifts());
      fw.append(",");
      fw.append(request.getMessage());
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

  @Override
  public void printTable() throws IOException, SQLException {
    // csv to java
    CSVToJava();
    // display locations and attributes
    System.out.println("ID |\t Sender |\t Patient |\t Gifts |\t Message |\t Staff |\t Destination |\t Date |\t Time");
    for (GiftRequest request : this.List.values()) {
      System.out.println(
              request.ID
                      + " | \t"
                      + request.name
                      + " | \t"
                      + request.patientName
                      + " | \t"
                      + request.gifts
                      + " | \t"
                      + request.message
                      + " | \t"
                      + request.status
                      + " | \t"
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
  public void edit(GiftRequest data) throws IOException, SQLException {
    if (List.containsKey(data.ID)) { // check if node exists
      if (EmployeeDaoImpl.List.containsKey(data.getEmployee().getEmployeeID())) { // check if employee to be added exists
        data.setEmployee(EmployeeDaoImpl.List.get(data.getEmployee().getEmployeeID()));
        this.List.replace(data.ID, data);
        this.JavaToSQL();
        this.JavaToCSV(csvFile);
      } else {
        System.out.println("No Such Employee Exists in Database");
      }
    } else {
      System.out.println("The Specified Request Does Not Exist");
    }

  }

  @Override
  public void add(GiftRequest data) throws IOException, SQLException {
      if (List.containsKey(data.ID)) {
        System.out.println("A Request With This ID Already Exists");
      } else {
        if (EmployeeDaoImpl.List.containsKey(data.getEmployee().getEmployeeID())) {
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
  public void remove(GiftRequest data) throws IOException {
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
  public GiftRequest askUser() {
    Scanner labInput = new Scanner(System.in);

    String inputID = "N/A";
    String inputSender = "N/A";
    String inputPatient = "N/A";
    String inputGifts = "N/A";
    String inputMessage =  "N/A";
    String inputStatus = "N/A";
    String inputStaff = "N/A";
    String inputDestination = "N/A";
    String inputDate = "N/A";
    String inputTime = "N/A";

    System.out.println("Input Request ID: ");
    inputID = labInput.nextLine();

    System.out.println("Input Patient Name: ");
    inputPatient = labInput.nextLine();

    System.out.println("Input Gifts: ");
    inputGifts = labInput.nextLine();

    System.out.println("Input Staff: ");
    inputStaff = labInput.nextLine();

    Employee empty = new Employee(inputStaff);;

    return new GiftRequest(inputID, inputSender, inputPatient, inputGifts, inputMessage, inputStatus, empty, inputDestination, inputDate, inputTime);
  }
}
