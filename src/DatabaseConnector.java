// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 03/30/2025

// DatabaseConnector class:
// This class handles both authentication and establishing a database connection.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static String server;
    private static String database;
    private static String dbUser;
    private static String dbPass;


     //Stores the database credentials for use in getConnection().
     //Call this once after the user enters their details.
    public static void setCredentials(String server, String database, String dbUser, String dbPass) {
        DatabaseConnector.server = server;
        DatabaseConnector.database = database;
        DatabaseConnector.dbUser = dbUser;
        DatabaseConnector.dbPass = dbPass;
    }

     // Creates and returns a new Connection object using the stored credentials.
     // This method is called every time you need to interact with the database.
    public static Connection getConnection() throws SQLException {
        if (server == null || database == null || dbUser == null || dbPass == null) {
            throw new SQLException("Database credentials have not been set.");
        }
        // Build the JDBC URL.
        String url = "jdbc:mysql://" + server + "/" + database;
        return DriverManager.getConnection(url, dbUser, dbPass);
    }
}
