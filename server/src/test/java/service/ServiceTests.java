package service;

import dataaccess.DbUtils;
import dataaccess.ServiceException;
import model.GamesRequest;
import model.LogoutRequest;
import model.RegisterRequest;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class ServiceTests {
    private static AuthService authService = new AuthService();
    private static GameService gameService = new GameService();
    private static final GamesRequest GAME_REQ = new GamesRequest("hello", null, "BLACK");

    @BeforeAll
    public static void init() {
        UserData req = new UserData("username", "pass", "user@gmail.com");
        try {
            DbUtils.clearDB();
            authService.register(req);
            authService.register(new UserData("one", "two", "three"));
        } catch (Exception e) {
            System.exit(1);
        }
    }

    @Test
    public void testLoginPositive() throws Exception {
        try {
            authService.login(new RegisterRequest.LoginRequest("username", "pass"));

        } catch (ServiceException e) {
            assert (false);
        }

    }

    @Test
    public void testLoginNegative() {
        RegisterRequest.LoginRequest logReq = new RegisterRequest.LoginRequest("gary", "pass");

        try {
            authService.login(logReq);
            assert (false);
        } catch (Exception ignored) {
        }

    }

    @Test
    public void testLogoutPositive() throws Exception {

        try {

            var res = authService.login(new RegisterRequest.LoginRequest("one", "two"));
            authService.logout(new LogoutRequest(res.authToken()));
        } catch (ServiceException e) {
            assert (false);
        }


    }

    @Test
    public void testLogoutNegative() {
        try {
            authService.logout(new LogoutRequest(""));
            assert (false);
        } catch (ServiceException e) {
            assert (true);
        }
    }

    @Test
    public void testRegisterNegative() {
        try {
            authService.register(null);
        } catch (Exception e) {
            assert (true);
        }


    }

    @Test
    public void testRegisterPositive() {
        UserData req = new UserData("garth", "pass", "user@gmail.com");
        try {
            authService.register(req);

        } catch (Exception e) {
            assert (false);
        }

    }


    @Test
    public void testCreateGamePositive() {
        var res = gameService.createGame(new GamesRequest("game", 123, "BLACK"));
        assert (res.gameID() > 0);
    }

    @Test
    public void testCreateGameNegative() {
        try {
            gameService.createGame(null);
            assert (false);
        } catch (Exception e) {
            assert (true);
        }
    }

    @Test
    public void testListGamesPositive() {
        gameService.listGames();
        assert (true);
    }

    @Test
    public void testListGamesNegative() {
        try {

            gameService.listGames();
        } catch (Exception e) {
            assert (false);
        }
    }

    @Test
    public void testJoinGamePositive() {


        var gameRes = gameService.createGame(GAME_REQ);

        try {

            gameService.joinGame(new GamesRequest("hello", gameRes.gameID(), "WHITE"), "garth");
            assert (true);
        } catch (Exception e) {
            assert (false);
        }

    }

    @Test
    public void testJoinGameNegative() {
        try {

            gameService.joinGame(new GamesRequest("hello", 123, "hello"), "player");
            assert (false);
        } catch (Exception ignored) {
            assert (true);
        }
    }
}
