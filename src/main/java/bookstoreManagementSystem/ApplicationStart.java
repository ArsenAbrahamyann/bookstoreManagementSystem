package bookstoreManagementSystem;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ApplicationStart {
  static Scanner sc;

    /**
     * Starts the Bookstore Management System application. It displays a menu to the user and processes their choice
     * by calling the respective service.
     */
  public static void StartApplication() {
      sc = new Scanner(System.in);
      while (true) {
          int choice = 0;
          printManu();
          try {
              choice = sc.nextInt();
              sc.nextLine();
          } catch (InputMismatchException e) {
              sc.nextLine();
              continue;
              }

          switch (choice) {
              case 1:
                BookServices.bookStart();
                  break;
              case 2:
                CustomerServices.customerStart();
                  break;
              case 3:
                SalesServices.salesStart();
                  break;
              case 4:
                ServicesReportsImpl.ServicesReportsStart();
                  break;
              case 5:
                  System.out.println("Exiting the system.");
                  System.exit(0);
              default:
                  System.out.println("Invalid choice. Please try again.");
          }
      }
  }

    /**
     * Prints the main menu of the Bookstore Management System.
     */
  public static void printManu() {
      System.out.println("\nBookstore Management System ");
      System.out.println("1. Book ServicesReports");
      System.out.println("2. Customer ServicesReports");
      System.out.println("3. Sales Processing");
      System.out.println("4. Sales Reports");
      System.out.println("5. Exit");
      System.out.print("Enter your choice: ");
  }
}

