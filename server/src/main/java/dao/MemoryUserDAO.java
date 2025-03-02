package dao;

import dataaccess.DataAccessException;
import dataaccess.ServiceException;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {

    private static Map<String, UserData> userData = new HashMap<>();
    private static MemoryUserDAO userDAO = null;

    public MemoryUserDAO() {

    }

    @Override
    public UserData getUser(String username) {

        return userData.get(username);
    }


    @Override
    public void addUser(UserData clientData) throws ServiceException {

        if (userData.get(clientData.username()) != null) {
            throw new ServiceException(403, "Username already taken");
        }

        userData.put(clientData.username(), clientData);
    }

    public void clear() {
        userDAO = new MemoryUserDAO();
        userData = new HashMap<>();
    }

    public static synchronized MemoryUserDAO getInstance() {
        if (userDAO == null) {
            userDAO = new MemoryUserDAO();
        }

        return userDAO;
    }
}
