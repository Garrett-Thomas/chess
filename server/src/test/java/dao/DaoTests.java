package dao;

import model.UserData;
import org.junit.jupiter.api.*;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DaoTests {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    private final UserData john;


    public DaoTests() {
        this.authDAO = new MemoryAuthDAO();
        this.userDAO = new MemoryUserDAO();
        this.john = new UserData("John Doe", "password123", "john@gmail.com");

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
    @Order(4)
    @DisplayName("Get auth token for user")
    public void getAuthToken() {
        try {
            this.userDAO.addUser(john);
            var token = this.authDAO.createAuth(john.username());
            assert(token != null);
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
}
