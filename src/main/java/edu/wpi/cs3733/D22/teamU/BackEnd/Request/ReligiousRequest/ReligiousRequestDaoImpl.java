package edu.wpi.cs3733.D22.teamU.BackEnd.Request.ReligiousRequest;

import edu.wpi.cs3733.D22.teamU.BackEnd.DataDao;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.LocationDaoImpl;
import edu.wpi.cs3733.D22.teamU.BackEnd.Request.MedicineRequest.MedicineRequest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

    public Location checkLocation(String location){
        if (LocationDaoImpl.
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
                List.put(
                        row[0],
                        new ReligiousRequest(
                                row[0], row[1], row[2], row[3], row[4], row[5], checkEmployee(row[6]), row[7]));
            }
        }
    }

}
