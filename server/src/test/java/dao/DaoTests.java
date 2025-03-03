package dao;

import chess.PieceUtils;
import dataaccess.ServiceException;
import model.UserData;
import org.junit.jupiter.api.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DaoTests {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;
    private final GameDAO gameDAO;
    private final UserData john;


    public DaoTests() {
        this.authDAO = new MemoryAuthDAO();
        this.userDAO = new MemoryUserDAO();
        this.gameDAO = new MemoryGameDAO();

        this.john = new UserData("John Doe", "password123", "john@gmail.com");

    }

    @BeforeEach
    public void cleanup() {
        this.userDAO.clear();
        this.gameDAO.clear();
    }

    @Test
    @Order(1)
    @DisplayName("Test add user")
    public void makeUser() {
        try {

            this.userDAO.addUser(john);
        } catch (Exception e) {
            assert (false);
        }


    }
    @Test
    @DisplayName("Test double add user")
    public void doubleUser(){
        try{
            this.userDAO.addUser(john);
            this.userDAO.addUser(john);
            assert(false);
        } catch (ServiceException e) {
            assert(true);
        }
    }

   @Test
   @DisplayName("Test clear userDao")
   public void clearDAO(){
            this.userDAO.clear();
   }


    @Test
    @Order(2)
    @DisplayName("Test get user by username")
    public void getUser() {

        try {
            this.userDAO.addUser(john);
            var res = this.userDAO.getUser(this.john.username());
            assert (res == this.john);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    @Order(3)
    @DisplayName("Get nonexistent user by name")
    public void getFakeUser() {
        var res = this.userDAO.getUser("random");


        assert (res == null);
    }

    @Test
    @DisplayName("Clear user dao")
    public void clearUserDAO() {
        this.userDAO.clear();
    }

    @Test
    @Order(4)
    @DisplayName("Get auth token for user")
    public void getAuthToken() {
        try {
            this.userDAO.addUser(john);
            var token = this.authDAO.createAuth(john.username());
            assert (token != null);
        } catch (Exception e) {
            assert (false);
        }

    }

    @Test
    @Order(5)
    @DisplayName("Delete auth token")
    public void deleteAuthToken() {
        try {
            this.userDAO.addUser(john);
            var token = this.authDAO.createAuth(john.username());
            this.authDAO.deleteAuthToken(token);
        } catch (Exception e) {
            assert (false);
        }

    }

    @Test
    @Order(6)
    @DisplayName("Test list games")
    public void listGames() {
        try {
            this.gameDAO.getGames();
        } catch (Exception e) {
            assert (false);
        }

    }

    @Test
    @DisplayName("Test create game")
    public void createGame() {
        var gameID = this.gameDAO.createGame("dopeGame");
        assert (gameID > 0);

    }

    @Test
    @DisplayName("Test join game")
    public void joinGame() {
        var gameName = "game";
        var gameID = this.gameDAO.createGame(gameName);

        try {
            this.gameDAO.joinGame("temp", "BLACK", gameID);
            this.gameDAO.joinGame("other", "WHITE", gameID);
        } catch (Exception e) {
            assert (false);
        }
    }

    @Test
    @DisplayName("Test join non-existent game")
    public void joinGameNegative() {
        try {
            this.gameDAO.joinGame("temp", "BLACK", 12);
            assert (false);
        } catch (Exception e) {
            assert (true);
        }
    }

    @Test
    @DisplayName("Test clear games")
    public void clearGames() {
        this.gameDAO.clear();
        assert (this.gameDAO.getGames().isEmpty());
    }

    @Test
    @DisplayName("Test join full game")
    public void joinFullGames() {
        var gameID = gameDAO.createGame("agame");

        try {

            this.gameDAO.joinGame("p1", "WHITE", gameID);
            this.gameDAO.joinGame("p2", "BLACK", gameID);
        } catch (Exception e) {
            assert (false);
        }

        try {
            this.gameDAO.joinGame("hello", "WHITE", gameID);
        } catch (Exception e) {
            assert (true);
        }
    }

    @Test
    @DisplayName("Test join game with non white/black color")
    public void joinGameWithWrongColor() {
        var gameID = this.gameDAO.createGame("kanye_game");

        try {

            this.gameDAO.joinGame("kanye", "BLUE", gameID);
        } catch (ServiceException e) {
            assert (true);
        }
    }

    @Test
    @DisplayName("Join as already taken color")
    public void joinGameWithTakenColor() {

        var gameID = this.gameDAO.createGame("Chess.com");

        try {
            this.gameDAO.joinGame("Magnus Carlson", "WHITE", gameID);
            this.gameDAO.joinGame("Bill Gates", "WHITE", gameID);
            assert (false);
        } catch (ServiceException e) {
            assert (true);
        }
    }

    @Test
    @DisplayName("gameDAO get instance not null")
    public void gameDaoGetInstance() {

        assert (MemoryGameDAO.getInstance() != null);
    }

    @Test
    @DisplayName("userDAO get instance not null")
    public void userDaoGetInstance() {
        assert (MemoryUserDAO.getInstance() != null);
    }


}
