package edu.wpi.cs3733.D22.teamU.BackEnd.Request.SecurityRequest;

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

public class SecurityRequestDaoImpl implements DataDao<SecurityRequest> {
  public Statement statement;
  public String csvFile;
  public static HashMap<String, SecurityRequest> List = new HashMap<String, SecurityRequest>();
  public ArrayList<SecurityRequest> list = new ArrayList<SecurityRequest>();

  public SecurityRequestDaoImpl(Statement statement, String csvFile) {
    this.statement = statement;
    this.csvFile = csvFile;
  }

  @Override
  public ArrayList<SecurityRequest> list() {
    return null;
  }

  @Override
  public HashMap<String, SecurityRequest> hList() {
    return this.List;
  }

  @Override
  public void CSVToJava() throws IOException, SQLException {
    List = new HashMap<String, SecurityRequest>();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    String[] header = br.readLine().split(",");
    int columns = header.length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == columns) {
        Employee temporary = checkEmployee(row[3]);
        SecurityRequest r =
            new SecurityRequest(
                row[0], row[1], row[2], temporary, row[4], row[5], row[6], row[7], row[8]);
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
          System.out.println("Employee Not Found" + r.employee.getEmployeeID() + "SecurityRequest");
        }
      }
    }
  }

  public void CSVToJava(ArrayList<Location> locations, HashMap<String, Employee> employees)
      throws IOException, SQLException {
    List = new HashMap<String, SecurityRequest>();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    String[] header = br.readLine().split(",");
    int columns = header.length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == columns) {
        Employee temporary = checkEmployee(row[3]);
        SecurityRequest r =
            new SecurityRequest(
                row[0], row[1], row[2], temporary, row[4], row[5], row[6], row[7], row[8]);
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
          Employee e = employees.get(temporary.getEmployeeID());
          e.addRequest(r);
          r.setEmployee(e);
        } catch (Exception exception) {
          System.out.println("Employee Not Found" + r.employee.getEmployeeID() + "SecurityRequest");
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
      statement.execute("Drop table SecurityRequest");
    } catch (SQLException e) {
      System.out.println("didn't drop table");
    }
    try {
      statement.execute(
          "CREATE TABLE SecurityRequest("
              + "ID varchar (10) not null,"
              + "name varchar (20) not null,"
              + "status varchar (15) not null,"
              + "employee varchar (20) not null,"
              + "destination varchar(15) not null,"
              + "description varchar (200) not null,"
              + "lethal varchar(10) not null,"
              + "date varchar (10) not null,"
              + "time varchar (10) not null)");

      for (SecurityRequest currSecurity : List.values()) {
        statement.execute(
            "INSERT INTO SecurityRequest VALUES("
                + "'"
                + currSecurity.getID()
                + "','"
                + currSecurity.getName()
                + "','"
                + currSecurity.getStatus()
                + "','"
                + currSecurity.getEmployee().getEmployeeID()
                + "','"
                + currSecurity.getDestination()
                + "','"
                + currSecurity.getDescriptionOfThreat()
                + "','"
                + currSecurity.getLeathalForcePermited()
                + "','"
                + currSecurity.getDate()
                + "','"
                + currSecurity.getTime()
                + "')");
      }
    } catch (SQLException e) {
      System.out.println("JavaToSQL error in SecurityRequestImp");
      System.out.println(e);
    }
  }

  @Override
  public void SQLToJava() {
    List = new HashMap<String, SecurityRequest>();
    try {
      ResultSet results;
      results = statement.executeQuery("SELECT * FROM SecurityRequest");

      while (results.next()) {
        String id = results.getString("ID");
        String name = results.getString("name");
        String status = results.getString("status");
        String employee = results.getString("employee");
        String destination = results.getString("destination");
        String description = results.getString("description");
        String lethal = results.getString("lethal");
        String date = results.getString("date");
        String time = results.getString("time");

        SecurityRequest SQLRow =
            new SecurityRequest(
                id,
                name,
                status,
                checkEmployee(employee),
                destination,
                description,
                lethal,
                date,
                time);
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
    fw.append("Name");
    fw.append(",");
    fw.append("Status");
    fw.append(",");
    fw.append("Staff");
    fw.append(",");
    fw.append("Destination");
    fw.append(",");
    fw.append("Description");
    fw.append(",");
    fw.append("Lethal");
    fw.append(",");
    fw.append("Date");
    fw.append(",");
    fw.append("Time");
    fw.append("\n");

    for (SecurityRequest request : List.values()) {
      fw.append(request.getID());
      fw.append(",");
      fw.append(request.getName());
      fw.append(",");
      fw.append(request.getStatus());
      fw.append(",");
      fw.append(request.getEmployee().getEmployeeID());
      fw.append(",");
      fw.append(request.getDestination());
      fw.append(",");
      fw.append(request.getDescriptionOfThreat());
      fw.append(",");
      fw.append(request.getLeathalForcePermited());
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
        "ID |\t Name |\t Status |\t Staff |\t Destination |\t Description |\t Lethal |\t Date |\t Time");
    for (SecurityRequest request : this.List.values()) {
      System.out.println(
          request.ID
              + " | \t"
              + request.name
              + " | \t"
              + request.status
              + " | \t"
              + request.employee.getEmployeeID()
              + " | \t"
              + request.destination
              + " | \t"
              + request.descriptionOfThreat
              + " | \t"
              + request.leathalForcePermited
              + " | \t"
              + request.date
              + " | \t"
              + request.time);
    }
  }

  @Override
  public void edit(SecurityRequest data) throws IOException, SQLException {
    if (List.containsKey(data.ID)) { // check if node exists
      if (EmployeeDaoImpl.List.containsKey(
          data.getEmployee().getEmployeeID())) { // check if employee to be added exists
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
  public void add(SecurityRequest data) throws IOException, SQLException {
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
  public void remove(SecurityRequest data) throws IOException {
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
  public SecurityRequest askUser() {
    Scanner labInput = new Scanner(System.in);

    String inputID = "N/A";
    String inputSender = "N/A";
    String inputStatus = "N/A";
    String inputStaff = "test2";
    String inputDestination = "FDEPT00101";
    String inputDescript = "N/A";
    String inputLethal = "absolutely";
    String inputDate = "N/A";
    String inputTime = "N/A";

    System.out.println("Input Request ID: ");
    inputID = labInput.nextLine();

    System.out.println("Input Staff: ");
    inputStaff = labInput.nextLine();

    System.out.println("Input Destination: ");
    inputDestination = labInput.nextLine();

    Employee empty = new Employee(inputStaff);

    return new SecurityRequest(
        inputID,
        inputSender,
        inputStatus,
        empty,
        inputDestination,
        inputDescript,
        inputLethal,
        inputDate,
        inputTime);
  }
}
