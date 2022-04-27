package edu.wpi.cs3733.D22.teamU.API.MedicineData;

import java.time.LocalDate;

/** Medicine Object */
public class Medicine {
  private String ID;
  private String medicineName;
  private String destinationID;
  private String patientID;
  private String employeeID;
  private LocalDate dateNeeded;
  private String amount;

  /**
   * Return amount of medication needed Note: Amount is a String since different units of medication
   * might be needed (ex. 1 pill is not the same as 1mg)
   *
   * @return String of amount
   */
  public String getAmount() {
    return amount;
  }

  /**
   * Set amount of medication needed Note: Amount is a String since different units of medication
   * might be needed (ex. 1 pill is not the same as 1mg)
   *
   * @param amount Amount of medicine
   */
  public void setAmount(String amount) {
    this.amount = amount;
  }

  public Medicine(
      String ID,
      String medicineName,
      String destinationID,
      String patientID,
      String employeeID,
      LocalDate dateNeeded,
      String amount) {
    setMedicineName(medicineName);
    setDestinationID(destinationID);
    setPatientID(patientID);
    setEmployeeID(employeeID);
    setDateNeeded(dateNeeded);
    setAmount(amount);
    setID(ID);
  }

  public Medicine(
      String medicineName,
      String destinationID,
      String patientID,
      String employeeID,
      LocalDate dateNeeded,
      String amount) {
    setMedicineName(medicineName);
    setDestinationID(destinationID);
    setPatientID(patientID);
    setEmployeeID(employeeID);
    setDateNeeded(dateNeeded);
    setAmount(amount);
    setID(makeID());
  }

  /**
   * Gets ID of object
   *
   * @return ID of Object
   */
  public String getID() {
    return ID;
  }

  public void setID(String ID) {
    this.ID = ID;
  }

  public String getMedicineName() {
    return medicineName;
  }

  public void setMedicineName(String medicineName) {
    this.medicineName = medicineName;
  }

  public String getDestinationID() {
    return destinationID;
  }

  public void setDestinationID(String destinationID) {
    this.destinationID = destinationID;
  }

  public String getPatientID() {
    return patientID;
  }

  public void setPatientID(String patientID) {
    this.patientID = patientID;
  }

  public String getEmployeeID() {
    return employeeID;
  }

  public void setEmployeeID(String employeeID) {
    this.employeeID = employeeID;
  }

  public LocalDate getDateNeeded() {
    return dateNeeded;
  }

  public void setDateNeeded(LocalDate dateNeeded) {
    this.dateNeeded = dateNeeded;
  }

  @Override
  public String toString() {
    return "Medicine{"
        + "ID='"
        + ID
        + '\''
        + ", medicineName='"
        + medicineName
        + '\''
        + ", destinationID='"
        + destinationID
        + '\''
        + ", patientID='"
        + patientID
        + '\''
        + ", employeeID='"
        + employeeID
        + '\''
        + ", dateNeeded="
        + dateNeeded
        + ", amount='"
        + amount
        + '\''
        + '}';
  }

  private String makeID() {
    return medicineName
        + "_"
        + destinationID
        + "_"
        + patientID
        + "_"
        + employeeID
        + "_"
        + dateNeeded.toString();
  }
}
