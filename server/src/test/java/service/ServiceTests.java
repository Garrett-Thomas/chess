package service;

import dataaccess.ServiceException;
import model.GamesRequest;
import model.GamesResult;
import model.RegisterRequest;
import model.UserData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class ServiceTests {
    private AuthService authService = new AuthService();
    private GameService gameService = new GameService();


    @Test
    public void testAuthServicePositive(){
        UserData req = new UserData("username", "pass", "user@gmail.com");
        try{
            authService.register(req);

        } catch (ServiceException e) {
            fail();
        }

    }

    @Test
    public void testAuthServiceNegative(){
        RegisterRequest.LoginRequest logReq = new RegisterRequest.LoginRequest("gary", "pass");

        try{
            authService.login(logReq);
            fail();
        } catch (ServiceException ignored) {
        }

    }


    @Test
    public void testGameServicePositive(){
       var res =  gameService.createGame(new GamesRequest("game", 123, "BLACK"));
       assert(res.gameID() > 0);
    }

}
