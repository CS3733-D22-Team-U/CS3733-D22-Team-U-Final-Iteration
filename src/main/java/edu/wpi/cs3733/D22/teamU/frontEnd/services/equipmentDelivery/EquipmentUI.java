package edu.wpi.cs3733.D22.teamU.frontEnd.services.equipmentDelivery;

import edu.wpi.cs3733.D22.teamU.BackEnd.Employee.Employee;

public class EquipmentUI {
  private String id;
  private String equipmentName;
  private String location;
  private int amountInUse;
  private int amountAvailable;
  private int totalAmount;
  private final String type = "Equipment";
  private String destination;
  private int requestAmount;
  private String requestDate;
  private String requestTime;
  private Employee employee;
  private String patient;
  private String floor;
  private String nodeType;


  public EquipmentUI(String name, int inUse, int available, int total, String location) {
    equipmentName = name;
    amountInUse = inUse;
    amountAvailable = available;
    totalAmount = total;
    this.location = location;
  }

  public EquipmentUI(
      String name, int inUse, int available, String location, String floor, String nodeType) {
    equipmentName = name;
    amountInUse = inUse;
    amountAvailable = available;
    this.location = location;
    this.floor = floor;
    this.nodeType = nodeType;
  }

  public EquipmentUI(
      String id,
      String name,
      int request,
      String destination,
      String date,
      String timestamp) {
    this.id = id;
    equipmentName = name;
    requestAmount = request;
    this.destination = destination;
    requestDate = date;
    requestTime = timestamp;
  }


  public EquipmentUI(
      String id, Employee employee, String patient, String name, int request, String destination, String floor, String nodeType) {
    this.id = id;
    this.employee = employee;
    this.patient = patient;
    equipmentName = name;
    requestAmount = request;
    this.destination = destination;
    this.floor = floor;
    this.nodeType = nodeType;
  }

  public int getRequestAmount() {
    return requestAmount;
  }

  public String getEquipmentName() {
    return equipmentName;
  }

  public String getRequestDate() {
    return requestDate;
  }

  public String getRequestTime() {
    return requestTime;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public String getDestination() {
    return destination;
  }

  public void setDestination(String destination) {
    this.destination = destination;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getFloor() {
    return floor;
  }

  public void setFloor(String floor) {
    this.floor = floor;
  }

  public String getNodeType() {
    return nodeType;
  }

  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
  }

  public void setEquipmentName(String equipmentName) {
    this.equipmentName = equipmentName;
  }

  public int getAmountInUse() {
    return amountInUse;
  }

  public void setAmountInUse(int amountInUse) {
    this.amountInUse = amountInUse;
  }

  public int getAmountAvailable() {
    return amountAvailable;
  }

  public void setAmountAvailable(int amountAvailable) {
    this.amountAvailable = amountAvailable;
  }

  public int getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(int totalAmount) {
    this.totalAmount = totalAmount;
  }

  public void setRequestAmount(int requestAmount) {
    this.requestAmount = requestAmount;
  }

  public void setRequestDate(String requestDate) {
    this.requestDate = requestDate;
  }

  public void setRequestTime(String requestTime) {
    this.requestTime = requestTime;
  }

  public Employee getEmployee() {
    return employee;
  }

  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  public String getPatient() {
    return patient;
  }

  public void setPatient(String patient) {
    this.patient = patient;
  }
}
