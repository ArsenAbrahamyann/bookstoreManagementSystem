package bookstoreManagementSystem;

import java.sql.*;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    /**
     * Private constructor to create a DatabaseConnection instance.
     * Initializes the database connection using JDBC for PostgreSQL.
     */
    private DatabaseConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/postgres";
            String userName = "postgres";
            String password = "postgres";
            this.connection = DriverManager.getConnection(url, userName, password);
            System.out.println("Connected successfully");
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Database Connection Creation Failed : " + e.getMessage());
        }
    }



    /**
     * Retrieves the JDBC Connection object representing the database connection.
     *
     * @return The JDBC Connection object.
     */
    public Connection getConnection() {
        return connection;
    }


    /**
     * Gets the singleton instance of the DatabaseConnection class.
     * If an instance does not exist, it creates a new one. If an existing instance is closed,
     * it creates and returns a new instance.
     *
     * @return The singleton instance of DatabaseConnection.
     */
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else {
            try {
                if (instance.getConnection().isClosed()) {
                    instance = new DatabaseConnection();
                }
            } catch (SQLException e) {
                throw new RuntimeException();
            }
        }
        return instance;
    }


}
