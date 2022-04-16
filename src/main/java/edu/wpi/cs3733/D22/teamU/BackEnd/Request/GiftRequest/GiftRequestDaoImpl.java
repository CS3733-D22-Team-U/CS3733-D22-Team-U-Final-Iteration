package edu.wpi.cs3733.D22.teamU.BackEnd.Request.GiftRequest;

import edu.wpi.cs3733.D22.teamU.BackEnd.DataDao;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;
import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.EmployeeDaoImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class GiftRequestDaoImpl implements DataDao<GiftRequest> {
    public Statement statement;
    public String csvFile;
    public HashMap<String, GiftRequest> List = new HashMap<String, GiftRequest>();
    public ArrayList<GiftRequest> list = new ArrayList<GiftRequest>();

    public GiftRequestDaoImpl(Statement statement, String csvFile)
    {
        this.statement = statement;
        this.csvFile = csvFile;
    }

    @Override
    public ArrayList<GiftRequest> list() {
        return null;
    }

    @Override
    public HashMap<String, GiftRequest> hList() {
        return this.List;
    }

    @Override
    public void CSVToJava() throws IOException, SQLException {
        List = new HashMap<String, GiftRequest>();
        String s;
        File file = new File(csvFile);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String[] header = br.readLine().split(",");
        int columns = header.length;
        while ((s = br.readLine()) != null) {
            String[] row = s.split(",");
            if (row.length == columns) {
                List.put(
                        row[0], new GiftRequest(row[0], row[1], row[2], row[3], row[4], row[5], checkEmployee(row[6]), row[7], row[8], row[9]));
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

    }

    @Override
    public void SQLToJava() {

    }

    @Override
    public void JavaToCSV(String csvFile) throws IOException {

    }

    @Override
    public void printTable() throws IOException, SQLException {

    }

    @Override
    public void edit(GiftRequest data) throws IOException, SQLException {

    }

    @Override
    public void add(GiftRequest data) throws IOException, SQLException {

    }

    @Override
    public void remove(GiftRequest data) throws IOException {

    }

    @Override
    public int search(String id) {
        return 0;
    }

    @Override
    public void saveTableAsCSV(String nameOfCSV) throws SQLException {

    }

    @Override
    public GiftRequest askUser() {
        return null;
    }
}
