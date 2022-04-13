package edu.wpi.cs3733.D22.teamU.BackEnd;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public interface DataDao<T> {
  String csvFile = null;
  // ArrayList<T> list;
  ArrayList<T> list();

  HashMap<String, T> hList();

  void CSVToJava() throws IOException;

  void JavaToSQL();

  void SQLToJava();

  void JavaToCSV(String csvFile) throws IOException;

  void printTable() throws IOException;

  void edit(T data) throws IOException;

  void add(T data) throws IOException;

  void remove(T data) throws IOException;

  int search(String id);

  void saveTableAsCSV(String nameOfCSV) throws SQLException;

  T askUser();
}
