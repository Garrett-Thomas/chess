package dao;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dataaccess.DataAccessException;

public class MemoryAuthDAO implements AuthDAO {

    private static MemoryAuthDAO memDAO = null;

    // Map of auth token to username
    private final Map<String, String> authUsers;

    public MemoryAuthDAO() {
        this.authUsers = new HashMap<>();
    }


    public boolean validateAuth(String token){
        return this.authUsers.get(token) != null;
    }

    public String getUsername(String token){
        return this.authUsers.get(token);
    }

    public String createAuth(String username) throws DataAccessException {
        var token = UUID.randomUUID().toString();
        this.authUsers.put(token, username);

        return token;
    }

    @Override
    public void deleteAuthToken(String token) throws DataAccessException {

        if (this.authUsers.get(token) == null) {
            throw new DataAccessException("Auth token not found");
        }

        this.authUsers.remove(token);


    }

    public void clear(){
        memDAO = new MemoryAuthDAO();
    }

    public static synchronized MemoryAuthDAO getInstance() {
        if (memDAO == null) {
            memDAO = new MemoryAuthDAO();
        }

        return memDAO;
    }
}
