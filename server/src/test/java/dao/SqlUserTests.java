package dao;

import dataaccess.ServiceException;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.PreparedStatement;

public class SqlUserTests {

    private static SQLUserDAO sqlUserDAO = new SQLUserDAO();
    private static UserData garyData = new UserData("gary", "pass", "waters");

    @BeforeAll
    public static void init() {
        try {

            sqlUserDAO.addUser(garyData);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void getUser() {
        var res = sqlUserDAO.getUser(garyData.username());
        assert (res.equals(garyData));
    }

}
