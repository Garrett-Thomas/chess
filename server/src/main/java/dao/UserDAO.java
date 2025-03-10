package dao;

import dataaccessclasses.ServiceException;
import model.UserData;

public interface UserDAO {


    public UserData getUser(String username);

    public void addUser(UserData userData) throws ServiceException;

    void clear();

}
