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
    SQLAuthDAO() {
    }

    @Override
    public boolean validateAuth(String token) {
        return false;
    }

    @Override
    public String getUsername(String token) {

        try(var conn = DatabaseManager.getConnection()){

            var res = DbUtils.executeQuery(getUserString, token);

            return res.getString("username");
        }
        catch(Exception e){
            System.err.println(e.toString());
        }

        return "";
    }

    @Override
    public String createAuth(String username) throws Exception {

        var token = UUID.randomUUID().toString();
        try (var conn = DatabaseManager.getConnection()) {

            DbUtils.executeUpdate(createAuthString, username, token);

        }
        catch (DataAccessException e) {
            throw new ServiceException(500, "Server error");
        }

        return token;
    }

    @Override
    public void deleteAuthToken(String token) throws ServiceException {

    }

    @Override
    public void clear() {

    }
}
