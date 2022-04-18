package edu.wpi.cs3733.D22.teamU.BackEnd;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Menu {
  public Menu() {}

  public void menu() throws IOException, SQLException {

    Scanner userInput = new Scanner(System.in);

    System.out.println(
        "What database would you like to chose: \n"
            + "1 - Locations\n"
            + "2 - Employees\n"
            + "3 - Equipment\n"
            + "4 - Equipment Request\n"
            + "5 - Lab Request\n"
            + "6 - Laundry Request\n"
            + "7 - Medicine Request\n"
            + "8 - Gift Request\n"
            + "9 - Meal Request\n"
            + "10 - Religious Request\n"
            + "11 - Translator Request\n"
            + "12 -  Maintenance Request\n"
            + "13 - Computer Service Request\n"
            + "14 - Security Request \n"
            + "15 - Change Server\n"
            + "16 - Quit\n");

    switch (userInput.nextInt()) {
      case 1:
        locationMenu();
        break;
      case 2:
        employeesMenu();
        break;
      case 3:
        equipmentMenu();
        break;
      case 4:
        equipRequestMenu();
        break;
      case 5:
        labRequestMenu();
        break;

      case 6:
        laundryRequestMenu();
        break;

      case 7:
        medicineRequestMenu();
        break;

      case 8:
        giftRequestMenu();
        break;

      case 9:
        mealRequestMenu();
        break;

      case 10:
        religiousRequestMenu();
        break;

      case 11:
        translatorRequestMenu();
        break;

      case 13:
        maintenanceRequestMenu();
        break;
      case 14:
        compservRequestMenu();
        break;
      case 15:
        securityRequestMenu();
        break;
      case 16:
        serveChangeMenu();
        break;
      case 17:
        // exit whole menu
        break;
    }
  }

  private void serveChangeMenu() throws SQLException, IOException {
    Scanner changeInput = new Scanner(System.in);
    System.out.println("Press 1 for Embedded Database\n" + "Press 2 for Client Server Database\n");

    int a = changeInput.nextInt();

    if (a == 1) {
      Udb.getInstance().changeDriver(true);
    } else if (a == 2) {
      Udb.getInstance().changeDriver(false);
    } else {
      System.out.println("Not an option");
    }
    menu();
  }

  private void locationMenu() throws SQLException, IOException {
    Scanner locationsInput = new Scanner(System.in);

    System.out.println(
        "1 - List Location Information\n"
            + "2 - Change Location Floor and Type\n"
            + "3 - Enter New Location\n"
            + "4 - Delete Location \n"
            + "5 - Save Location Information to CSV file\n"
            + "6 - Return to Main Menu\n");

    switch (locationsInput.nextInt()) {
      case 1:
        Udb.getInstance().locationImpl.printTable();
        locationMenu();
        break;
      case 2:
        Udb.getInstance().edit(Udb.getInstance().locationImpl.askUser());
        locationMenu();
        break;
      case 3:
        Udb.getInstance().add(Udb.getInstance().locationImpl.askUser());
        locationMenu();
        break;
      case 4:
        Udb.getInstance().remove(Udb.getInstance().locationImpl.askUser());
        locationMenu();
        break;
      case 5:
        Scanner justNeedCSVName = new Scanner(System.in);

        System.out.println("Enter the name of the CSV file");
        String nameOfFile = justNeedCSVName.nextLine();

        Udb.getInstance().saveTableAsCSV("Locations", nameOfFile);
        locationMenu();
        break;
      case 6:
        // menu
        menu();
        break;
      default:
        System.out.println("Something went wrong");
        break;
    }
  }

  private void employeesMenu() throws SQLException, IOException {
    Scanner employeeInput = new Scanner(System.in);

    System.out.println(
        "1 - List Employee Information\n"
            + "2 - Edit Employee Information\n"
            + "3 - Enter New Employee\n"
            + "4 - Delete Employee\n"
            + "5 - Save Employee Information to CSV\n"
            + "6 - Return to Main Menu\n");
    switch (employeeInput.nextInt()) {
      case 1:
        Udb.getInstance().EmployeeImpl.printTable();
        employeesMenu();
        break;
      case 2:
        Udb.getInstance().edit(Udb.getInstance().EmployeeImpl.askUser());
        employeesMenu();
        break;
      case 3:
        Udb.getInstance().add(Udb.getInstance().EmployeeImpl.askUser());
        employeesMenu();
        break;
      case 4:
        Udb.getInstance().remove(Udb.getInstance().EmployeeImpl.askUser());
        employeesMenu();
        break;
      case 5:
        Scanner justNeedCSVName = new Scanner(System.in);

        System.out.println("Enter the name of the CSV file");
        String nameOfFile = justNeedCSVName.nextLine();

        Udb.getInstance().saveTableAsCSV("Employees", nameOfFile);
        employeesMenu();
        break;
      case 6:
        // menu
        menu();
        break;
    }
  }

  private void equipmentMenu() throws SQLException, IOException {
    Scanner equipmentInput = new Scanner(System.in);

    System.out.println(
        "1 - List Equipment Information\n"
            + "2 - Edit Equipment Information\n"
            + "3 - Enter New Equipment\n"
            + "4 - Delete Equipment\n"
            + "5 - Save Equipment Information to CSV\n"
            + "6 - Return to Main Menu\n");
    switch (equipmentInput.nextInt()) {
      case 1:
        Udb.getInstance().EquipmentImpl.printTable();
        equipmentMenu();
        break;
      case 2:
        Udb.getInstance().edit(Udb.getInstance().EquipmentImpl.askUser());
        equipmentMenu();
        break;
      case 3:
        Udb.getInstance().add(Udb.getInstance().EquipmentImpl.askUser());
        equipmentMenu();
        break;
      case 4:
        Udb.getInstance().remove(Udb.getInstance().EquipmentImpl.askUser());
        equipmentMenu();
        break;
      case 5:
        Scanner justNeedCSVName = new Scanner(System.in);

        System.out.println("Enter the name of the CSV file");
        String nameOfFile = justNeedCSVName.nextLine();

        Udb.getInstance().saveTableAsCSV("Equipments", nameOfFile);
        equipmentMenu();
        break;
      case 6:
        // menu
        menu();
        break;
    }
  }

  private void equipRequestMenu() throws IOException, SQLException {
    Scanner requestInput = new Scanner(System.in);

    System.out.println(
        "1 - List Equipment Request Information\n"
            + "2 - Edit Equipment Request Information\n"
            + "3 - Enter New Equipment Request\n"
            + "4 - Delete Equipment Request\n"
            + "5 - Save Equipment Request Information to CSV\n"
            + "6 - Return to Main Menu\n");
    switch (requestInput.nextInt()) {
      case 1:
        Udb.getInstance().equipRequestImpl.printTable();
        equipRequestMenu();
        break;
      case 2:
        Udb.getInstance().edit(Udb.getInstance().equipRequestImpl.askUser());
        equipRequestMenu();
        break;
      case 3:
        Udb.getInstance().add(Udb.getInstance().equipRequestImpl.askUser());
        equipRequestMenu();
        break;
      case 4:
        Udb.getInstance().remove(Udb.getInstance().equipRequestImpl.askUser());
        equipRequestMenu();
        break;
      case 5:
        Scanner justNeedCSVName = new Scanner(System.in);

        System.out.println("Enter the name of the CSV file");
        String nameOfFile = justNeedCSVName.nextLine();

        Udb.getInstance().saveTableAsCSV("EquipRequests", nameOfFile);
        equipRequestMenu();
        break;
      case 6:
        // menu
        menu();
        break;
      default:
        menu();
        break;
    }
  }

  private void labRequestMenu() throws SQLException, IOException {

    Scanner labMenu = new Scanner(System.in);

    System.out.println(
        "1 - List Lab Request Information\n"
            + "2 - Change Lab Request\n"
            + "3 - Enter New Lab Request\n"
            + "4 - Delete Lab Request\n"
            + "5 - Save Lab Request Information to CSV file\n"
            + "6 - Return to Main Menu\n");

    switch (labMenu.nextInt()) {
      case 1:
        Udb.getInstance().labRequestImpl.printTable();
        labRequestMenu();
        break;
      case 2:
        Udb.getInstance().labRequestImpl.edit(Udb.getInstance().labRequestImpl.askUser());
        labRequestMenu();
        break;
      case 3:
        Udb.getInstance().add(Udb.getInstance().labRequestImpl.askUser());
        labRequestMenu();
        break;
      case 4:
        Udb.getInstance().labRequestImpl.remove(Udb.getInstance().labRequestImpl.askUser());
        labRequestMenu();
        break;
      case 5:
        Scanner justNeedCSVName = new Scanner(System.in);

        System.out.println("Enter the name of the CSV file");
        String nameOfFile = justNeedCSVName.nextLine();

        Udb.getInstance().saveTableAsCSV("LabRequests", nameOfFile);
        labRequestMenu();
        break;
      case 6:
        // menu
        menu();
        break;
      default:
        labRequestMenu();
        break;
    }
  }

  private void laundryRequestMenu() throws SQLException, IOException {
    Scanner laundryInput = new Scanner(System.in);

    System.out.println(
        "1 - List Laundry Request Information\n"
            + "2 - Change Laundry Request\n"
            + "3 - Enter New Laundry Request\n"
            + "4 - Delete Laundry Request \n"
            + "5 - Save Laundry Request Information to CSV file\n"
            + "6 - Return to Main Menu\n");

    switch (laundryInput.nextInt()) {
      case 1:
        Udb.getInstance().laundryRequestImpl.printTable();
        laundryRequestMenu();
        break;
      case 2:
        Udb.getInstance().edit(Udb.getInstance().laundryRequestImpl.askUser());
        laundryRequestMenu();
        break;
      case 3:
        Udb.getInstance().add(Udb.getInstance().laundryRequestImpl.askUser());
        laundryRequestMenu();
        break;
      case 4:
        Udb.getInstance().remove(Udb.getInstance().laundryRequestImpl.askUser());
        laundryRequestMenu();
        break;
      case 5:
        Scanner justNeedCSVName = new Scanner(System.in);

        System.out.println("Enter the name of the CSV file");
        String nameOfFile = justNeedCSVName.nextLine();

        Udb.getInstance().saveTableAsCSV("LaundryRequest", nameOfFile);
        laundryRequestMenu();
        break;
      case 6:
        // menu
        menu();
        break;
      default:
        System.out.println("Something went wrong");
        menu();
        break;
    }
  }

  private void medicineRequestMenu() throws SQLException, IOException {
    Scanner medicineInput = new Scanner(System.in);

    System.out.println(
        "1 - List Medicine Request Information\n"
            + "2 - Change Medicine Request\n"
            + "3 - Enter New Medicine Request\n"
            + "4 - Delete Medicine Request \n"
            + "5 - Save Medicine Request Information to CSV file\n"
            + "6 - Return to Main Menu\n");

    switch (medicineInput.nextInt()) {
      case 1:
        Udb.getInstance().medicineRequestImpl.printTable();
        medicineRequestMenu();
        break;
      case 2:
        Udb.getInstance().edit(Udb.getInstance().medicineRequestImpl.askUser());
        medicineRequestMenu();
        break;
      case 3:
        Udb.getInstance().add(Udb.getInstance().medicineRequestImpl.askUser());
        medicineRequestMenu();
        break;
      case 4:
        Udb.getInstance().remove(Udb.getInstance().medicineRequestImpl.askUser());
        medicineRequestMenu();
        break;
      case 5:
        Scanner justNeedCSVName = new Scanner(System.in);

        System.out.println("Enter the name of the CSV file");
        String nameOfFile = justNeedCSVName.nextLine();

        Udb.getInstance().saveTableAsCSV("MedicineRequest", nameOfFile);
        medicineRequestMenu();
        break;
      case 6:
        // menu
        menu();
        break;
      default:
        System.out.println("Something went wrong");
        menu();
        break;
    }
  }

  private void giftRequestMenu() throws SQLException, IOException {
    Scanner giftInput = new Scanner(System.in);

    System.out.println(
        "1 - List Gift Request Information\n"
            + "2 - Change Gift Request\n"
            + "3 - Enter New Gift Request\n"
            + "4 - Delete Gift Request \n"
            + "5 - Save Gift Request Information to CSV file\n"
            + "6 - Return to Main Menu\n");

    switch (giftInput.nextInt()) {
      case 1:
        Udb.getInstance().giftRequestImpl.printTable();
        giftRequestMenu();
        break;
      case 2:
        Udb.getInstance().edit(Udb.getInstance().giftRequestImpl.askUser());
        giftRequestMenu();
        break;
      case 3:
        Udb.getInstance().add(Udb.getInstance().giftRequestImpl.askUser());
        giftRequestMenu();
        break;
      case 4:
        Udb.getInstance().remove(Udb.getInstance().giftRequestImpl.askUser());
        giftRequestMenu();
        break;
      case 5:
        Scanner justNeedCSVName = new Scanner(System.in);

        System.out.println("Enter the name of the CSV file");
        String nameOfFile = justNeedCSVName.nextLine();

        Udb.getInstance().saveTableAsCSV("GiftRequest", nameOfFile);
        giftRequestMenu();
        break;
      case 6:
        // menu
        menu();
        break;
      default:
        System.out.println("Something went wrong");
        menu();
        break;
    }
  }

  private void mealRequestMenu() throws SQLException, IOException {
    Scanner mealInput = new Scanner(System.in);

    System.out.println(
        "1 - List Meal Request Information\n"
            + "2 - Change Meal Request\n"
            + "3 - Enter New Meal Request\n"
            + "4 - Delete Meal Request \n"
            + "5 - Save Meal Request Information to CSV file\n"
            + "6 - Return to Main Menu\n");

    switch (mealInput.nextInt()) {
      case 1:
        Udb.getInstance().mealRequestImpl.printTable();
        mealRequestMenu();
        break;
      case 2:
        Udb.getInstance().edit(Udb.getInstance().mealRequestImpl.askUser());
        mealRequestMenu();
        break;
      case 3:
        Udb.getInstance().add(Udb.getInstance().mealRequestImpl.askUser());
        mealRequestMenu();
        break;
      case 4:
        Udb.getInstance().remove(Udb.getInstance().mealRequestImpl.askUser());
        mealRequestMenu();
        break;
      case 5:
        Scanner justNeedCSVName = new Scanner(System.in);

        System.out.println("Enter the name of the CSV file");
        String nameOfFile = justNeedCSVName.nextLine();

        Udb.getInstance().saveTableAsCSV("MealRequest", nameOfFile);
        mealRequestMenu();
        break;
      case 6:
        // menu
        menu();
        break;
      default:
        System.out.println("Something went wrong");
        menu();
        break;
    }
  }

  private void religiousRequestMenu() throws SQLException, IOException {
    Scanner mealInput = new Scanner(System.in);

    System.out.println(
            "1 - List Religious Request Information\n"
                    + "2 - Change Religious Request\n"
                    + "3 - Enter New Religious Request\n"
                    + "4 - Delete Religious Request \n"
                    + "5 - Save Religious Request Information to CSV file\n"
                    + "6 - Return to Main Menu\n");

    switch (mealInput.nextInt()) {
      case 1:
        Udb.getInstance().religiousRequestImpl.printTable();
        religiousRequestMenu();
        break;
      case 2:
        Udb.getInstance().edit(Udb.getInstance().religiousRequestImpl.askUser());
        religiousRequestMenu();
        break;
      case 3:
        Udb.getInstance().add(Udb.getInstance().religiousRequestImpl.askUser());
        religiousRequestMenu();
        break;
      case 4:
        Udb.getInstance().remove(Udb.getInstance().religiousRequestImpl.askUser());
        religiousRequestMenu();
        break;
      case 5:
        Scanner justNeedCSVName = new Scanner(System.in);

        System.out.println("Enter the name of the CSV file");
        String nameOfFile = justNeedCSVName.nextLine();

        Udb.getInstance().saveTableAsCSV("ReligiousRequest", nameOfFile);
        religiousRequestMenu();
        break;
      case 6:
        // menu
        menu();
        break;
      default:
        System.out.println("Something went wrong");
        menu();
        break;
    }
  }
  private void translatorRequestMenu() throws SQLException, IOException {
    Scanner employeeInput = new Scanner(System.in);

    System.out.println(
            "1 - List Translator Request Information\n"
                    + "2 - Change Translator Request\n"
                    + "3 - Enter New Translator Request\n"
                    + "4 - Delete Translator Request \n"
                    + "5 - Save Translator Request Information to CSV file\n"
                    + "6 - Return to Main Menu\n");
    switch (employeeInput.nextInt()) {
      case 1:
        Udb.getInstance().translatorRequestImpl.printTable();
        translatorRequestMenu();
        break;
      case 2:
        Udb.getInstance().edit(Udb.getInstance().translatorRequestImpl.askUser());
        translatorRequestMenu();
        break;
      case 3:
        Udb.getInstance().add(Udb.getInstance().translatorRequestImpl.askUser());
        translatorRequestMenu();
        break;
      case 4:
        Udb.getInstance().remove(Udb.getInstance().translatorRequestImpl.askUser());
        translatorRequestMenu();
        break;
      case 5:
        Scanner justNeedCSVName = new Scanner(System.in);

        System.out.println("Enter the name of the CSV file");
        String nameOfFile = justNeedCSVName.nextLine();

        Udb.getInstance().saveTableAsCSV("TranslateRequests", nameOfFile);
        translatorRequestMenu();
        break;
      case 6:
        // menu
        menu();
        break;
    }
  }
  private void maintenanceRequestMenu() throws SQLException, IOException {
    Scanner employeeInput = new Scanner(System.in);

    System.out.println(
            "1 - List Maintenance Request Information\n"
                    + "2 - Change Maintenance Request\n"
                    + "3 - Enter New Maintenance Request\n"
                    + "4 - Delete Maintenance Request \n"
                    + "5 - Save Maintenance Request Information to CSV file\n"
                    + "6 - Return to Main Menu\n");
    switch (employeeInput.nextInt()) {
      case 1:
        Udb.getInstance().maintenanceRequestImpl.printTable();
        maintenanceRequestMenu();
        break;
      case 2:
        Udb.getInstance().edit(Udb.getInstance().maintenanceRequestImpl.askUser());
        maintenanceRequestMenu();
        break;
      case 3:
        Udb.getInstance().add(Udb.getInstance().maintenanceRequestImpl.askUser());
        maintenanceRequestMenu();
        break;
      case 4:
        Udb.getInstance().remove(Udb.getInstance().maintenanceRequestImpl.askUser());
        maintenanceRequestMenu();
        break;
      case 5:
        Scanner justNeedCSVName = new Scanner(System.in);

        System.out.println("Enter the name of the CSV file");
        String nameOfFile = justNeedCSVName.nextLine();

        Udb.getInstance().saveTableAsCSV("MaintenanceRequests", nameOfFile);
        maintenanceRequestMenu();
        break;
      case 6:
        // menu
        menu();
        break;
    }
  }

  private void compservRequestMenu() throws SQLException, IOException {
    Scanner employeeInput = new Scanner(System.in);

    System.out.println(
            "1 - List Computer Service Request Information\n"
                    + "2 - Change Computer Service Request\n"
                    + "3 - Enter New Computer Service Request\n"
                    + "4 - Delete Computer Service Request\n"
                    + "5 - Save Computer Service Request Information to CSV file\n"
                    + "6 - Return to Main Menu\n");
    switch (employeeInput.nextInt()) {
      case 1:
        Udb.getInstance().compservRequestImpl.printTable();
        compservRequestMenu();
        break;
      case 2:
        Udb.getInstance().edit(Udb.getInstance().compservRequestImpl.askUser());
        compservRequestMenu();
        break;
      case 3:
        Udb.getInstance().add(Udb.getInstance().compservRequestImpl.askUser());
        compservRequestMenu();
        break;
      case 4:
        Udb.getInstance().remove(Udb.getInstance().compservRequestImpl.askUser());
        compservRequestMenu();
        break;
      case 5:
        Scanner justNeedCSVName = new Scanner(System.in);

        System.out.println("Enter the name of the CSV file");
        String nameOfFile = justNeedCSVName.nextLine();

        Udb.getInstance().saveTableAsCSV("CompServRequests", nameOfFile);
        compservRequestMenu();
        break;
      case 6:
        // menu
        menu();
        break;
    }
  }

  private void securityRequestMenu() throws SQLException, IOException {
    Scanner employeeInput = new Scanner(System.in);

    System.out.println(
            "1 - List Security Request Information\n"
                    + "2 - Change Security Request\n"
                    + "3 - Enter New Security Request\n"
                    + "4 - Delete Security Request\n"
                    + "5 - Save Security Request Information to CSV file\n"
                    + "6 - Return to Main Menu\n");
    switch (employeeInput.nextInt()) {
      case 1:
        Udb.getInstance().securityRequestImpl.printTable();
        securityRequestMenu();
        break;
      case 2:
        Udb.getInstance().edit(Udb.getInstance().securityRequestImpl.askUser());
        securityRequestMenu();
        break;
      case 3:
        Udb.getInstance().add(Udb.getInstance().securityRequestImpl.askUser());
        securityRequestMenu();
        break;
      case 4:
        Udb.getInstance().remove(Udb.getInstance().securityRequestImpl.askUser());
        securityRequestMenu();
        break;
      case 5:
        Scanner justNeedCSVName = new Scanner(System.in);

        System.out.println("Enter the name of the CSV file");
        String nameOfFile = justNeedCSVName.nextLine();

        Udb.getInstance().saveTableAsCSV("SecurityRequests", nameOfFile);
        securityRequestMenu();
        break;
      case 6:
        // menu
        menu();
        break;
    }
  }


}
