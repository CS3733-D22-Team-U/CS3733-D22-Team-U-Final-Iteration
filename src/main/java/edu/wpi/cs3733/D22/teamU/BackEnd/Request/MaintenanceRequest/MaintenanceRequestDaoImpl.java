package edu.wpi.cs3733.D22.teamU.BackEnd.Request.MaintenanceRequest;

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

public class MaintenanceRequestDaoImpl implements DataDao<MaintenanceRequest> {

  public Statement statement;
  public String csvFile;
  public static HashMap<String, MaintenanceRequest> List = new HashMap<String, MaintenanceRequest>();
  public ArrayList<MaintenanceRequest> list = new ArrayList<MaintenanceRequest>();

  public MaintenanceRequestDaoImpl(Statement statement, String csvFile)
      throws SQLException, IOException {
    this.statement = statement;
    this.csvFile = csvFile;
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
  public ArrayList<MaintenanceRequest> list() {
    return null;
  }

  @Override
  public HashMap<String, MaintenanceRequest> hList() {
    return null;
  }

  @Override
  public void CSVToJava() throws IOException, SQLException {
    List = new HashMap<String, MaintenanceRequest>();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    String[] header = br.readLine().split(",");
    int columns = header.length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == columns) {
        Employee temporary = checkEmployee(row[4]);
        MaintenanceRequest r =
            new MaintenanceRequest(
                row[0], row[1], row[2], row[3], temporary, row[5], row[6], row[7], row[8]);
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
        }
      }
    }
  }

  public void CSVToJava(ArrayList<Location> locations, HashMap<String, Employee> employees)
      throws IOException, SQLException {
    List = new HashMap<String, MaintenanceRequest>();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    String[] header = br.readLine().split(",");
    int columns = header.length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == columns) {
        MaintenanceRequest r =
            new MaintenanceRequest(
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
        try {
          Employee e = employees.get(row[4]);
          e.addRequest(r);
          r.setEmployee(e);
        } catch (Exception exception) {
          System.out.println(
              "Employee Not Found "
                  + r.getEmployee().getEmployeeID()
                  + " Maintenance Request"
                  + r.getID());
        }
      }
    }
  }

  @Override
  public void JavaToSQL() {
    try {
      statement.execute("Drop table MaintenanceRequest");
    } catch (Exception e) {
      System.out.println("didn't drop table");
    }

    try {
      statement.execute(
          "CREATE TABLE MaintenanceRequest("
              + "ID varchar(10) not null,"
              + "name varchar(20) not null,"
              + "status varchar(20) not null,"
              + "destination varchar(20) not null,"
              + "employee varchar(15) not null,"
              + "typeOfMaintenance varchar(20) not null,"
              + "description varchar(60) not null,"
              + "date varchar(10) not null,"
              + "time varchar(10) not null)");

      for (MaintenanceRequest currMainReq : List.values()) {
        statement.execute(
            "INSERT INTO MaintenanceRequest VALUES("
                + "'"
                + currMainReq.getID()
                + "','"
                + currMainReq.getName()
                + "','"
                + currMainReq.getStatus()
                + "','"
                + currMainReq.getDestination()
                + "','"
                + currMainReq.getEmployee().getEmployeeID()
                + "','"
                + currMainReq.getTypeOfMaintenance()
                + "','"
                + currMainReq.getDescription()
                + "','"
                + currMainReq.getDate()
                + "','"
                + currMainReq.getTime()
                + "')");
      }
    } catch (SQLException e) {
      System.out.println("JavaToSQL error in MaintenanceRequestImp");
      e.printStackTrace();
    }
  }

  @Override
  public void SQLToJava() {
    List = new HashMap<String, MaintenanceRequest>();
    try {
      ResultSet results;
      results = statement.executeQuery("SELECT * FROM MaintenanceRequest");

      while (results.next()) {
        String ID = results.getString("ID");
        String name = results.getString("name");
        String status = results.getString("status");
        String destination = results.getString("destination");
        String employee = results.getString("employee");
        String typeOfMaintenance = results.getString("typeOfMaintenance");
        String description = results.getString("description");
        String date = results.getString("date");
        String time = results.getString("time");

        MaintenanceRequest SQLRow =
            new MaintenanceRequest(
                ID,
                name,
                status,
                destination,
                checkEmployee(employee),
                typeOfMaintenance,
                description,
                date,
                time);

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
    fw.append("name");
    fw.append(",");
    fw.append("Status");
    fw.append(",");
    fw.append("Destination");
    fw.append(",");
    fw.append("Employee");
    fw.append(",");
    fw.append("Type of Maintenance");
    fw.append(",");
    fw.append("Description");
    fw.append(",");
    fw.append("Date");
    fw.append(",");
    fw.append("Time");
    fw.append("\n");

    for (MaintenanceRequest request : List.values()) {
      fw.append(request.getID());
      fw.append(",");
      fw.append(request.getName());
      fw.append(",");
      fw.append(request.getStatus());
      fw.append(",");
      fw.append(request.getDestination());
      fw.append(",");
      fw.append(request.getEmployee().getEmployeeID());
      fw.append(",");
      fw.append(request.getTypeOfMaintenance());
      fw.append(",");
      fw.append(request.getDescription());
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
    CSVToJava();
    System.out.println(
        "ID |\t Name |\t Status |\t Destination |\t Employee |\t Type Of Maintenance |\t Description |\t Date |\t Time");
    for (MaintenanceRequest request : this.List.values()) {
      System.out.println(
          request.ID
              + " | \t"
              + request.name
              + " | \t"
              + request.status
              + " | \t"
              + request.destination
              + " | \t"
              + request.getEmployee().getEmployeeID()
              + " | \t"
              + request.typeOfMaintenance
              + " | \t"
              + request.description
              + " | \t"
              + request.date
              + " | \t"
              + request.time);
    }
  }

  @Override
  public void edit(MaintenanceRequest data) throws IOException, SQLException {
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
        System.out.println("NO Such STAFF");
      }
    } else {
      System.out.println("Doesn't Exist");
    }
  }

  @Override
  public void add(MaintenanceRequest data) throws IOException, SQLException {
    if (List.containsKey(data.ID)) {
      System.out.println("A Request With This ID Already Exists");
    } else {
      if (EmployeeDaoImpl.List.containsKey(data.getEmployee().getEmployeeID())) {
        data.setEmployee(EmployeeDaoImpl.List.get(data.getEmployee().getEmployeeID()));
        this.List.put(data.ID, data);
        this.JavaToSQL();
        this.JavaToCSV(csvFile);
      } else {
        System.out.println("NO Such STAFF");
      }
    }
  }

  @Override
  public void remove(MaintenanceRequest data) throws IOException {
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
  public MaintenanceRequest askUser() {
    Scanner labInput = new Scanner(System.in);

    String inputID = "None";
    String inputName = "N/A";
    String inputStatus = "N/A";
    String inputDestination = "N/A";
    String inputEmployee = "N/A";
    String inputTypeOfMaintenance = "N/A";
    String inputDescription = "N/A";
    String inputDate = "N/A";
    String inputTime = "N/A";

    System.out.println("ID: ");
    inputID = labInput.nextLine();

    System.out.println("Input Type of Maintenance: ");
    inputTypeOfMaintenance = labInput.nextLine();

    System.out.println("Employee: ");
    inputEmployee = labInput.nextLine();

    Employee empty = new Employee(inputEmployee);

    return new MaintenanceRequest(
        inputID,
        inputName,
        inputStatus,
        inputDestination,
        empty,
        inputTypeOfMaintenance,
        inputDescription,
        inputDate,
        inputTime);
  }
}
