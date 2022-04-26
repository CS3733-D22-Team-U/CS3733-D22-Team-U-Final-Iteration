package edu.wpi.cs3733.D22.teamU.BackEnd.Request.LaundryRequest;

import com.google.cloud.firestore.DocumentReference;
import edu.wpi.cs3733.D22.teamU.BackEnd.DataDao;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class LaundryRequestDaoImpl implements DataDao<LaundryRequest> {
  public Statement statement;
  public String csvFile;
  public static HashMap<String, LaundryRequest> List = new HashMap<String, LaundryRequest>();
  public ArrayList<LaundryRequest> list = new ArrayList<LaundryRequest>();

  public LaundryRequestDaoImpl(Statement statement, String csvFile)
      throws SQLException, IOException {
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

  // Checks whether an employee exists
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
        Employee temporary = checkEmployee(row[2]);
        LaundryRequest r =
            new LaundryRequest(
                row[0], row[1], temporary, row[3], row[4], row[5], row[6], row[7], row[8], row[9]);
        List.put(row[0], r);
        try {
          Location temp = new Location();
          temp.setNodeID(r.destination);
          Location l =
              Udb.getInstance()
                  .locationImpl
                  .locations
                  .get(Udb.getInstance().locationImpl.locations.indexOf(temp));
          l.setNodeType("SERV");
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
      throws IOException {
    List = new HashMap<String, LaundryRequest>();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    String[] header = br.readLine().split(",");
    int columns = header.length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == columns) {
        LaundryRequest r =
            new LaundryRequest(
                row[0],
                row[1],
                checkEmployee(row[2]),
                row[3],
                row[4],
                row[5],
                row[6],
                row[7],
                row[8],
                row[9]);
        List.put(row[0], r);
        try {
          Location temp = new Location();
          temp.setNodeID(r.destination);
          Location l = locations.get(locations.indexOf(temp));
          l.setNodeType("SERV");
          l.addRequest(r);
          r.setLocation(l);
        } catch (Exception exception) {
        }
        try {
          Employee e = employees.get(row[2]);
          e.addRequest(r);
          r.setEmployee(e);
        } catch (Exception exception) {
          System.out.println(
              "Employee Not Found "
                  + r.getEmployee().getEmployeeID()
                  + " Laundry Request"
                  + r.getID());
        }
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
              + "patientName varchar(20) not null,"
              + "staff varchar(20) not null,"
              + "status varchar(20) not null,"
              + "location varchar(15) not null,"
              + "pickUp varchar(10) not null,"
              + "dropOff varchar(10) not null,"
              + "time varchar(10) not null,"
              + "services varchar(50) not null,"
              + "notes varchar(50) not null)");

      for (LaundryRequest currLaud : List.values()) {
        //firebaseUpdate(currLaud);

        statement.execute(
            "INSERT INTO LaundryRequest VALUES("
                + "'"
                + currLaud.getID()
                + "','"
                + currLaud.getPatientName()
                + "','"
                + currLaud.getEmployee().getEmployeeID()
                + "','"
                + currLaud.getStatus()
                + "','"
                + currLaud.getLocation().getNodeID()
                + "','"
                + currLaud.getPickUpDate()
                + "','"
                + currLaud.getDropOffDate()
                + "','"
                + currLaud.getTime()
                + "','"
                + currLaud.getServices()
                + "','"
                + currLaud.getNotes()
                + "')");
      }
    } catch (SQLException e) {
      System.out.println("JavaToSQL error in LaundryRequestImp");
      e.printStackTrace();
    }
  }

  public void firebaseUpdate(LaundryRequest currLaundReq) {
    DocumentReference docRef = db.collection("laundryRequests").document(currLaundReq.getID());
    Map<String, Object> data = new HashMap<>();
    data.put("patientName", currLaundReq.getPatientName());
    data.put("employeeID", currLaundReq.getEmployee().getEmployeeID());
    data.put("status", currLaundReq.getStatus());
    data.put("destination", currLaundReq.getDestination());
    data.put("pickUpDate", currLaundReq.getPickUpDate());
    data.put("dropOffDate", currLaundReq.getDropOffDate());
    data.put("time", currLaundReq.getTime());
    data.put("services", currLaundReq.getServices());
    data.put("notes", currLaundReq.getNotes());
    docRef.set(data);
  }

  @Override
  public void SQLToJava() {
    List = new HashMap<String, LaundryRequest>();
    try {
      ResultSet results;
      results = statement.executeQuery("SELECT * FROM LaundryRequest");

      while (results.next()) {
        String ID = results.getString("ID");
        String patientName = results.getString("patientName");
        String staff = results.getString("staff");
        String status = results.getString("status");
        String location = results.getString("location");
        String pickUp = results.getString("pickUp");
        String dropOff = results.getString("dropOff");
        String time = results.getString("time");
        String services = results.getString("services");
        String notes = results.getString("notes");

        LaundryRequest SQLRow =
            new LaundryRequest(
                ID,
                patientName,
                checkEmployee(staff),
                status,
                location,
                pickUp,
                dropOff,
                time,
                services,
                notes);

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
    fw.append("PatientName");
    fw.append(",");
    fw.append("Staff");
    fw.append(",");
    fw.append("Status");
    fw.append(",");
    fw.append("Location");
    fw.append(",");
    fw.append("Pick Up");
    fw.append(",");
    fw.append("Drop Off");
    fw.append(",");
    fw.append("Time");
    fw.append(",");
    fw.append("Services");
    fw.append(",");
    fw.append("Notes");
    fw.append("\n");

    for (LaundryRequest request : List.values()) {
      fw.append(request.getID());
      fw.append(",");
      fw.append(request.getPatientName());
      fw.append(",");
      fw.append(request.getEmployee().getEmployeeID());
      fw.append(",");
      fw.append(request.getStatus());
      fw.append(",");
      fw.append(request.getDestination());
      fw.append(",");
      fw.append(request.getPickUpDate());
      fw.append(",");
      fw.append(request.getDropOffDate());
      fw.append(",");
      fw.append(request.getTime());
      fw.append(",");
      fw.append(request.getServices());
      fw.append(",");
      fw.append(request.getNotes());
      fw.append("\n");
    }
    fw.close();
  }

  @Override
  public void printTable() throws IOException {
    CSVToJava();
    System.out.println(
        "ID |\t Patient Name |\t Staff |\t Status |\t Location |\t Pick Up |\t Drop Off \t Time |\t Services |\t Notes");
    for (LaundryRequest request : this.List.values()) {
      System.out.println(
          request.ID
              + " | \t"
              + request.patientName
              + " | \t"
              + request.getEmployee().getEmployeeID()
              + " | \t"
              + request.status
              + " | \t"
              + request.destination
              + " | \t"
              + request.pickUpDate
              + " | \t"
              + request.dropOffDate
              + " | \t"
              + request.time
              + " | \t"
              + request.services
              + " | \t"
              + request.notes);
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
        System.out.println("NO Such STAFF");
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
      db.collection("laundryRequests").document(data.getID()).delete();

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
    String inputPatient = "N/A";
    String inputStaff = "N/A";
    String inputTime = "N/A";
    String inputStatus = "N/A";
    String inputLocation = "FDEPT00101";
    String inputPick = "N/A";
    String inputDrop = "N/A";
    String inputServices = "N/A";
    String inputNotes = "N/A";

    System.out.println("Input ID: ");
    inputID = labInput.nextLine();

    System.out.println("Staff Name: ");
    inputStaff = labInput.nextLine();

    Employee empty = new Employee(inputStaff);

    return new LaundryRequest(
        inputID,
        inputPatient,
        empty,
        inputStatus,
        inputLocation,
        inputPick,
        inputDrop,
        inputTime,
        inputServices,
        inputNotes);
  }
}
