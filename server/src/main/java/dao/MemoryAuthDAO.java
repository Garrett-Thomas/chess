package dao;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import dataaccess.DataAccessException;
import dataaccess.ServiceException;

public class MemoryAuthDAO implements AuthDAO {

    private static MemoryAuthDAO memDAO = null;

    // Map of auth token to username
    private static Map<String, String> authUsers = new HashMap<>();

    public MemoryAuthDAO() {
    }


    public boolean validateAuth(String token) {
        return authUsers.get(token) != null;
    }

    public String getUsername(String token) {
        return authUsers.get(token);
    }

    public String createAuth(String username) {
        var token = UUID.randomUUID().toString();
        authUsers.put(token, username);

        return token;
    }

    @Override
    public void deleteAuthToken(String token) throws ServiceException {

        if (authUsers.get(token) == null) {
            throw new ServiceException(400, "Auth token not found");
        }

        authUsers.remove(token);


    }

    public void clear() {
        memDAO = new MemoryAuthDAO();
        authUsers = new HashMap<>();
    }

    public static synchronized MemoryAuthDAO getInstance() {
        if (memDAO == null) {
            memDAO = new MemoryAuthDAO();
        }

        return memDAO;
    }
}
