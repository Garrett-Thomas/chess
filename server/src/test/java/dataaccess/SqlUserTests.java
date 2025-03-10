package dataaccess;

import dataaccessclasses.ServiceException;
import model.UserData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.Objects;

public class SqlUserTests {

    private static SQLUserDAO sqlUserDAO = new SQLUserDAO();
    private static UserData garyData = new UserData("gary", "pass", "waters");

    @BeforeEach
    public void init() {
        try {

            sqlUserDAO.addUser(garyData);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    @Order(1)
    public void getUser() {
        var res = sqlUserDAO.getUser(garyData.username());
        assert (Objects.equals(res.username(), garyData.username()));
    }

    @Test
    @Order(2)
    public void testClear() {
        sqlUserDAO.clear();

        var res = sqlUserDAO.getUser(garyData.username());
        assert (res == null);
    }

}
