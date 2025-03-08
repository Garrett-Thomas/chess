package dao;


import dataaccess.DbUtils;
import dataaccess.ServiceException;

import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {

    private static final String CREATE_AUTH_STRING =
            """
                    INSERT INTO auth (username, token) VALUES (?, ?)
                    """;

    private static final String GET_USER_STRING =
            """
                    SELECT username FROM auth WHERE token = ? 
                    """;
    private static final String deleteTokenString =
            """
                    UPDATE auth SET token = NULL WHERE token = ?
                    """;

    private static final String deleteTableString =
            """
                    TRUNCATE TABLE auth 
                    """;
    private static SQLAuthDAO sqlAuthDAO = null;

    public static SQLAuthDAO getInstance() {
        if (sqlAuthDAO == null) {
            sqlAuthDAO = new SQLAuthDAO();
        }
        return sqlAuthDAO;
    }

    @Override
    public boolean validateAuth(String token) {

        try {
            var res = DbUtils.executeQuery(GET_USER_STRING, token);
            if (res.next()) {

                return !res.getString("username").isEmpty();
            }
            throw new ServiceException(500, "Error validating auth");

        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public String getUsername(String token) {

        try {
            var res = DbUtils.executeQuery(GET_USER_STRING, token);
            if (res.next()) {
                return res.getString("username");
            }
            throw new ServiceException(500, "Couldn't retrieve userdata");

        } catch (Exception e) {
            return null;
        }

    }

    @Override
    public String createAuth(String username) throws ServiceException {

        var token = UUID.randomUUID().toString();
        DbUtils.executeUpdate(CREATE_AUTH_STRING, username, token);
        return token;
    }

    @Override
    public void deleteAuthToken(String token) throws ServiceException {
        DbUtils.executeUpdate(deleteTokenString, token);
    }

    @Override
    public void clear() {
        try {

            DbUtils.executeUpdate(deleteTableString);
        } catch (ServiceException e) {
            System.err.println("Error deleting auth table: " + e.toString());
        }
    }
}
