package dao;

import dataaccess.DataAccessException;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {

    private final Map<String, UserData> userData;
    private static MemoryUserDAO userDAO = null;

    public MemoryUserDAO() {
        this.userData = new HashMap<>();

    }

    @Override
    public UserData getUser(String username) {

        return this.userData.get(username);
    }


    @Override
    public void addUser(UserData userData) throws DataAccessException {
        if (this.userData.get(userData.username()) != null) {
            throw new DataAccessException("Username taken");
        }
        this.userData.put(userData.username(), userData);
    }

    public void clear(){
        userDAO = new MemoryUserDAO();
    }
    public static synchronized MemoryUserDAO getInstance() {
        if (userDAO == null) {
            userDAO = new MemoryUserDAO();
        }

        return userDAO;
    }
}
