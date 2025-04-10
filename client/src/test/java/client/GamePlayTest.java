package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ui.EscapeSequences;
import ui.GamePlay;
import ui.LocalStorage;
import ui.UIException;

import java.util.ArrayList;

public class GamePlayTest {

    private static ChessGame chessGame;

    @BeforeAll
    public static void setup() {
        try {
            chessGame = new ChessGame();
            var move1 = new ChessMove(new ChessPosition(2, 7), new ChessPosition(4, 7), null);
            var move2 = new ChessMove(new ChessPosition(7, 4), new ChessPosition(5, 4), null);

            var move3 = new ChessMove(new ChessPosition(4, 7), new ChessPosition(5, 7), null);
            var move4 = new ChessMove(new ChessPosition(5, 4), new ChessPosition(4, 4), null);

            chessGame.makeMove(move1);
            chessGame.makeMove(move2);
            chessGame.makeMove(move3);
            chessGame.makeMove(move4);
            GamePlay.setGame(chessGame);
        } catch (Exception e) {
            System.err.println("Couldn't initialize tests");
        }

    }

    @Test
    public void highlightMoves() throws UIException {

        ArrayList<String> params = new ArrayList<>();
        params.add("g5");
        GamePlay.legalMoves(params, ChessGame.TeamColor.BLACK);

    }

    @Test
    public void testParsePosition() throws UIException {
        var tempBoard = new ChessGame();
        String pos1 = "c2";
        var res = GamePlay.parseStringToPosition(pos1, ChessGame.TeamColor.WHITE);
        var piece = tempBoard.getBoard().getPiece(res);
        System.out.println();

    }
}
