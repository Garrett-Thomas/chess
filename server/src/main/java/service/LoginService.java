package service;

import dao.AuthDAO;
import dao.MemoryAuthDAO;
import dao.MemoryUserDAO;
import dao.UserDAO;
import dataaccess.DataAccessException;
import model.UserData;
import server.*;

import java.util.Objects;

public class LoginService {

    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public LoginService() {
        this.authDAO = MemoryAuthDAO.getInstance();
        this.userDAO = MemoryUserDAO.getInstance();
    }

    public void logout(LogoutRequest logoutReq) throws DataAccessException {
        this.authDAO.deleteAuthToken(logoutReq.authToken());

    }

    public LoginResponse register(UserData req) throws DataAccessException {

        var username = req.username();
        if (username.isEmpty() || req.password().isEmpty() || req.email().isEmpty()) {

            throw new DataAccessException("Field(s) cannot be empty");
        }

        if (this.userDAO.getUser(username) != null) {
            throw new DataAccessException("User already exists");
        }

        this.userDAO.addUser(req);

        var token = this.authDAO.createAuth(username);


        return new LoginResponse(token, username);

    }

    public LoginResponse login(LoginRequest res) throws DataAccessException {

        var userData = this.userDAO.getUser(res.getUsername());
        if(userData == null){
            throw new DataAccessException("User does not exist");
        }
        if (!Objects.equals(userData.password(), res.getPassword())) {
            throw new DataAccessException("Invalid username");
        }

        var token = this.authDAO.createAuth(userData.username());

        return new LoginResponse(token, userData.username());
    }


}
