package service;

import dataaccess.DataAccessException;
import model.UserData;
import server.*;

import java.util.Objects;

public class LoginService {

    private AuthDAO authDAO;

    public LoginService() {
        this.authDAO = new MemoryAuthDAO();
    }

    public LoginResponse register(UserData req) throws DataAccessException {

        var username = req.username();
        if (username.isEmpty() || req.password().isEmpty() || req.email().isEmpty()) {

            throw new DataAccessException("Field(s) cannot be empty");
        }

        if (this.authDAO.getUser(username) != null) {
            throw new DataAccessException("User already exists");
        }

        this.authDAO.addUser(req);

        var token = this.authDAO.createAuth(username);


        return new LoginResponse(token, username);

    }

    public LoginResponse login(LoginRequest res) throws DataAccessException {

        var userData = this.authDAO.getUser(res.getUsername());
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
