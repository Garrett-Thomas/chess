package dataaccess;

import dao.AuthDAO;
import dao.SQLAuthDAO;
import dataaccessclasses.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SqlDaoTests {
    private static final AuthDAO SQL_AUTH_DAO = new SQLAuthDAO();
    public static String token;

    SqlDaoTests() {
    }

    @BeforeEach
    public void setup() throws Exception {
        token = SQL_AUTH_DAO.createAuth("test");
    }

    @Test
    public void getAuth() throws Exception {
        var res = SQL_AUTH_DAO.createAuth("Gary");
        assert (!res.isEmpty());

    }

    @Test
    public void getUser() throws Exception {
        var username = "waters";
        var token = SQL_AUTH_DAO.createAuth(username);

        var res = SQL_AUTH_DAO.getUsername(token);
        assert (username.equals(res));
    }

    @Test
    public void authUser(){
        assert(SQL_AUTH_DAO.validateAuth(token));
    }

    @Test
    public void authUserNegative(){
        assert(!SQL_AUTH_DAO.validateAuth("hello"));
    }

    @Test
    public void deleteToken() throws ServiceException {
        SQL_AUTH_DAO.deleteAuthToken(token);

        assert(!SQL_AUTH_DAO.validateAuth(token));

    }

    @Test
    public void testClear(){
        SQL_AUTH_DAO.clear();
        assert(SQL_AUTH_DAO.getUsername(token) == null);
    }

}
