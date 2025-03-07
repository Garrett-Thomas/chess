package dao;

import dataaccess.DatabaseManager;
import dataaccess.ServiceException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

public class SqlDaoTests {
    private static final AuthDAO sqlAuthDao = new SQLAuthDAO();
    public static String token;

    SqlDaoTests() {
    }

    @BeforeEach
    public void setup() throws Exception {
        token = sqlAuthDao.createAuth("test");
    }

    @Test
    public void getAuth() throws Exception {
        var res = sqlAuthDao.createAuth("Gary");
        assert (!res.isEmpty());

    }

    @Test
    public void getUser() throws Exception {
        var username = "waters";
        var token = sqlAuthDao.createAuth(username);

        var res = sqlAuthDao.getUsername(token);
        assert (username.equals(res));
    }

    @Test
    public void authUser(){
        assert(sqlAuthDao.validateAuth(token));
    }

    @Test
    public void authUserNegative(){
        assert(!sqlAuthDao.validateAuth("hello"));
    }

    @Test
    public void deleteToken() throws ServiceException {
        sqlAuthDao.deleteAuthToken(token);

        assert(!sqlAuthDao.validateAuth(token));

    }

    @Test
    public void testClear(){
        sqlAuthDao.clear();
        assert(sqlAuthDao.getUsername(token) == null);
    }

}
