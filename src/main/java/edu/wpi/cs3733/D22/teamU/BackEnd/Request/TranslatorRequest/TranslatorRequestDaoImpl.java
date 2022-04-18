package edu.wpi.cs3733.D22.teamU.BackEnd.Request.TranslatorRequest;

import edu.wpi.cs3733.D22.teamU.BackEnd.DataDao;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

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
  public void SQLToJava() {}

  @Override
  public void JavaToCSV(String csvFile) throws IOException {}

  @Override
  public void printTable() throws IOException, SQLException {}

  @Override
  public void edit(TranslatorRequest data) throws IOException, SQLException {}

  @Override
  public void add(TranslatorRequest data) throws IOException, SQLException {}

  @Override
  public void remove(TranslatorRequest data) throws IOException {}

  @Override
  public int search(String id) {
    return 0;
  }

  @Override
  public void saveTableAsCSV(String nameOfCSV) throws SQLException {}

  @Override
  public TranslatorRequest askUser() {
    return null;
  }
}
