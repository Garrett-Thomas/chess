package server;

import dataaccess.DataAccessException;
import model.UserData;
import service.LoginService;
import spark.*;
import com.google.gson.Gson;


public class AuthHandler {

    private final LoginService loginService;

    AuthHandler() {
        this.loginService = new LoginService();
    }


    public Object deleteToken(Request req, spark.Response res) {
        try {


            var delReq = new LogoutRequest(req.headers("Authorization"));

            this.loginService.logout(delReq);

            return new Gson().toJson("");
        } catch (DataAccessException e) {
            return new Gson().toJson(new ResponseSuper(e.getMessage()));
        }


    }

    public Object postRegister(Request req, spark.Response res) {
        try {
            var registerReq = new Gson().fromJson(req.body(), UserData.class);

            var regResult = this.loginService.register(registerReq);

            return new Gson().toJson(regResult);

        } catch (DataAccessException e) {

            return new Gson().toJson(new ResponseSuper(e.getMessage()));
        }


    }

    public Object postLogin(Request req, spark.Response res) {

        try {

            var loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);

            LoginResponse loginRes = this.loginService.login(loginRequest);

            return new Gson().toJson(loginRes);
        } catch (DataAccessException e) {
            return new Gson().toJson(new ResponseSuper(e.getMessage()));
        }

    }

}
