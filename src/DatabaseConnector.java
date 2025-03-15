// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 03/30/2025

// DatabaseConnector class:
// This class handles both authentication and establishing a database connection.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static Connection connection = null;

    // Connects to the MySQL database using user-provided details.
    public static void connect(String server, String database, String dbUser, String dbPassword) throws SQLException {
        // Concatenated string to ask user for database information.
        String connectionUrl = "jdbc:mysql://" + server + "/" + database;
        connection = DriverManager.getConnection(connectionUrl, dbUser, dbPassword);
    }

    // Returns the active connection.
    public static Connection getConnection() {

        return connection;
    }
}
