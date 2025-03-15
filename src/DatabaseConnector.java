import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static Connection connection = null;

    // Connects to the MySQL database using user-provided details.
    public static void connect(String server, String database, String dbUser, String dbPassword) throws SQLException {
        // Build the connection URL (adjust parameters as needed)
        String connectionUrl = "jdbc:mysql://" + server + "/" + database;
        connection = DriverManager.getConnection(connectionUrl, dbUser, dbPassword);
    }

    // Returns the active connection.
    public static Connection getConnection() {
        return connection;
    }
}
