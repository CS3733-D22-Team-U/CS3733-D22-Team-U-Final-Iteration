package edu.wpi.cs3733.D22.teamU.BackEnd.Request.EquipRequest;

import edu.wpi.cs3733.D22.teamU.BackEnd.DataDao;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class EquipRequestDaoImpl implements DataDao<EquipRequest> {
  public Statement statement;
  public HashMap<String, EquipRequest> List = new HashMap<String, EquipRequest>();
  public String csvFile;
  public ArrayList<EquipRequest> list = new ArrayList<EquipRequest>();

  public EquipRequestDaoImpl(Statement statement, String csvfile) throws SQLException, IOException {
    this.csvFile = csvfile;
    this.statement = statement;
  }

  @Override
  public ArrayList<EquipRequest> list() {
    return null;
  }

  @Override
  public HashMap<String, EquipRequest> hList() {
    return this.List;
  }

  public Employee checkEmployee(String employee) throws NullPointerException {
    if (EmployeeDaoImpl.List.get(employee) != null) {
      return EmployeeDaoImpl.List.get(employee);
    } else {
      Employee empty = new Employee("N/A");
      return empty;
    }
  }
  /**
   * Reads CSV file and puts the Equipment into an array list: EquipmentList
   *
   * @throws IOException
   */
  public void CSVToJava() throws IOException {
    List = new HashMap<String, EquipRequest>();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    int size = br.readLine().split(",").length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == size) {
        EquipRequest r =
            new EquipRequest(
                row[0],
                row[1],
                Integer.parseInt(row[2]),
                row[3],
                row[4],
                checkEmployee(row[5]),
                row[6],
                row[7],
                row[8],
                Integer.parseInt(row[9]));
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
      }
    }
  }

  public void CSVToJava(ArrayList<Location> locations) throws IOException {
    List = new HashMap<String, EquipRequest>();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    int size = br.readLine().split(",").length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == size) {
        EquipRequest e =
            new EquipRequest(
                row[0],
                row[1],
                Integer.parseInt(row[2]),
                row[3],
                row[4],
                checkEmployee(row[5]),
                row[6],
                row[7],
                row[8],
                Integer.parseInt(row[9]));
        List.put(row[0], e);
        try {
          Location temp = new Location();
          temp.setNodeID(e.destination);
          Location l = locations.get(locations.indexOf(temp));
          l.addRequest(e);
          e.setLocation(l);
        } catch (Exception exception) {
        }
      }
    }
  }

  /**
   * Copies the array list: EquipmentList and writes it into the CSV file
   *
   * @param csvFile
   * @throws IOException
   */
  public void JavaToCSV(String csvFile) throws IOException {
    PrintWriter fw = new PrintWriter(new File(csvFile));

    fw.append("ID");
    fw.append(",");
    fw.append("Name");
    fw.append(",");
    fw.append("Amount");
    fw.append(",");
    fw.append("Type");
    fw.append(",");
    fw.append("Status");
    fw.append(",");
    fw.append("Employee");
    fw.append(",");
    fw.append("Destination");
    fw.append(",");
    fw.append("Date");
    fw.append(",");
    fw.append("Time");
    fw.append(",");
    fw.append("Priority");
    fw.append("\n");

    for (EquipRequest request : List.values()) {
      fw.append(request.getID());
      fw.append(",");
      fw.append(request.getName());
      fw.append(",");
      fw.append(Integer.toString(request.getAmount()));
      fw.append(",");
      fw.append(request.getType());
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
      fw.append(Integer.toString(request.getPri()));
      fw.append("\n");
    }
    fw.close();
  }

  public void JavaToSQL() {

    try {
      statement.execute("Drop table EquipRequest");
    } catch (Exception e) {
      System.out.println("didn't drop table");
    }

    try {
      statement.execute(
          "CREATE TABLE EquipRequest("
              + "ID varchar(10) not null,"
              + "name varchar(50) not null, "
              + "amount int not null,"
              + "typeOfRequest varchar(10),"
              + "status varchar(15) not null,"
              + "staff varchar(20) not null,"
              + "destination varchar(10) not null,"
              + "date varchar(10) not null,"
              + "time varchar(10) not null,"
              + "pri int not null)");

      for (EquipRequest currReq : List.values()) {
        statement.execute(
            "INSERT INTO EquipRequest VALUES("
                + "'"
                + currReq.getID()
                + "','"
                + currReq.getName()
                + "',"
                + currReq.getAmount()
                + ",'"
                + currReq.getType()
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
                + "',"
                + currReq.getPri()
                + ")");
      }
    } catch (SQLException e) {
      System.out.println("JavaToSQL error in EquipRequestImp");
    }
  }

  public void SQLToJava() {
    List = new HashMap<String, EquipRequest>();
    try {
      ResultSet results;
      results = statement.executeQuery("SELECT * FROM EquipRequest");

      while (results.next()) {
        String id = results.getString("ID");
        String name = results.getString("name");
        int amount = results.getInt("amount");
        String type = results.getString("typeOfRequest");
        String status = results.getString("status");
        String staff = results.getString("staff");
        String destination = results.getString("destination");
        String date = results.getString("date");
        String time = results.getString("time");
        int pri = results.getInt("pri");

        EquipRequest SQLRow =
            new EquipRequest(
                id, name, amount, type, status, checkEmployee(staff), destination, date, time, pri);

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
        "ID |\t Name |\t Amount |\t Type |\t Staff |\t Destination |\t Date |\t Time |\t Priority");
    for (EquipRequest request : this.List.values()) {
      System.out.println(
          request.ID
              + " | \t"
              + request.name
              + " | \t"
              + request.amount
              + " | \t"
              + request.typeOfRequest
              + " | \t"
              + request.employee.getEmployeeID()
              + " | \t"
              + request.destination
              + " | \t"
              + request.date
              + " | \t"
              + request.time
              + " | \t"
              + request.pri);
    }
  }

  @Override
  public void edit(EquipRequest data) throws IOException, SQLException {
    // takes entries from SQL table that match input node and updates it with a new floor and
    // location type
    // input ID

    if (List.containsKey(data.ID)) {
      if (EmployeeDaoImpl.List.containsKey(data.getEmployee().getEmployeeID())) {
        data.updateLocation(data.destination, Udb.getInstance().locationImpl.list());
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

  /**
   * Prompts user for the name of a new item and then adds it to the csv file and database
   *
   * @param //csvFile
   * @throws IOException
   */
  @Override
  public void add(EquipRequest data) throws IOException, SQLException {

    if (List.containsKey(data.ID)) {
      System.out.println("A Request With This ID Already Exists");
    } else {
      if (EmployeeDaoImpl.List.containsKey(data.getEmployee().getEmployeeID())) {
        data.updateLocation(data.destination, Udb.getInstance().locationImpl.list());
        data.setEmployee(EmployeeDaoImpl.List.get(data.getEmployee().getEmployeeID()));
        this.List.put(data.ID, data);
        this.JavaToSQL();
        this.JavaToCSV(csvFile);
      } else {
        System.out.println("No Such Staff Exists in Database");
      }
    }
  }

  /**
   * Prompts user for the name of the item they wish to remove and then removes that item from the
   * database and csv file
   *
   * @throws IOException
   */
  @Override
  public void remove(EquipRequest data) throws IOException {
    try {
      data.location.getRequests().remove(data);
      List.remove(data.ID);
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

  /**
   * Prompts user for the name of a new file and then creates the new file in the project folder
   * then it copies the database table: EquipmentList into the CSV file
   *
   * @throws SQLException
   */
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

  public EquipRequest askUser() {
    Scanner reqInput = new Scanner(System.in);

    String inputID = "None";
    String inputName = "N/A";
    int inputAmount = 0;
    String inputType = "N/A";
    String inputStaff = "N/A";
    String inputDestination = "FDEPT00101";
    String inputDate = "N/A";
    String inputTime = "N/A";
    String inputStatus = "N/A";
    int inputPriority = 0;

    System.out.println("Input request ID: ");
    inputID = reqInput.nextLine();

    System.out.println("Input name: ");
    inputName = reqInput.nextLine();

    System.out.println("Input staff: ");
    inputStaff = reqInput.nextLine();

    Employee empty = new Employee(inputStaff);

    return new EquipRequest(
        inputID,
        inputName,
        inputAmount,
        inputType,
        inputStatus,
        empty,
        inputDestination,
        inputDate,
        inputTime,
        inputPriority);
  }
}
