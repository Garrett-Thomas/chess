package client;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import passoff.model.*;
import server.Server;
import server.ServerFacade;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerFacadeTests {

    private static TestUser existingUser;
    private static TestUser newUser;
    private static TestCreateRequest createRequest;
    private static ServerFacade serverFacade;
    private static Server server;

    private String existingAuth;

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        serverFacade = new ServerFacade("localhost", Integer.toString(port));
        existingUser = new TestUser("john", "pass", "fast@gog.com");
        newUser = new TestUser("jake", "secure", "pass@gog.com");
        createRequest = new TestCreateRequest("dopeGame");
    }

    @BeforeEach
    public void setup() {
        serverFacade.clear();

        //one user already logged in
        TestAuthResult regResult = serverFacade.register(existingUser);
        existingAuth = regResult.getAuthToken();
    }

    @Test
    public void successLogin() {
        TestAuthResult loginResult = serverFacade.login(existingUser);
        Assertions.assertEquals(existingUser.getUsername(), loginResult.getUsername());
    }

    @Test
    public void loginInvalidUser() {
        TestAuthResult loginResult = serverFacade.login(newUser);
        assert (loginResult.getAuthToken() == null);
    }

    @Test
    public void loginWrongPassword() {
        TestUser loginRequest = new TestUser(existingUser.getUsername(), newUser.getPassword());

        TestAuthResult loginResult = serverFacade.login(loginRequest);

        assert (loginResult.getAuthToken() == null);
    }

    @Test
    public void successRegister() {
        //submit register request
        TestAuthResult registerResult = serverFacade.register(newUser);
        assert (!registerResult.getAuthToken().isEmpty());
    }

    @Test
    public void registerTwice() {
        //submit register request trying to register existing user
        TestAuthResult registerResult = serverFacade.register(existingUser);
        assert (registerResult.getAuthToken() == null);
    }

    @Test
    public void failRegister() {
        //attempt to register a user without a password
        TestUser registerRequest = new TestUser(newUser.getUsername(), null, newUser.getEmail());
        TestAuthResult registerResult = serverFacade.register(registerRequest);
        assert (registerResult.getAuthToken() == null);
    }

    @Test
    public void successLogout() {
        //log out existing user
        TestResult result = serverFacade.logout(existingAuth);
        assert(result.getMessage() == null);
    }

    @Test
    public void failLogout() {
        //log out user twice
        //second logout should fail
        serverFacade.logout(existingAuth);
        TestResult result = serverFacade.logout(existingAuth);

        assert (result.getMessage() != null);
    }

    @Test
    public void goodCreate() {
        TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);
        assert (createResult.getGameID() != null);
    }

    @Test
    public void badAuthCreate() {
        //log out user so auth is invalid
        serverFacade.logout(existingAuth);

        TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);
        assert (createResult.getGameID() == null);
    }

    @Test
    public void goodJoin() {
        TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);
        TestJoinRequest joinRequest = new TestJoinRequest(ChessGame.TeamColor.WHITE, createResult.getGameID());
        TestResult joinResult = serverFacade.joinPlayer(joinRequest, existingAuth);

        assert (joinResult.getMessage() == null);
    }

    @Test
    @DisplayName("Join Bad Authentication")
    public void badAuthJoin() {
        //create game
        TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);

        //try join as white
        TestJoinRequest joinRequest = new TestJoinRequest(ChessGame.TeamColor.WHITE, createResult.getGameID());
        TestResult joinResult = serverFacade.joinPlayer(joinRequest, existingAuth + "bad stuff");
        assert (joinResult.getMessage() != null);
    }

    @Test
    public void badColorJoin() {
        TestCreateResult createResult = serverFacade.createGame(createRequest, existingAuth);
        int gameID = createResult.getGameID();
        var joinResult = serverFacade.joinPlayer(new TestJoinRequest("green", gameID), existingAuth);
        assert (joinResult.getMessage() != null);

    }


}
