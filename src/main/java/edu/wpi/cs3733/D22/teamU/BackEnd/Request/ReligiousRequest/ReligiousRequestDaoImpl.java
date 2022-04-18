package edu.wpi.cs3733.D22.teamU.BackEnd.Request.ReligiousRequest;

import edu.wpi.cs3733.D22.teamU.BackEnd.DataDao;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.LocationDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MedicineRequest.MedicineRequest;
import edu.wpi.cs3733.D22.teamU.BackEnd.Udb;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class ReligiousRequestDaoImpl implements DataDao<ReligiousRequest> {
    public Statement statement;
    public String csvFile;
    public HashMap<String, ReligiousRequest> List = new HashMap<String, ReligiousRequest>();

    public ReligiousRequestDaoImpl(Statement statement, String csvfile) {
        this.csvFile = csvfile;
        this.statement = statement;
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
        br.readLine();
        while ((s = br.readLine()) != null) {
            String[] row = s.split(",");
            if (row.length == 8) {
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
                                checkEmployee(row[8]));
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
                            + "employee varchar(50) not null)");
            for (ReligiousRequest currReq : List.values()) {
                statement.execute(
                        "INSERT INTO MedicineRequest VALUES("
                                + "'"
                                + currReq.getID()
                                + "','"
                                + currReq.getName()
                                + "','"
                                + currReq.getDate()
                                + "','"
                                + currReq.getTime()
                                + "','"
                                + currReq.getPatient()
                                + "','"
                                + currReq.getReligion()
                                + "','"
                                + currReq.getStatus()
                                + "','"
                                + currReq.getDestination()
                                + "','"
                                + currReq.getEmployee().getEmployeeID()
                                + "')");
            }
        } catch (SQLException e) {
            System.out.println("Connection failed. Check output console.");
        }
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
                String patientName = results.getString("patientName");
                String status = results.getString("status");
                String staff = results.getString("staff");
                String location = results.getString("location");
                String date = results.getString("date");
                String employee = results.getString("time");

                ReligiousRequest SQLRow =
                        new ReligiousRequest(
                                id, name, date, time, patient, checkEmployee(staff), location, date, time);

                List.put(id, SQLRow);
            }
        } catch (SQLException e) {
            System.out.println("request not found");
        }
    }

}
