// Otoniel Rodriguez-Perez
// CEN-3024C-24204
// 04/03/2025

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnector class:
 * Handles both authentication and establishing a database connection.
 * <p>
 * This class stores database credentials and provides a method to
 * create and return a new connection using those credentials.
 * </p>
 *
 * Usage:
 * <ul>
 *   <li>Call {@link #setCredentials(String, String, String, String)} once after the user enters their details.</li>
 *   <li>Call {@link #getConnection()} whenever a connection is needed.</li>
 * </ul>
 */

public class DatabaseConnector {
    private static String server;
    private static String database;
    private static String dbUser;
    private static String dbPass;

    /**
     * Stores the database credentials for use in {@link #getConnection()}.
     * Call this once after the user enters their details.
     *
     * @param server   the database server address.
     * @param database the name of the database.
     * @param dbUser   the database username.
     * @param dbPass   the database password.
     */
    public static void setCredentials(String server, String database, String dbUser, String dbPass) {
        DatabaseConnector.server = server;
        DatabaseConnector.database = database;
        DatabaseConnector.dbUser = dbUser;
        DatabaseConnector.dbPass = dbPass;
    }

    /**
     * Creates and returns a new {@link Connection} object using the stored credentials.
     * <p>
     * This method is called every time you need to interact with the database.
     * </p>
     *
     * @return a new database connection.
     * @throws SQLException if the credentials have not been set or the connection fails.
     */
    public static Connection getConnection() throws SQLException {
        if (server == null || database == null || dbUser == null || dbPass == null) {
            throw new SQLException("Database credentials have not been set.");
        }
        // Build the JDBC URL.
        String url = "jdbc:mysql://" + server + "/" + database;
        return DriverManager.getConnection(url, dbUser, dbPass);
    }
}









