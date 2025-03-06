package dao;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.DbUtils;
import dataaccess.ServiceException;

import java.sql.SQLException;
import java.util.UUID;

public class SQLAuthDAO implements AuthDAO {

    private static final String createAuthString =
            """
                    INSERT INTO auth (username, token) VALUES (?, ?)
                    """;

    private static final String getUserString =
            """
                    SELECT username FROM auth WHERE token = ? 
                    """;
    private static final String deleteTokenString =
            """
                    SET token = NULL WHERE token = ?
                    """;

    private static final String deleteTableString =
            """
                    TRUNCATE TABLE auth 
                    """;

    SQLAuthDAO() {
    }

    @Override
    public boolean validateAuth(String token) {

        try {
            var res = DbUtils.executeQuery(getUserString, token);
            return !res.getString("username").isEmpty();

        } catch (Exception e) {
            System.err.println(e.toString());
            return false;
        }

    }

    @Override
    public String getUsername(String token) {

        try {
            var res = DbUtils.executeQuery(getUserString, token);
            return res.getString("username");

        } catch (Exception e) {
            System.err.println(e.toString());
            return null;
        }

    }

    @Override
    public String createAuth(String username) throws ServiceException {

        var token = UUID.randomUUID().toString();
        DbUtils.executeUpdate(createAuthString, username, token);
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
