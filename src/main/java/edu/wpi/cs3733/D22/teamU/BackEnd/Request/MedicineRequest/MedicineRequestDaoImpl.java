package edu.wpi.cs3733.D22.teamU.BackEnd.Request.MedicineRequest;

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

public class MedicineRequestDaoImpl implements DataDao<MedicineRequest> {
  public Statement statement;
  public String csvFile;
  public static HashMap<String, MedicineRequest> List = new HashMap<String, MedicineRequest>();
  public ArrayList<MedicineRequest> list = new ArrayList<MedicineRequest>();

  public MedicineRequestDaoImpl(Statement statement, String csvfile)
      throws SQLException, IOException {
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

  public void CSVToJava() throws IOException {
    List = new HashMap<String, MedicineRequest>();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    int size = br.readLine().split(",").length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == size) { // or change to 9 if no work
        Employee temporary = checkEmployee(row[4]);
        MedicineRequest r =
            new MedicineRequest(
                row[0],
                row[1],
                Integer.parseInt(row[2]),
                row[3],
                row[4],
                temporary,
                row[6],
                row[7],
                row[8]);
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
    List = new HashMap<String, MedicineRequest>();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    int size = br.readLine().split(",").length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == size) { // or change to 9 if no work
        MedicineRequest r =
            new MedicineRequest(
                row[0],
                row[1],
                Integer.parseInt(row[2]),
                row[3],
                row[4],
                checkEmployee(row[5]),
                row[6],
                row[7],
                row[8]);
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
          Employee e = employees.get(row[5]);
          e.addRequest(r);
          r.setEmployee(e);
        } catch (Exception exception) {
          System.out.println(
              "Employee Not Found "
                  + r.getEmployee().getEmployeeID()
                  + " Medicine Request"
                  + r.getID());
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
    fw.append("Amount");
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
      fw.append(Integer.toString(request.getAmount()));
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
              + "amount int not null,"
              + "patientName varchar(50) not null,"
              + "status varchar(50) not null,"
              + "staff varchar(50) not null,"
              + "destination varchar(50) not null,"
              + "date varchar(10) not null,"
              + "time varchar(10) not null)");
      for (MedicineRequest currReq : List.values()) {

        // checking if the data already exists
        DocumentReference docRef = db.collection("medRequests").document(currReq.getID());
        ApiFuture<DocumentSnapshot> ds = docRef.get();
        try {
          if (!ds.get().exists() || ds.get() == null) {
            // firebaseUpdate(currReq);
          }
        } catch (Exception e) {
          System.out.println("firebase error in java to sql locations");
        }

        statement.execute(
            "INSERT INTO MedicineRequest VALUES("
                + "'"
                + currReq.getID()
                + "','"
                + currReq.getName()
                + "',"
                + currReq.getAmount()
                + ",'"
                + currReq.getPatientName()
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
      System.out.println("JavaToSQL error in MedicineRequestImp");
    }
  }

  public void firebaseUpdate(MedicineRequest currMedReq) {
    DocumentReference docRef = db.collection("medRequests").document(currMedReq.getID());
    Map<String, Object> data = new HashMap<>();
    data.put("name", currMedReq.getName());
    data.put("amount", currMedReq.getAmount());
    data.put("patientName", currMedReq.getPatientName());
    data.put("status", currMedReq.getStatus());
    data.put("employeeID", currMedReq.getEmployee().getEmployeeID());
    data.put("destination", currMedReq.getDestination());
    data.put("date", currMedReq.getDate());
    data.put("time", currMedReq.getTime());
    docRef.set(data);
  }

  public void SQLToJava() {
    List = new HashMap<String, MedicineRequest>();
    try {
      ResultSet results;
      results = statement.executeQuery("SELECT * FROM MedicineRequest");

      while (results.next()) {
        String id = results.getString("ID");
        String name = results.getString("name");
        int amount = results.getInt("amount");
        String patientName = results.getString("patientName");
        String status = results.getString("status");
        String staff = results.getString("staff");
        String location = results.getString("location");
        String date = results.getString("date");
        String time = results.getString("time");

        MedicineRequest SQLRow =
            new MedicineRequest(
                id, name, amount, patientName, status, checkEmployee(staff), location, date, time);

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
        "ID |\t Name |\t Amount |\t Patient Name |\t Status |\t Employee Name |\t Location |\t Date |\t Time");
    for (MedicineRequest request : this.List.values()) {
      System.out.println(
          request.ID
              + " | \t"
              + request.name
              + " | \t"
              + request.amount
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
        System.out.println("No Such STAFF");
      }
    }
  }

  @Override
  public void remove(MedicineRequest data) throws IOException {
    // removes entries from SQL table that match input node
    try {
      this.List.remove(data.ID);
      db.collection("medRequests").document(data.getID()).delete();

      this.JavaToSQL();
      this.JavaToCSV(csvFile);
    } catch (Exception e) {
      System.out.println("This Data Point Was Not Found");
    }
  }

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
    int inputAmount;
    String inputPatientName = "N/A";
    String inputStatus = "N/A";
    String inputStaff = "N/A";
    String inputLocation = "FDEPT00101";
    String inputDate = "N/A";
    String inputTime = "N/A";

    System.out.println("Input request ID: ");
    inputID = reqInput.nextLine();

    System.out.println("Input name: ");
    inputName = reqInput.nextLine();

    System.out.println("Input request amount: ");
    inputAmount = Integer.parseInt(reqInput.nextLine());

    System.out.println("Input Staff name: ");
    inputStaff = reqInput.nextLine();

    Employee empty = new Employee(inputStaff);

    return new MedicineRequest(
        inputID,
        inputName,
        inputAmount,
        inputPatientName,
        inputStatus,
        empty,
        inputLocation,
        inputDate,
        inputTime);
  }
}
