package json;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.CoordinateParser;
import utils.GsonParent;

public class ChessGameParsing {

    private static ChessGame chessGame = null;
    private static Gson gson = GsonParent.getInstance();

    public static class Test{
       private static ChessGame game;



       public static void setGame(ChessGame game){
           Test.game = game;
       }


       public static ChessGame getGame(){
           return Test.game;
       }

    }


    @BeforeAll
    public static void setup() {
        try {
            chessGame = new ChessGame();
            ChessMove whiteMove = new ChessMove(new ChessPosition(2, 1), new ChessPosition(3, 1), null);
            chessGame.makeMove(whiteMove);

        } catch (Exception e) {
            System.err.println("Couldn't initialize tests");
        }


    }

    @org.junit.jupiter.api.Test
    public void testParse() {

        var gameJson = gson.toJson(chessGame);
        var fromJson = gson.fromJson(gameJson, ChessGame.class);
        assert(fromJson.getBoard().equals(chessGame.getBoard()));

    }

    @org.junit.jupiter.api.Test
    public void testSetChessGame() {

        var coord = new ChessPosition(2, 1);
        var res = chessGame.getBoard().getPiece(coord);
        System.out.println(CoordinateParser.parseCoordinates(coord));
    }

    }
