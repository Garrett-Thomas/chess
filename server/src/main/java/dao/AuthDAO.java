package dao;

import dataaccessclasses.ServiceException;

public interface AuthDAO {


    boolean validateAuth(String token);

    String getUsername(String token);

    public String createAuth(String username) throws Exception;

    void deleteAuthToken(String token) throws ServiceException;

    void clear();
}
