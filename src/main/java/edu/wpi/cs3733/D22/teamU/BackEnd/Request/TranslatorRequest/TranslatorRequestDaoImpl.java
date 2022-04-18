package edu.wpi.cs3733.D22.teamU.BackEnd.Request.TranslatorRequest;

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

public class TranslatorRequestDaoImpl implements DataDao<TranslatorRequest> {
  public Statement statement;
  public String csvFile;
  public HashMap<String, TranslatorRequest> List = new HashMap<String, TranslatorRequest>();
  public ArrayList<TranslatorRequest> list = new ArrayList<TranslatorRequest>();

  @Override
  public ArrayList<TranslatorRequest> list() {
    return null;
  }

  @Override
  public HashMap<String, TranslatorRequest> hList() {
    return List;
  }

  public Employee checkEmployee(String employee) {
    if (EmployeeDaoImpl.List.get(employee) != null) {
      return EmployeeDaoImpl.List.get(employee);
    } else {
      Employee empty = new Employee("N/A");
      return empty;
    }
  }

  @Override
  public void CSVToJava() throws IOException, SQLException {
    List = new HashMap<String, TranslatorRequest>();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    String[] header = br.readLine().split(",");
    int columns = header.length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == columns) {
        TranslatorRequest r =
            new TranslatorRequest(
                row[0], row[1], row[2], row[3], checkEmployee(row[4]), row[6], row[7], row[8]);
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
          System.out.println("Location Not Found" + r.destination);
        }
      }
    }
  }

  @Override
  public void JavaToSQL() {
    try {
      statement.execute("Drop table MedicineRequest");
    } catch (Exception e) {
      System.out.println("didn't drop table");
    }

    try {
      statement.execute(
          "CREATE TAbLE TranslatorRequest("
              + "ID varchar(10) not null,"
              + "patientName varchar(20) not null,"
              + "toLanguage varchar(20) not null,"
              + "status varchar(20) not null,"
              + "staff varchar(20) not null,"
              + "destination varchar(15) not null,"
              + "date varchar(10) not null,"
              + "time varchar(10) not null)");
      for (TranslatorRequest currReq : List.values()) {
        statement.execute(
            "INSERT INTO TranslatorRequest VALUES("
                + "'"
                + currReq.getID()
                + "','"
                + currReq.getPatientName()
                + "','"
                + currReq.getToLang()
                + "','"
                + currReq.getStatus()
                + "','"
                + currReq.getEmployee().getEmployeeID()
                + "','"
                + currReq.getDestination()
                + "','"
                + currReq.getDate()
                + "','"
                + currReq.getTime()
                + "')");
      }
    } catch (SQLException e) {
      System.out.println("JavaToSQL error in TranslatorRequestDaoImpl");
    }
  }

  @Override
  public void SQLToJava() {
    List = new HashMap<String, TranslatorRequest>();
    try {
      ResultSet results;
      results = statement.executeQuery("SELECT * FROM TranslatorRequest");

      while (results.next()) {
        String id = results.getString("ID");
        String patientName = results.getString("patientName");
        String toLang = results.getString("toLanguage");
        String status = results.getString("status");
        String staff = results.getString("staff");
        String destination = results.getString("destination");
        String date = results.getString("date");
        String time = results.getString("time");

        TranslatorRequest SQLRow =
            new TranslatorRequest(
                id, patientName, toLang, status, checkEmployee(staff), destination, date, time);

        List.put(id, SQLRow);
      }
    } catch (Exception e) {
      System.out.println("translator request not found");
    }
  }

  @Override
  public void JavaToCSV(String csvFile) throws IOException {
    PrintWriter fw = new PrintWriter(new File(csvFile));

    fw.append("ID");
    fw.append(",");
    fw.append("Patient");
    fw.append(",");
    fw.append("Language Needed");
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

    for (TranslatorRequest request : List.values()) {
      fw.append(request.getID());
      fw.append(",");
      fw.append(request.getPatientName());
      fw.append(",");
      fw.append(request.getToLang());
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
    System.out.println(
        "ID |\t Patient Name |\t Language Needed |\t Status |\t Employee Name |\t Destination |\t Date |\t Time");
    for (TranslatorRequest request : this.List.values()) {
      System.out.println(
          request.ID
              + " | \t"
              + request.patientName
              + " | \t"
              + request.toLang
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
  public void edit(TranslatorRequest data) throws IOException, SQLException {
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
        System.out.println("No Such STAFF");
      }
    } else {
      System.out.println("Doesn't Exist");
    }
  }

  @Override
  public void add(TranslatorRequest data) throws IOException, SQLException {
    if (List.containsKey(data.ID)) {
      System.out.println("A Request With This ID Already Exists");
    } else {
      if (EmployeeDaoImpl.List.containsKey(data.getEmployee().getEmployeeID())) {
        data.setEmployee(EmployeeDaoImpl.List.get(data.getEmployee().getEmployeeID()));
        this.List.put(data.ID, data);
        this.JavaToSQL();
        this.JavaToCSV(csvFile);
      } else {
        System.out.println("No Such Staff Exists in Database");
      }
    }
  }

  @Override
  public void remove(TranslatorRequest data) throws IOException {
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
  public TranslatorRequest askUser() {
    Scanner reqInput = new Scanner(System.in);
    String inputId = "N/A";
    String inputpatientName = "N/A";
    String inputToLang = "N/A";
    String inputStatus = "N/A";
    String inputStaff = "N/A";
    String inputDestination = "N/A";
    String inputDate = "N/A";
    String inputTIme = "N/A";

    System.out.println("Input Request ID: ");
    inputId = reqInput.nextLine();

    System.out.println("Input Patient Name: ");
    inputpatientName = reqInput.nextLine();

    System.out.println("Input request amount: ");
    inputToLang = reqInput.nextLine();

    System.out.println("Input Staff name: ");
    inputStaff = reqInput.nextLine();

    Employee empty = new Employee(inputStaff);

    return new TranslatorRequest(
        inputId,
        inputpatientName,
        inputToLang,
        inputStatus,
        empty,
        inputDestination,
        inputDate,
        inputTIme);
  }
}
