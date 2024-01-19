package bookstoreManagementSystem;

import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class CustomerServices {
    private static final DatabaseConnection databaseConnection = DatabaseConnection.getInstance();
    private static final Connection connection = databaseConnection.getConnection();
    private static Scanner sc = new Scanner(System.in);

    /**
     * Starts the customer management system, providing a menu for various actions.
     */
    public static void customerStart() {
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
                    creatTable();
                    break;
                case 2:
                    if (checkTableCustomers()) {
                        break;
                    }
                    insertCustomer(exemplary());
                    break;
                case 3:
                    if (checkTableCustomers()) {
                        break;
                    }
                    updateExemplary();
                    break;
                case 4:
                    if (checkTableCustomers() || SalesServices.checkSales()) {
                        break;
                    }
                    viewPurchaseHistory();
                    break;
                case 5:
                    if (checkTableCustomers()) {
                       break;
                    }
                    deleteCustomersAll();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");

            }
        }

    }

    /**
     * Prints the menu for customer management actions.
     */
    public static void printManu(){
        System.out.println("\nCustomers Management System ");
        System.out.println("1. creat customers");
        System.out.println("2. insert customer");
        System.out.println("3. Update customer by information");
        System.out.println("4. view purchase history");
        System.out.println("5. delete All Table customers");
        System.out.println("6. Back to Main Menu");
        System.out.print("Enter your choice: ");
    }


    /**
     * Generates an exemplary Customer object based on user input. It prompts the user to enter a name, email, and phone
     * number, performs validations, and returns a Customer object with the provided information.
     *
     * @return An exemplary Customer object with validated user-provided information.
     */
    public static Customer exemplary() {
        System.out.println("Please enter name:");
        String name = getNonEmptyInput("Name cannot be empty.");
        System.out.println("Please enter email:");
        String email = getEmailInput("Invalid email format. Please enter a valid email.");
        System.out.println("Please enter phone:");
        String phone = ""+getPhoneInput("Invalid phone format. Please enter a valid phone number.(000-00-00-00)");

        Customer customer = new Customer(name,email,phone);


        return customer;
    }

    /**
     * Reads and validates a non-negative integer input from the user through the console. It displays an error message and
     * prompts for re-entry until a valid non-negative integer is provided.
     *
     * @param errorMessage The error message to display for invalid input.
     * @return A valid non-negative integer input provided by the user.
     */
    private static int getNonNegativeIntInput(String errorMessage) {
        int input;
        do {
            try {
                input = Integer.parseInt(sc.nextLine());
                if (input < 0) {
                    System.out.println(errorMessage);
                }
            } catch (NumberFormatException e) {
                System.out.println(errorMessage);
                input = -1;
            }
        } while (input < 0);
        return input;
    }

    /**
     * Reads and validates a non-empty string input from the user through the console. It displays an error message and
     * prompts for re-entry until a valid non-empty string is provided.
     *
     * @param errorMessage The error message to display for invalid input.
     * @return A valid non-empty string input provided by the user.
     */
    private static String getNonEmptyInput(String errorMessage) {
        String input;
        do {
            input = sc.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println(errorMessage);
            }
        } while (input.isEmpty());
        return input;
    }


    /**
     * Reads and validates an email address input from the user through the console. It displays an error message and
     * prompts for re-entry until a valid email address is provided.
     *
     * @param errorMessage The error message to display for invalid input.
     * @return A valid email address input provided by the user.
     */
    private static String getEmailInput(String errorMessage) {
        String email;
        do {
            email = sc.nextLine().trim();
            if (!isValidEmail(email)) {
                System.out.println(errorMessage);
            }
        } while (!isValidEmail(email));
        return email;
    }

    /**
     * Validates an email address using a regular expression.
     *
     * @param email The email address to validate.
     * @return {@code true} if the email address is valid, {@code false} otherwise.
     */
    private static boolean isValidEmail(String email) {
        // Basic email validation using a regular expression
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return Pattern.matches(emailRegex, email);
    }

    /**
     * Creates the "customers" table in the database if it does not exist.
     */
    public static void creatTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS customers(
                    customerId SERIAL PRIMARY KEY ,
                    name VARCHAR(128) NOT NULL ,
                    email VARCHAR(128) NOT NULL ,
                    phone VARCHAR(15) NOT NULL 
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
     * Inserts a new customer into the "customers" table.
     *
     * @param customer The customer object to be inserted.
     */
    public static void insertCustomer(Customer customer) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO customers(name,email,phone)VALUES (?,?,?)"
        )) {
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getEmail());
            preparedStatement.setString(3, customer.getPhone());
            preparedStatement.executeUpdate();
            System.out.println("successful");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates customer information in the "customers" table.
     *
     * @param newName   The new name of the customer.
     * @param newEmail  The new email of the customer.
     * @param newPhone  The new phone number of the customer.
     * @param name      The current name of the customer to be updated.
     */
    public static void UpdateCustomer(String newName,String newEmail,String newPhone,String name) {
        String sql = "UPDATE customers SET name=?,email=?,phone=? WHERE name=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1,newName);
            preparedStatement.setString(2,newEmail);
            preparedStatement.setString(3,newPhone);
            preparedStatement.setString(4,name);
            int i = preparedStatement.executeUpdate();
            if (i > 0) {
                System.out.println("Update successful");
            }else {
                System.out.println("no such name found");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Updates customer information interactively using an exemplary customer as a template.
     */
    public static void updateExemplary() {
        String sql = "SELECT * FROM customers";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println("name= " + resultSet.getString("name"));
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("pleas select where consumer update");
        String name = sc.nextLine();
        System.out.println("Please enter name:");
        String newName = getNonEmptyInput("Name cannot be empty.");
        System.out.println("Please enter email:");
        String newEmail = getEmailInput("Invalid email format. Please enter a valid email.");
        System.out.println("Please enter phone:");
        String newPhone = ""+getPhoneInput("Invalid phone format. Please enter a valid phone number.(000-00-00-00)");
        UpdateCustomer(newName,newEmail,newPhone,name);
    }
    private static String getPhoneInput(String errorMessage) {
        String phone;
        do {
            phone = sc.nextLine().trim();
            if (!isValidPhone(phone)) {
                System.out.println(errorMessage);
            }
        } while (!isValidPhone(phone));
        return phone;
    }
    private static boolean isValidPhone(String phone) {
        // Updated regular expression to accept numbers with optional dashes
        return phone.matches("^(\\d{10}|\\d{3}[-\\s]?\\d{2}[-\\s]?\\d{2}[-\\s]?\\d{2})$");
    }

    /**
     *
     *Views a customer’s purchase history.
     */
    public static void viewPurchaseHistory() {
        printAllCustomers();
        System.out.println("pleas enter customer customersId");
        int customersId = getNonNegativeIntInput("Invalid input. Please enter a valid quantity in stock.");
        String sql = "SELECT * FROM sales WHERE customersId=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setInt(1,customersId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println("name=" + resultSet.getString("name") + ", " +
                                   "email=" + resultSet.getString("email") + ", " +
                                   "phone=" + resultSet.getString("phone"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Deletes all records from the "customers" table.
     */
    public static void deleteCustomersAll() {
        checkTableCustomers();
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE customers");
            System.out.println("successful");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if the "customers" table exists.
     *
     * @return True if the "customers" table exists, false otherwise.
     */
    public static boolean checkTableCustomers() {
        String sql = "SELECT * FROM customers";
        try (Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("customers does not exist pleas creat table");
            return true;
        }
        return false;
    }

    /**
     * Prints all customers in the "customers" table.
     */
    public static void printAllCustomers() {
        String sql = "SELECT * FROM customers";
        try (Statement statement = connection.createStatement()){
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                System.out.println("customerId= " + resultSet.getInt("customerId") + ", "  +
                                   "name= " + resultSet.getString("name"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Checks if a given customer ID is valid by querying the "customers" table.
     *
     * @param customerId The customer ID to be validated.
     * @return True if the customer ID is valid, false otherwise.
     */
    static boolean isCustomerIdValid(int customerId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1 FROM customers WHERE customerId = ?")) {
            preparedStatement.setInt(1, customerId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
