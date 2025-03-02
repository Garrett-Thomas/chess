package server;

import dataaccess.DataAccessException;
import dataaccess.ServiceException;
import model.*;
import service.AuthService;
import spark.*;
import com.google.gson.Gson;


public class AuthHandler {

    private final AuthService authService;

    AuthHandler() {
        this.authService = new AuthService();
    }


    public Object deleteToken(Request req, spark.Response res) throws ServiceException, DataAccessException {
        ServerUtils.authUser(req, res);

        var delReq = new LogoutRequest(req.headers("Authorization"));

        this.authService.logout(delReq);

        return new Gson().toJson(null);


    }

    public Object postRegister(Request req, spark.Response res) throws ServiceException {

        var registerReq = new Gson().fromJson(req.body(), UserData.class);
        var regResult = this.authService.register(registerReq);
        return new Gson().toJson(regResult);

    }

    public Object postLogin(Request req, spark.Response res) throws ServiceException {

        var loginRequest = new Gson().fromJson(req.body(), RegisterRequest.LoginRequest.class);

        LoginResponse loginRes = this.authService.login(loginRequest);

        return new Gson().toJson(loginRes);


    }

}
