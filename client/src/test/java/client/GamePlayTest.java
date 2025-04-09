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

//        LocalStorage.setTeamColor(ChessGame.TeamColor.WHITE);
        ArrayList<String> params = new ArrayList<>();
        params.add("h5");

        System.out.println("Hello");
        System.out.print(EscapeSequences.ERASE_SCREEN);
        System.out.println("Hello");
        System.out.print(EscapeSequences.moveCursorToLocation(30, 10));
        GamePlay.legalMoves(params, ChessGame.TeamColor.BLACK);

    }

    @Test
    public void testParsePosition() {
        /**
         * For a pawn at a2 of white team
         * This is at board[0][6] and we know that to get the position
         * a -> col 2 -> row
         * in this case a -> 8 2 -> 6
         public ChessPiece getPiece(ChessPosition position) {
         return this.board[position.getColumn() - 1][8 - position.getRow()];
         }
         *
         * for h7 want chess position of
         *  pos(7, 1)
         *
         *
         */
        var tempBoard = new ChessGame();
        String pos1 = "c2";
        var res = GamePlay.parseStringToPosition(pos1, ChessGame.TeamColor.WHITE);
        var piece = tempBoard.getBoard().getPiece(res);
        System.out.println();

    }
}
