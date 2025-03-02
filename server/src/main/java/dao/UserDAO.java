package dao;

import dataaccess.DataAccessException;
import dataaccess.ServiceException;
import model.UserData;

public interface UserDAO {


    public UserData getUser(String username);

    public void addUser(UserData userData) throws ServiceException;

    void clear();

}
