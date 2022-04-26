package edu.wpi.cs3733.D22.teamU.frontEnd.pathFinding;

import edu.wpi.cs3733.D22.teamU.BackEnd.DataDao;
import edu.wpi.cs3733.D22.teamU.BackEnd.Location.Location;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class EdgeDao implements DataDao<Edge> {
  ArrayList<Edge> edgeArray;
  HashMap<String, Edge> edgeHash;

  @Override
  public ArrayList<Edge> list() {
    return edgeArray;
  }

  @Override
  public HashMap<String, Edge> hList() {
    return edgeHash;
  }

  @Override
  public void CSVToJava() throws IOException, SQLException {}

  public void CSVToJava(ArrayList<Location> locations) throws IOException, SQLException {}

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

  @Override
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
