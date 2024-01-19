package bookstoreManagementSystem;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ServicesReportsImpl {
    private static final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    private static final Connection connection = databaseConnection.getConnection();
    private static Scanner sc = new Scanner(System.in);

    /**
     * Initiates the services for generating and displaying reports related to sales, books, customers, and revenue
     * based on user input. The user is presented with a menu to choose from various report options.
     */
    public static void ServicesReportsStart() {
        int choirs = 0;
        while (true) {
            printManu();
            try {
                choirs = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                sc.nextLine();
                continue;
            }

            switch (choirs) {
                case 1:
                    if (SalesServices.checkSales() || BookServices.checkTableBooks() || CustomerServices.checkTableCustomers()) {
                        break;
                    }
                    informationAboutBooksSoldInTheSurvey();
                    break;
                case 2:
                    if (SalesServices.checkSales() || BookServices.checkTableBooks() || CustomerServices.checkTableCustomers()) {
                        break;
                    }
                    queryForFindingTheTotalRevenueGeneratedFromEachGenreOfBooks();
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Displays the menu options for the ServicesReports management system.
     */
    public static void printManu() {
            System.out.println("\nServicesReports Management System ");
            System.out.println("1. information about books sold in the survey");
            System.out.println("2. query for finding the total revenue generated from each genre of books");
            System.out.println("3. Back to Main Menu");
            System.out.print("Enter your choice: ");
    }


    /**
     * Retrieves and prints information about books sold in the survey,
     * including book title, customer name, and date of sale.
     */
    public static void informationAboutBooksSoldInTheSurvey() {
        String sql = """
                   SELECT  books.title AS bookTitle, Customers.name AS customerName, sales.dataOfSale
                        FROM sales
                        JOIN books ON sales.bookId = books.bookId
                        JOIN customers ON sales.customersId = customers.customerId;
                """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println("Book Title: " + resultSet.getString("bookTitle") +
                                   ", Customer Name: " + resultSet.getString("customerName") +
                                   ", Sale Data: " + resultSet.getDate("dataOfSale"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    /**
     * Executes a query to find the total revenue generated from each genre of books
     * and prints the results, including genre and total revenue.
     */
    public static void queryForFindingTheTotalRevenueGeneratedFromEachGenreOfBooks() {
        String sql = """
                 SELECT books.genre, SUM(sales.totalPrice) AS revenue
                    FROM sales
                    JOIN books ON sales.bookId = books.bookId
                    GROUP BY books.genre;
                """;
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String genre = resultSet.getString("genre");
                double revenue = resultSet.getDouble("revenue");

                System.out.println("Genre: " + genre + ", Total Revenue: " + revenue);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
