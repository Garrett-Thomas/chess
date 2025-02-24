package server;
import dataaccess.DataAccessException;
import model.AuthData;
import model.UserData;

public interface AuthDAO {

    public UserData getUser(String username) throws DataAccessException;
    public void addUser(UserData userData) throws DataAccessException;
    public String createAuth(String username) throws DataAccessException;
    void deleteAuthToken(String token) throws DataAccessException;




}
