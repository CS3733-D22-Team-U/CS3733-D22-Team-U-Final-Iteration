package edu.wpi.cs3733.D22.teamU.API.Employee;

import java.util.Objects;

public class DefaultEmployee {
  private String ID;

  public DefaultEmployee(String ID) {
    this.ID = ID;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DefaultEmployee employee = (DefaultEmployee) o;
    return Objects.equals(ID, employee.ID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ID);
  }

  public String getID() {
    return ID;
  }

  public void setID(String ID) {
    this.ID = ID;
  }

  @Override
  public String toString() {
    return getID();
  }
}
