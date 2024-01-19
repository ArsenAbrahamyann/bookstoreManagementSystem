package bookstoreManagementSystem;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SalesServices {
    private static final DatabaseConnection DBC = DatabaseConnection.getInstance();
    private static final Connection connection = DBC.getConnection();
    private static Scanner sc = new Scanner(System.in);


    /**
     * Initiates the sales management system, allowing users to choose from various options.
     */
    public static void salesStart() {
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
                    if ( BookServices.checkTableBooks() || CustomerServices.checkTableCustomers() ) {
                        break;
                    }
                    creatTable();
                    break;
                case 2:
                   if (checkSales() || BookServices.checkTableBooks() || CustomerServices.checkTableCustomers()) {
                       break;
                   }
                    insertSales(exemplary());
                    break;
                case 3:
                    if (checkSales() || BookServices.checkTableBooks() || CustomerServices.checkTableCustomers()) {
                        break;
                    }
                    calculateTotalRevenueByGenre();
                    break;
                case 4:
                    if(checkSales()) {
                       break;
                    }
                    deleteSalesAll();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Prints the sales management menu for user selection.
     */
    public static void printManu() {
        System.out.println("\nSales Management System ");
        System.out.println("1. creat sales table");
        System.out.println("2. insert sales");
        System.out.println("3. See calculate total revenue by genre");
        System.out.println("4. delete all table sales");
        System.out.println("5. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }

    /**
     * Generates an exemplary Sales object based on user input. It prompts the user to enter a BookId, customersId, and
     * quantitySold, calculates the totalPrice, and returns a Sales object with the provided information.
     *
     * @return An exemplary Sales object with user-provided information.
     */
    public static Sales exemplary() {
        BookServices.printAllBooks();
        System.out.println("Please enter BookId:");
        int bookId = getPositiveIntInput("Invalid input. Please enter a valid BookId.");
        CustomerServices.printAllCustomers();
        System.out.println("Please enter customersId:");
        int customersId = getPositiveIntInput("Invalid input. Please enter a valid customersId.");
        System.out.println("Please enter quantitySold:");
        int quantitySold = getPositiveIntInput("Invalid input. Please enter a valid quantitySold.");
        double totalPrice = quantitySold * BookServices.searchBookIdByPrice(bookId);

        Sales sales = new Sales(bookId,customersId,quantitySold,totalPrice);


        return sales;
    }

    /**
     * Reads and validates a positive integer input from the user through the console. It displays an error message and
     * prompts for re-entry until a valid positive integer is provided.
     *
     * @param errorMessage The error message to display for invalid input.
     * @return A valid positive integer input provided by the user.
     */
    private static int getPositiveIntInput(String errorMessage) {
        int input;
        do {
            try {
                input = Integer.parseInt(sc.nextLine());
                if (input <= 0) {
                    System.out.println(errorMessage);
                }
            } catch (NumberFormatException e) {
                System.out.println(errorMessage);
                input = -1;
            }
        } while (input <= 0);
        return input;
    }

    /**
     * Creates the "sales" table if it does not exist.
     */
    public static void creatTable() {
        String sql = """
                 CREATE TABLE IF NOT EXISTS sales(
                    saleId SERIAL PRIMARY KEY ,
                    bookId INTEGER,
                    customersId INTEGER,
                    dataOfSale  DATE,
                    quantitySold INTEGER NOT NULL CHECK ( quantitySold >= 0 ),
                    totalPrice REAL NOT NULL  CHECK ( totalPrice >= 0 ),
                    CONSTRAINT fkBook FOREIGN KEY (bookId) REFERENCES books(bookId) ON DELETE SET NULL ,
                    CONSTRAINT fkCustomer FOREIGN KEY (customersId) REFERENCES customers(customerId) ON DELETE SET NULL 
                    );
                """;
        try(Statement statement = connection.createStatement()) {
            statement.execute(sql);
            System.out.println("successful");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Inserts a sales record into the "sales" table after validating the input.
     *
     * @param sales The Sales object to be inserted.
     */
    public static void insertSales(Sales sales) {
        if (sales.getBookId() <= 0 || sales.getCustomersId() <= 0) {
            System.out.println("Invalid bookId or customerId. Please provide valid values.");
            return;
        }

        if (!BookServices.isBookIdValid(sales.getBookId()) ) {
            System.out.println("Invalid bookId. Please make sure the bookId exists in the books table.");
            return;
        } else if (!CustomerServices.isCustomerIdValid(sales.getCustomersId())) {
            System.out.println("Invalid customerId. Please make sure the customerId exists in the customers table.");
            return;
        }


        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO sales(bookId,customersId,dataOfSale,quantitySold,totalPrice) VALUES (?,?,NOW(),?,?)")) {
            preparedStatement.setInt(1, sales.getBookId());
            preparedStatement.setInt(2, sales.getCustomersId());
            preparedStatement.setInt(3, sales.getQuantitySold());
            preparedStatement.setDouble(4, sales.getTotalPrice());
            int count = preparedStatement.executeUpdate();
            if (count > 0) {
                System.out.println("Update successfully");
                updateStock(sales.getBookId(), sales.getQuantitySold());
            } else {
                System.out.println("No rows affected. Check bookId and customerId values.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the stock quantity of a book after a successful sales record insertion.
     *
     * @param bookId       The ID of the book.
     * @param quantitySold The quantity sold.
     */
    public static void updateStock(int bookId, int quantitySold)  {
        String query = "UPDATE books SET quantityInStock = quantityInStock - ? WHERE bookId=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, quantitySold);
            preparedStatement.setInt(2, bookId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Calculates and prints the total revenue by genre from sales records.
     */
    public static void calculateTotalRevenueByGenre() {
        try {
            String sql = "SELECT b.genre, SUM(b.price * s.totalPrice) AS total_revenue " +
                           "FROM sales s " +
                           "JOIN books b ON s.bookId = b.bookId " +
                           "GROUP BY b.genre";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    System.out.println("Genre: " + resultSet.getString("genre") +
                                       ", Total Revenue: $" + resultSet.getDouble("total_revenue"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the "sales" table from the database.
     */
    public static void deleteSalesAll() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE sales");
            System.out.println("successful");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if the "sales" table exists and prompts the user to create it if not.
     *
     * @return true if the "sales" table does not exist; otherwise, false.
     */
    public static boolean checkSales() {
        String sql = "SELECT * FROM sales";
        try (Statement statement = connection.createStatement()){
            statement.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("sales no exist pleas creat sales");
            return true;
        }
        return false;
    }
}
