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
            ChessMove whiteMove = new ChessMove(new ChessPosition(2, 7), new ChessPosition(4, 7), null);
            ChessMove blackMove = new ChessMove(new ChessPosition(7, 8), new ChessPosition(5, 8), null);
            chessGame.makeMove(whiteMove);
            chessGame.makeMove(blackMove);
            GamePlay.setGame(chessGame);
        } catch (Exception e) {
            System.err.println("Couldn't initialize tests");
        }

    }

    @Test
    public void highlightMoves() throws UIException {

        ArrayList<String> params = new ArrayList<>();
        params.add("h5");

        System.out.println("Hello");
        System.out.print(EscapeSequences.ERASE_SCREEN);
        System.out.println("Hello");
        System.out.print(EscapeSequences.moveCursorToLocation(30, 10));
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
