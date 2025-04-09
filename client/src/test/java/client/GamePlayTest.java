package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
            ChessMove whiteMove = new ChessMove(new ChessPosition(2, 1), new ChessPosition(3, 1), null);
            chessGame.makeMove(whiteMove);
            GamePlay.setGame(chessGame);
        } catch (Exception e) {
            System.err.println("Couldn't initialize tests");
        }

    }

    @Test
    public void highlightMoves() throws UIException {

        LocalStorage.setTeamColor(ChessGame.TeamColor.BLACK);
        ArrayList<String> params = new ArrayList<>();
        params.add("h7");

        GamePlay.legalMoves(params);

    }

    @Test
    public void testParsePosition() {
        /**
         * For a pawn at a2 of white team
         * This is at board[0][6] and we know that to get the position
         *
         */
        var tempBoard = new ChessGame();
        String pos1 = "a1";
        var res = GamePlay.parseStringToPosition(pos1, ChessGame.TeamColor.BLACK);
        var piece = tempBoard.getBoard().getPiece(res);
        System.out.println();

    }
}
