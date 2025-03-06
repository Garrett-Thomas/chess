package dao;

import dataaccess.DatabaseManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

public class SqlDaoTests {
    private static final AuthDAO sqlAuthDao = new SQLAuthDAO();


    SqlDaoTests() {
    }

    @BeforeAll
    public static void setup() throws Exception {
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


}
