package edu.wpi.cs3733.D22.teamU.frontEnd.services.equipmentDelivery;

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

  private int priority;

  public EquipmentUI(String name, int inUse, int available, int total, String location) {
    equipmentName = name;
    amountInUse = inUse;
    amountAvailable = available;
    totalAmount = total;
    this.location = location;
  }

  public EquipmentUI(
      String id,
      String name,
      int request,
      String destination,
      String date,
      String timestamp,
      int priority) {
    this.id = id;
    equipmentName = name;
    requestAmount = request;
    this.destination = destination;
    requestDate = date;
    requestTime = timestamp;
    this.priority = priority;
  }

  public int getRequestAmount() {
    return requestAmount;
  }

  public String getEquipmentName() {
    return equipmentName;
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

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }
}
