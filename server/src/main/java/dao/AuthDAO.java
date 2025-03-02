package dao;

import dataaccess.DataAccessException;

public interface AuthDAO {



    boolean validateAuth(String token);

    String getUsername(String token);
    public String createAuth(String username) throws DataAccessException;

    void deleteAuthToken(String token) throws DataAccessException;
    void clear();
}
