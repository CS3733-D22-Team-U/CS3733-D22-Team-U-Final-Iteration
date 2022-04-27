package edu.wpi.cs3733.D22.teamU.API.MedicineData;

import java.util.HashMap;

/**
 * MedicineDao contains a HashMap of medicine, with the Value being the Medicine Object and it's key
 * being the String ID value of the Medicine
 */
public abstract class MedicineDao {
  private HashMap<String, Medicine> medicines = new HashMap<>();

  /**
   * Have MedicineDao do a certain function after updating the HashMap
   *
   * @param m The Medicine Object that was added
   */
  public abstract void add(Medicine m);

  /**
   * Have MedicineDao do a certain function after updating the HashMap
   *
   * @param m The Medicine Object that was edited
   */
  public abstract void edit(Medicine m);

  /**
   * Have MedicineDao do a certain function after updating the HashMap
   *
   * @param m The Medicine Object that was removed
   */
  public abstract void remove(Medicine m);

  /** @return Gets the Medicine HashMap */
  public HashMap<String, Medicine> getMedicines() {
    return medicines;
  }

  /**
   * Sets the Medicine HashMap. Useful for an initial load, such as filling out the list from stored
   * values in an SQL Database.
   *
   * @param medicines HashMap<String,Medicine> of the medicines, with String being the Medicine's ID
   */
  public void setMedicines(HashMap<String, Medicine> medicines) {
    this.medicines = medicines;
  }
}
