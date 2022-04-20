package edu.wpi.cs3733.D22.teamU.frontEnd.services;

import java.io.IOException;
import java.sql.SQLException;

public interface Service {

  public void addRequest() throws SQLException, IOException;

  public void removeRequest();

  public void updateRequest();
}
