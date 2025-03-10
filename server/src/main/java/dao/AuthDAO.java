package dao;

import dataaccess.DataAccessException;
import dataaccess.ServiceException;

import java.sql.SQLException;

public interface AuthDAO {


    boolean validateAuth(String token);

    String getUsername(String token);

    public String createAuth(String username) throws Exception;

    void deleteAuthToken(String token) throws ServiceException;

    void clear();
}
