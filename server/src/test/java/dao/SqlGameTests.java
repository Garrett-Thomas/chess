package dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SqlGameTests {
   private static final SQLGameDAO SQL_GAME_DAO = new SQLGameDAO();



    @BeforeAll
    public static void init(){
        SQL_GAME_DAO.clear();
       SQL_GAME_DAO.createGame("test");

    }

    @Test
    public void testList(){
        var res = SQL_GAME_DAO.getGames();
        assert(!res.isEmpty());
    }

}
