package server;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

import java.util.Map;

public interface AuthDAO {



    boolean validateAuth(String token);

    public String createAuth(String username) throws DataAccessException;

    void deleteAuthToken(String token) throws DataAccessException;

}
