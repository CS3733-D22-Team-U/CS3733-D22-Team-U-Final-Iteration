package edu.wpi.cs3733.D22.teamU.frontEnd.pathFinding;

import edu.wpi.cs3733.D22.teamU.BackEnd.DataDao;
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

public class EdgeDao implements DataDao<Edge> {
  ArrayList<Edge> edgeArray;
  HashMap<String, Edge> edgeHash;
  public String csvFile;
  public Statement statement;

  public EdgeDao(Statement s, String csv) {
    statement = s;
    csvFile = csv;
  }

  @Override
  public ArrayList<Edge> list() {
    return edgeArray;
  }

  @Override
  public HashMap<String, Edge> hList() {
    return edgeHash;
  }

  @Override
  public void CSVToJava() throws IOException, SQLException {
    edgeArray = new ArrayList<>();
    edgeHash = new HashMap<>();
    HashMap<String, Location> locations = Udb.getInstance().locationImpl.hList();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    int size = br.readLine().split(",").length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == size) {
        Edge e = new Edge(row[0], locations.get(row[1]), locations.get(row[2]));
        edgeArray.add(e);
        edgeHash.put(e.getEdgeID(), e);
        locations.get(row[1]).getConnected().add(e);
        locations.get(row[2]).getConnected().add(e);
      }
    }
  }

  public void CSVToJava(HashMap<String, Location> locations) throws IOException, SQLException {
    edgeArray = new ArrayList<>();
    edgeHash = new HashMap<>();
    String s;
    File file = new File(csvFile);
    BufferedReader br = new BufferedReader(new FileReader(file));
    int size = br.readLine().split(",").length;
    while ((s = br.readLine()) != null) {
      String[] row = s.split(",");
      if (row.length == size) {
        try {
          Edge e = new Edge(row[0], locations.get(row[1]), locations.get(row[2]));
          edgeArray.add(e);
          edgeHash.put(e.getEdgeID(), e);
          locations.get(row[1]).getConnected().add(e); // todo check for dupes
          locations.get(row[2]).getConnected().add(e);
        } catch (Exception e) {

        }
      }
    }
  }

  @Override
  public void JavaToSQL() {}

  @Override
  public void SQLToJava() {}

  @Override
  public void JavaToCSV(String csvFile) throws IOException {}

  @Override
  public void printTable() throws IOException, SQLException {}

  @Override
  public void edit(Edge data) throws IOException, SQLException {}

  @Override
  public void add(Edge data) throws IOException, SQLException {}

  @Override
  public void remove(Edge data) throws IOException {}

  public int search(String id) {
    return 0;
  }

  @Override
  public void saveTableAsCSV(String nameOfCSV) throws SQLException {}

  @Override
  public Edge askUser() {
    return null;
  }
}
