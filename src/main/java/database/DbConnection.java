package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    private final Connection connection;
    private static DbConnection instance;

    private DbConnection() throws SQLException {
        connection = DriverManager.
                getConnection("jdbc:mysql://localhost:3306/medicare_pos", "root", "Geeth@200104");
    }

    public Connection getConnection() {
        return connection;
    }

    public static DbConnection getInstance() throws SQLException {
        return instance == null ? instance = new DbConnection() : instance;
    }
}
