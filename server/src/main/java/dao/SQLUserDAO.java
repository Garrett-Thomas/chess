package dao;

import dataaccess.DbUtils;
import dataaccess.ServiceException;
import model.UserData;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

    private static final String getUserString = """
            SELECT * FROM user WHERE username = ?
            """;
    private static final String addUserString = """
            INSERT INTO user (username, password, email) VALUES (?, ?, ?)
            """;

    SQLUserDAO() {
    }


    @Override
    public UserData getUser(String username) {


        try {
            var res = DbUtils.executeQuery(getUserString, username);
            return new UserData(username, res.getString("password"), res.getString("email"));

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            return null;
        }

    }

    @Override
    public void addUser(UserData userData) throws ServiceException {
        var username = userData.username();
        var password = userData.password();
        var email = userData.email();
        var res = DbUtils.executeUpdate(addUserString, username, password, email);
    }

    @Override
    public void clear() {

    }
}
