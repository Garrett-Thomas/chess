package dao;

import dataaccess.DataAccessException;
import dataaccess.ServiceException;

public interface AuthDAO {


    boolean validateAuth(String token);

    String getUsername(String token);

    public String createAuth(String username);

    void deleteAuthToken(String token) throws ServiceException;

    void clear();
}
