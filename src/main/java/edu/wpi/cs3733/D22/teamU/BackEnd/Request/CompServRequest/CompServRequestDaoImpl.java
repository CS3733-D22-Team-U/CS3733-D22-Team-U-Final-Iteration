package edu.wpi.cs3733.D22.teamU.BackEnd.Request.CompServRequest;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
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
import java.util.Map;
import java.util.Scanner;

public class CompServRequestDaoImpl implements DataDao<CompServRequest> {
  public Statement statement;
  public String csvFile;
  public static HashMap<String, CompServRequest> List = new HashMap<String, CompServRequest>();
  public ArrayList<CompServRequest> list = new ArrayList<CompServRequest>();

  public CompServRequestDaoImpl(Statement statement, String csvFile)
      throws SQLException, IOException {
    this.statement = statement;
    this.csvFile = csvFile;
  }

  @Override
  public ArrayList<CompServRequest> list() {
    return null;
  }

  @Override
  public HashMap<String, CompServRequest> hList() {
    return this.List;
  }

  @Override
  public void CSVToJava() throws IOException, SQLException {
    List = new HashMap<String, CompServRequest>();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    String[] header = br.readLine().split(",");
    int columns = header.length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == columns) {
        Employee temporary = checkEmployee(row[3]);
        CompServRequest r =
            new CompServRequest(row[0], row[1], row[2], temporary, row[4], row[5], row[6], row[7]);
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
        try {
          Employee e =
              Udb.getInstance()
                  .EmployeeImpl
                  .List
                  .get(Udb.getInstance().EmployeeImpl.List.get(temporary.getEmployeeID()));
          e.addRequest(r);
          r.setEmployee(e);
        } catch (Exception exception) {
          System.out.println("Employee Not Found" + r.employee.getEmployeeID() + "Comp Request");
        }
      }
    }
  }

  public void CSVToJava(ArrayList<Location> locations, HashMap<String, Employee> employees)
      throws IOException, SQLException {
    List = new HashMap<String, CompServRequest>();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    String[] header = br.readLine().split(",");
    int columns = header.length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == columns) {
        Employee temporary = checkEmployee(row[3]);
        CompServRequest r =
            new CompServRequest(row[0], row[1], row[2], temporary, row[4], row[5], row[6], row[7]);
        List.put(row[0], r);
        try {
          Location temp = new Location();
          temp.setNodeID(r.destination);
          Location l = locations.get(locations.indexOf(temp));
          l.addRequest(r);
          r.setLocation(l);
        } catch (Exception exception) {
        }
        try {
          Employee e = employees.get(row[3]);
          e.addRequest(r);
          r.setEmployee(e);
        } catch (Exception exception) {
          System.out.println(
              "Employee Not Found "
                  + r.getEmployee().getEmployeeID()
                  + " CompServ Request"
                  + r.getID());
        }
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
      statement.execute("Drop table CompServRequest");
    } catch (SQLException e) {
      System.out.println("didn't drop table");
    }
    try {
      statement.execute(
          "CREATE TABLE CompServRequest("
              + "ID varchar (10) not null,"
              + "message varchar (200) not null,"
              + "status varchar (15) not null,"
              + "employee varchar (20) not null,"
              + "room varchar(20) not null,"
              + "date varchar (10) not null,"
              + "time varchar (10) not null,"
              + "device varchar(20) not null)");

      for (CompServRequest currCSR : List.values()) {
        DocumentReference docRef = db.collection("compServRequests").document(currCSR.getID());
        ApiFuture<DocumentSnapshot> ds = docRef.get();
        try {
          if (!ds.get().exists() || ds.get() == null) {
            firebaseupdate(currCSR);
          }
        } catch (Exception e) {
          System.out.println("firebase error in java to sql comp serv");
        }

        statement.execute(
            "INSERT INTO CompServRequest VALUES("
                + "'"
                + currCSR.getID()
                + "','"
                + currCSR.getMessage()
                + "','"
                + currCSR.getStatus()
                + "','"
                + currCSR.getEmployee().getEmployeeID()
                + "','"
                + currCSR.getDestination()
                + "','"
                + currCSR.getDate()
                + "','"
                + currCSR.getTime()
                + "','"
                + currCSR.getDevice()
                + "')");
      }
    } catch (SQLException e) {
      System.out.println("JavaToSQL error in CompServRequestImp");
      System.out.println(e);
    }
  }

  public void firebaseUpdate(CompServRequest comser) {
    DocumentReference docRef = db.collection("compServRequests").document(comser.getID());
    Map<String, Object> data = new HashMap<>();
    data.put("message", comser.getMessage());
    data.put("status", comser.getStatus());
    data.put("employeeID", comser.getEmployee().getEmployeeID());
    data.put("destination", comser.getDestination());
    data.put("date", comser.getDate());
    data.put("time", comser.getTime());
    data.put("device", comser.getDevice());
    docRef.set(data);
  }

  @Override
  public void SQLToJava() {
    List = new HashMap<String, CompServRequest>();
    try {
      ResultSet results;
      results = statement.executeQuery("SELECT * FROM CompServRequest");

      while (results.next()) {
        String id = results.getString("ID");
        String message = results.getString("message");
        String status = results.getString("status");
        String employee = results.getString("employee");
        String destination = results.getString("room");
        String date = results.getString("date");
        String time = results.getString("time");
        String device = results.getString("device");

        CompServRequest SQLRow =
            new CompServRequest(
                id, message, status, checkEmployee(employee), destination, date, time, device);
        List.put(id, SQLRow);
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
    fw.append(",");
    fw.append("Device");
    fw.append("\n");

    for (CompServRequest request : List.values()) {
      fw.append(request.getID());
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
      fw.append(",");
      fw.append(request.getDevice());
      fw.append("\n");
    }
    fw.close();
  }

  @Override
  public void printTable() throws IOException, SQLException {
    // csv to java
    CSVToJava();
    // display locations and attributes
    System.out.println("ID |\t Message |\t Staff |\t Room |\t Date |\t Time |\t Device");
    for (CompServRequest request : this.List.values()) {
      System.out.println(
          request.ID
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
              + request.time
              + " | \t"
              + request.device);
    }
  }

  @Override
  public void edit(CompServRequest data) throws IOException, SQLException {
    if (List.containsKey(data.ID)) { // check if node exists
      if (EmployeeDaoImpl.List.containsKey(
          data.getEmployee().getEmployeeID())) { // check if employee to be added exists
        data.setEmployee(EmployeeDaoImpl.List.get(data.getEmployee().getEmployeeID()));
        this.List.replace(data.ID, data);
        this.JavaToSQL();
        firebaseupdate(data);
        this.JavaToCSV(csvFile);
      } else {
        System.out.println("No Such Employee Exists in Database");
      }
    } else {
      System.out.println("A Request With This ID Already Does Not Exist");
    }
  }

  @Override
  public void add(CompServRequest data) throws IOException, SQLException {
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
  public void remove(CompServRequest data) throws IOException {
    // removes entries from SQL table that match input node
    try {
      this.List.remove(data.ID);
      db.collection("compServRequests").document(data.getID()).delete();
      this.JavaToSQL();
      this.JavaToCSV(csvFile);
    } catch (Exception e) {
      System.out.println("This Data Point Was Not Found");
    }
  }

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
  public CompServRequest askUser() {
    Scanner csInput = new Scanner(System.in);

    String inputID = "N/A";
    String inputMessage = "N/A";
    String inputStatus = "N/A";
    String inputStaff = "N/A";
    String inputDestination = "FDEPT00101";
    String inputDate = "N/A";
    String inputTime = "N/A";
    String inputDevice = "N/A";

    System.out.println("Input Request ID: ");
    inputID = csInput.nextLine();

    System.out.println("Input Staff: ");
    inputStaff = csInput.nextLine();

    System.out.println("Input Device: ");
    inputDevice = csInput.nextLine();

    System.out.println("Input Location of Device: ");
    inputDestination = csInput.nextLine();

    Employee empty = new Employee(inputStaff);

    return new CompServRequest(
        inputID,
        inputMessage,
        inputStatus,
        empty,
        inputDestination,
        inputDate,
        inputTime,
        inputDevice);
  }
}
