import chess.ChessGame;
import org.junit.jupiter.api.Test;
import ui.GamePlay;
import ui.LocalStorage;

public class GamePlayTest {



   @Test
   public void drawGame(){

       LocalStorage.setTeamColor(ChessGame.TeamColor.BLACK);
       GamePlay.drawBoard();


      assert(true);
   }

}
