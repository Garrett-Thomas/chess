package dao;

import dataaccess.DbUtils;
import dataaccess.ServiceException;
import model.UserData;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

    private static final String GET_USER_STRING = """
            SELECT * FROM user WHERE username = ?
            """;
    private static final String ADD_USER_STRING = """
            INSERT INTO user (username, password, email) VALUES (?, ?, ?)
            """;

    private static final String CLEAR_TABLE_STRING = """
            TRUNCATE TABLE user
            """;

    private static SQLUserDAO sqlUserDAO = null;


    public static SQLUserDAO getInstance() {
        if (sqlUserDAO == null) {
            sqlUserDAO = new SQLUserDAO();
        }
        return sqlUserDAO;
    }

    public SQLUserDAO() {
    }


    @Override
    public UserData getUser(String username) {


        try {
            var res = DbUtils.executeQuery(GET_USER_STRING, username);

            if(res.next()){
                return new UserData(username, res.getString("password"), res.getString("email"));
            }
            throw new ServiceException(500, "could not get user");

        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public void addUser(UserData userData) throws ServiceException {
        var username = userData.username();
        var password = DbUtils.hashPassword(userData.password());
        var email = userData.email();
        DbUtils.executeUpdate(ADD_USER_STRING, username, password, email);
    }

    @Override
    public void clear() {
        try {
            DbUtils.executeUpdate(CLEAR_TABLE_STRING);

        } catch (ServiceException e) {
            System.err.println("ERROR: " + e.getMessage());

        }

    }
}
