package server;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

public class MemoryAuthDAO implements AuthDAO {

    private final Map<String, UserData> userData;


    // Map of auth token to username
    private final Map<String, String> authUsers;

    public MemoryAuthDAO() {
        this.userData = new HashMap<>();
        this.authUsers = new HashMap<>();
    }

    public void addUser(UserData userData) throws DataAccessException {
        if (this.userData.get(userData.username()) != null) {
            throw new DataAccessException("Username taken");
        }
        this.userData.put(userData.username(), userData);
    }


    public UserData getUser(String username) throws DataAccessException {

        var user = this.userData.get(username);
        if (user == null) {
            throw new DataAccessException("User does not exist");
        }
        return user;
    }

    public String createAuth(String username) throws DataAccessException {
        var token = UUID.randomUUID().toString();
        var userData = this.userData.get(username);

        if (userData == null) {
            throw new DataAccessException("Cannot authenticate a user that doesn't exist");
        }
        this.authUsers.put(token, username);

        return token;
    }

    @Override
    public void deleteAuthToken(String token) throws DataAccessException{

        if(this.authUsers.get(token) == null) throw new DataAccessException("Auth token not found");

        this.authUsers.remove(token);


    }
}
