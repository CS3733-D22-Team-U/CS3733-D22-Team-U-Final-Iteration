package edu.wpi.cs3733.D22.teamU.BackEnd.Request.ReligiousRequest;

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

public class ReligiousRequestDaoImpl implements DataDao<ReligiousRequest> {
  public Statement statement;
  public String csvFile;
  public static HashMap<String, ReligiousRequest> List = new HashMap<String, ReligiousRequest>();
  public ArrayList<ReligiousRequest> list = new ArrayList<ReligiousRequest>();

  public ReligiousRequestDaoImpl(Statement statement, String csvfile) {
    this.csvFile = csvfile;
    this.statement = statement;
  }

  @Override
  public ArrayList<ReligiousRequest> list() {
    return null;
  }

  @Override
  public HashMap<String, ReligiousRequest> hList() {
    return this.List;
  }

  public Employee checkEmployee(String employee) {
    if (EmployeeDaoImpl.List.get(employee) != null) {
      return EmployeeDaoImpl.List.get(employee);
    } else {
      Employee empty = new Employee("N/A");
      return empty;
    }
  }

  public void CSVToJava() throws IOException {
    List = new HashMap<String, ReligiousRequest>();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    String[] header = br.readLine().split(",");
    int columns = header.length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == columns) {
        Employee temporary = checkEmployee(row[8]);
        ReligiousRequest r =
            new ReligiousRequest(
                row[0], row[1], row[2], row[3], row[4], row[5], row[6], row[7], temporary, row[9]);
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
          System.out.println(
              "Employee Not Found" + r.employee.getEmployeeID() + "ReligiousRequest");
        }
      }
    }
  }

  public void CSVToJava(ArrayList<Location> locations, HashMap<String, Employee> employees)
      throws IOException {
    List = new HashMap<String, ReligiousRequest>();
    String s;
    File file = new File(this.csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    String[] header = br.readLine().split(",");
    int columns = header.length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == columns) {
        Employee temporary = checkEmployee(row[8]);
        ReligiousRequest r =
            new ReligiousRequest(
                row[0],
                row[1],
                row[2],
                row[3],
                row[4],
                row[5],
                row[6],
                row[7],
                checkEmployee(row[8]),
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
          Employee e = employees.get(temporary.getEmployeeID());
          e.addRequest(r);
          r.setEmployee(e);
        } catch (Exception exception) {
        }
      }
    }
  }

  public void JavaToCSV(String csvFile) throws IOException {
    PrintWriter fw = new PrintWriter(new File(csvFile));

    fw.append("ID");
    fw.append(",");
    fw.append("Name");
    fw.append(",");
    fw.append("Date");
    fw.append(",");
    fw.append("Time");
    fw.append(",");
    fw.append("Patient");
    fw.append(",");
    fw.append("Religion");
    fw.append(",");
    fw.append("Status");
    fw.append(",");
    fw.append("Location");
    fw.append(",");
    fw.append("Employee");
    fw.append(",");
    fw.append("Notes");
    fw.append("\n");

    for (ReligiousRequest request : List.values()) {
      fw.append(request.getID());
      fw.append(",");
      fw.append(request.getName());
      fw.append(",");
      fw.append(request.getDate());
      fw.append(",");
      fw.append(request.getTime());
      fw.append(",");
      fw.append(request.getPatientName());
      fw.append(",");
      fw.append(request.getReligion());
      fw.append(",");
      fw.append(request.getStatus());
      fw.append(",");
      fw.append(request.getDestination());
      fw.append(",");
      fw.append(request.getEmployee().getEmployeeID());
      fw.append(",");
      fw.append(request.getNotes());
      fw.append("\n");
    }
    fw.close();
  }

  public void JavaToSQL() {

    try {
      statement.execute("Drop table ReligiousRequest");
    } catch (Exception e) {
      System.out.println("didn't drop table");
    }

    try {
      statement.execute(
          "CREATE TABLE ReligiousRequest("
              + "ID varchar(10) not null,"
              + "name varchar(50) not null, "
              + "date varchar(50) not null,"
              + "time varchar(50) not null,"
              + "patient varchar(50) not null,"
              + "religion varchar(50) not null,"
              + "status varchar(50) not null,"
              + "destination varchar(50) not null,"
              + "employee varchar(50) not null,"
              + "notes varchar(50) not null)");
      for (ReligiousRequest currReq : List.values()) {

        // checking if the data already exists
        DocumentReference docRef = db.collection("religRequests").document(currReq.getID());
        ApiFuture<DocumentSnapshot> ds = docRef.get();
        try {
          if (!ds.get().exists() || ds.get() == null) {
            // firebaseUpdate(currReq);
          }
        } catch (Exception e) {
          System.out.println("firebase error in java to sql religous requests");
        }

        statement.execute(
            "INSERT INTO ReligiousRequest VALUES("
                + "'"
                + currReq.getID()
                + "','"
                + currReq.getName()
                + "','"
                + currReq.getDate()
                + "','"
                + currReq.getTime()
                + "','"
                + currReq.getPatientName()
                + "','"
                + currReq.getReligion()
                + "','"
                + currReq.getStatus()
                + "','"
                + currReq.getDestination()
                + "','"
                + currReq.getEmployee().getEmployeeID()
                + "','"
                + currReq.getNotes()
                + "')");
      }
    } catch (SQLException e) {
      System.out.println("Connection failed. Check output console. religious request");
    }
  }

  public void firebaseUpdate(ReligiousRequest currReligReq) {
    DocumentReference docRef = db.collection("religRequests").document(currReligReq.getID());
    Map<String, Object> data = new HashMap<>();
    data.put("name", currReligReq.getName());
    data.put("date", currReligReq.getDate());
    data.put("time", currReligReq.getTime());
    data.put("patient", currReligReq.getPatient());
    data.put("religion", currReligReq.getReligion());
    data.put("status", currReligReq.getStatus());
    data.put("destination", currReligReq.getDestination());
    data.put("employeeID", currReligReq.getEmployee().getEmployeeID());
    data.put("notes", currReligReq.getNotes());
    docRef.set(data);
  }

  public void SQLToJava() {
    List = new HashMap<String, ReligiousRequest>();
    try {
      ResultSet results;
      results = statement.executeQuery("SELECT * FROM ReligiousRequest");

      while (results.next()) {
        String id = results.getString("ID");
        String name = results.getString("name");
        String date = results.getString("date");
        String time = results.getString("time");
        String patient = results.getString("patient");
        String religion = results.getString("religion");
        String status = results.getString("status");
        String destination = results.getString("destination");
        String employee = results.getString("employee");
        String notes = results.getString("notes");

        ReligiousRequest SQLRow =
            new ReligiousRequest(
                id,
                name,
                date,
                time,
                patient,
                religion,
                status,
                destination,
                checkEmployee(employee),
                notes);

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
        "ID |\t Name |\t Date |\t Time |\t Patient |\t Religion |\t Status |\t Destination |\t Employee");
    for (ReligiousRequest request : this.List.values()) {
      System.out.println(
          request.getID()
              + " | \t"
              + request.getName()
              + " | \t"
              + request.getDate()
              + " | \t"
              + request.getTime()
              + " | \t"
              + request.getPatient()
              + " | \t"
              + request.getReligion()
              + " | \t"
              + request.getStatus()
              + " | \t"
              + request.getDestination()
              + " | \t"
              + request.getEmployee().getEmployeeID()
              + " | \t"
              + request.getNotes());
    }
  }

  @Override
  public void edit(ReligiousRequest data) throws IOException {
    // takes entries from SQL table that match input node and updates it with a new floor and
    // location type
    // input ID
    if (List.containsKey(data.ID)) {
      if (EmployeeDaoImpl.List.containsKey(data.getEmployee().getEmployeeID())) {
        data.setEmployee(EmployeeDaoImpl.List.get(data.getEmployee().getEmployeeID()));
        this.List.replace(data.ID, data);
        // firebaseUpdate(data);
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
  public void add(ReligiousRequest data) throws IOException {
    if (List.containsKey(data.ID)) {
      System.out.println("A Request With This ID Already Exists");
    } else {
      if (EmployeeDaoImpl.List.containsKey(data.getEmployee().getEmployeeID())) {
        data.setEmployee(EmployeeDaoImpl.List.get(data.getEmployee().getEmployeeID()));
        this.List.put(data.ID, data);
        this.JavaToSQL();
        this.JavaToCSV(csvFile);
      } else {
        System.out.println("No Such STAFF");
      }
    }
  }

  @Override
  public void remove(ReligiousRequest data) throws IOException {
    // removes entries from SQL table that match input node
    try {
      this.List.remove(data.ID);
      db.collection("religRequests").document(data.getID()).delete();

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

  public ReligiousRequest askUser() {
    Scanner reqInput = new Scanner(System.in);

    String inputID = "None";
    String inputName = "N/A";
    String inputDate = "N/A";
    String inputTime = "N/A";
    String inputPatient = "N/A";
    String inputReligion = "N/A";
    String inputStatus = "N/A";
    String inputDestination = "N/A";
    String inputEmployee = "N/A";
    String inputNotes = "N/A";

    System.out.println("Input request ID: ");
    inputID = reqInput.nextLine();

    System.out.println("Input name: ");
    inputName = reqInput.nextLine();

    System.out.println("Input Religion ");
    inputReligion = reqInput.nextLine();

    System.out.println("Input Staff name: ");
    inputEmployee = reqInput.nextLine();

    Employee empty = new Employee(inputEmployee);

    return new ReligiousRequest(
        inputID,
        inputName,
        inputDate,
        inputTime,
        inputPatient,
        inputReligion,
        inputStatus,
        inputDestination,
        empty,
        inputNotes);
  }
}
