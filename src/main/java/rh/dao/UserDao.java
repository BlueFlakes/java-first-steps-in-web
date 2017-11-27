package rh.dao;

import rh.exceptions.DAOException;
import rh.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserDao {
    private static volatile UserDao dao = null;
    private final DaoLayer daoLayer;

    private UserDao() throws DAOException {
        daoLayer = new DaoLayer();
    }

    public static UserDao getInstance() throws DAOException {
        if (dao == null) {
            synchronized (UserDao.class) {
                if (dao == null) {
                    dao = new UserDao();
                }
            }
        }

        return dao;
    }

    public User getUser(Integer id) throws DAOException {
        String query = "SELECT * FROM Users WHERE id = ?";
        List<String> values = Collections.singletonList(id.toString());
        ResultSet rs = this.daoLayer.getResultSet(query, values);

        return createUser(rs);
    }

    public User getUser(String login, String password) throws DAOException {
        String query = "SELECT * FROM Users WHERE login = ? AND password = ?";
        List<String> values = Arrays.asList(login, password);
        ResultSet rs = this.daoLayer.getResultSet(query, values);

        return createUser(rs);
    }

    private User createUser(ResultSet rs) throws DAOException {
        try {
            if (rs.next()) {
                Integer userId = rs.getInt("id");
                String login = rs.getString("login");
                String password = rs.getString("password");
                String name = rs.getString("name");
                String email = rs.getString("email");

                return new User(userId, login, password, name, email);
            }
        } catch (SQLException e) {
            throw new DAOException("DAOException something goes wrong.( Get User By Id )");
        }

        return null;
    }

    public boolean addUser(User user) throws DAOException {
        String command = "INSERT INTO Users VALUES(?, ?, ?, ?, ?);";

        try {
            String id = user.getId().toString();
            String login = user.getLogin();
            String password = user.getPassword();
            String email = user.getEmail();
            String name = user.getName();
            List<String> values = Arrays.asList(id, login, password, email, name);

            this.daoLayer.executeCommand(command, values);
        } catch (DAOException e) {
            return false;
        }

        return true;
    }

    public Integer getNewId() throws DAOException {
        String query = "SELECT max(id) as maxId FROM Users;";

        try {
            ResultSet rs = this.daoLayer.getResultSet(query, new ArrayList<>());
            return rs.getInt("maxId") + 1;

        } catch (SQLException e) {
            throw new IllegalStateException("DAO -> Get new id failed.");
        }
    }
}
