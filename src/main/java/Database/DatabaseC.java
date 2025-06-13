package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton class for database connection to Neon PostgreSQL.
 * Replace DB_URL, USER, PASSWORD with your actual Neon credentials.
 */
public class DatabaseC {
    private static final Logger LOGGER = Logger.getLogger(Database.DatabaseC.class.getName());

    private static final String DB_URL = "jdbc:postgresql://ep-fancy-night-a10epazf-pooler.ap-southeast-1.aws.neon.tech/neondb?sslmode=require";
    private static final String USER = "neondb_owner";
    private static final String PASSWORD = "npg_LVRP5o6dmZUK";

    private static Connection connection;

    private DatabaseC() {
        // private constructor to prevent instantiation
    }

    /**
     * Get singleton connection.
     * @return Connection object
     * @throws SQLException If connection fails
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("org.postgresql.Driver"); // Ensure driver is loaded
                connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                LOGGER.log(Level.SEVERE, "PostgreSQL JDBC Driver not found!", e);
                throw new SQLException(e);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Failed to establish database connection.", e);
                throw e;
            }
        }
        return connection;
    }
}
