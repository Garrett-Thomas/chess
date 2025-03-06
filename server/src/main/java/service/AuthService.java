package service;

import dao.AuthDAO;
import dao.MemoryAuthDAO;
import dao.MemoryUserDAO;
import dao.UserDAO;
import dataaccess.ServiceException;
import model.LoginResponse;
import model.LogoutRequest;
import model.RegisterRequest;
import model.UserData;

import java.util.Objects;

public class AuthService {

    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public AuthService() {
        this.authDAO = MemoryAuthDAO.getInstance();
        this.userDAO = MemoryUserDAO.getInstance();
    }

    public void logout(LogoutRequest logoutReq) throws ServiceException {
        this.authDAO.deleteAuthToken(logoutReq.authToken());

    }

    public LoginResponse register(UserData req) throws Exception {

        var username = req.username();
        if (username == null || req.password() == null || req.email() == null) {

            throw new ServiceException(400, "Field(s) cannot be empty");
        }

        if (this.userDAO.getUser(username) != null) {
            throw new ServiceException(403, "User already exists");
        }

        this.userDAO.addUser(req);

        var token = this.authDAO.createAuth(username);


        return new LoginResponse(token, username);

    }

    public LoginResponse login(RegisterRequest.LoginRequest res) throws Exception {

        var userData = this.userDAO.getUser(res.username());
        if(userData == null){
            throw new ServiceException(401, "User does not exist");
        }
        if (!Objects.equals(userData.password(), res.password())) {
            throw new ServiceException(401, "Invalid password");
        }

        var token = this.authDAO.createAuth(userData.username());

        return new LoginResponse(token, userData.username());
    }


}
