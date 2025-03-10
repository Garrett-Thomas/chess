package dataaccess;

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
    public void testListGames(){
        var res = SQL_GAME_DAO.getGames();
        assert(!res.isEmpty());
    }
    @Test
    public void testCreateGame(){

        var id = SQL_GAME_DAO.createGame("new_game");
        assert(id > 0);
    }
    @Test
    public void negativeCreateGame(){
       var firstId = SQL_GAME_DAO.createGame("another_game");

        var secondId = SQL_GAME_DAO.createGame("another_game");

        assert(!firstId.equals(secondId));
    }

}
