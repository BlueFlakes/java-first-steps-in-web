package rh.dao;

import rh.exceptions.DAOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String dbUrl = "jdbc:sqlite:data/database.db";
    private static Connection connection;

    public static Connection getConnection() throws DAOException {
        if (connection == null) {
            synchronized (DBConnection.class) {
                if (connection == null) {
                    connectWithDatabase();
                }
            }
        }

        return connection;
    }

    private static void connectWithDatabase() throws DAOException {
        try {
            connection = DriverManager.getConnection(dbUrl);
        } catch (SQLException e) {
            throw new DAOException("Could not connect with database.");
        }
    }
}
