package rh.dao;

import rh.exceptions.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DaoLayer {
    private Connection connection;

    public DaoLayer() throws DAOException {
        this.connection = DBConnection.getConnection();
    }

    public ResultSet getResultSet(String query, List<String> values) throws DAOException {
        validate(query, values);

        try {
            PreparedStatement statement = prepareStatement(query, values);
            return statement.executeQuery();

        } catch (SQLException e) {
            throw new DAOException("Something failed.");
        }
    }

    public void executeCommand(String query, List<String> values) throws DAOException {
        validate(query, values);

        try {
            PreparedStatement statement = prepareStatement(query, values);
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Something failed.");
        }
    }

    private PreparedStatement prepareStatement(String query, List<String> values) {
        try {
            PreparedStatement stmt = connection.prepareStatement(query);

            for (int i = 0; i < values.size(); i++) {
                int index = i + 1;

                stmt.setString(index, values.get(i));
            }
            return stmt;

        } catch (SQLException e) {
            throw new IllegalStateException("Invalid sql query.");
        }
    }

    private void validate(final String query, final List<String> values) throws DAOException {
        DaoLayerValidator daoValidator = new DaoLayerValidator(query, values);

        if (daoValidator.isAnyInputNull())
            throw new DAOException("At least one value is null.");

        if (!daoValidator.isEqualAmountOfInjectionsAndValues())
            throw new DAOException("Not equal amount of injections and values delivered");

    }
}
