package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public class GamePlay {

    private static ChessGame game = new ChessGame();



    public static void eval(String cmd, ArrayList<String> params) {
        switch (cmd) {
            default -> drawBoard();
        }
    }

    private static String getStringForPiece(ChessPiece.PieceType type, ChessGame.TeamColor color) {
        switch (type) {
            case KING -> {
                return color == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_KING : EscapeSequences.BLACK_KING;
            }
            case QUEEN -> {
                return color == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_QUEEN : EscapeSequences.BLACK_QUEEN;
            }

            case KNIGHT -> {
                return color == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_KNIGHT : EscapeSequences.BLACK_KNIGHT;
            }
            case BISHOP -> {
                return color == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_BISHOP : EscapeSequences.BLACK_BISHOP;
            }
            case ROOK -> {
                return color == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_ROOK : EscapeSequences.BLACK_ROOK;
            }
            case PAWN -> {
                return color == ChessGame.TeamColor.WHITE ? EscapeSequences.WHITE_PAWN : EscapeSequences.BLACK_PAWN;
            }
        }
        return null;
    }

    public static void drawBoard() {
        var board = game.getBoard();


        ArrayList<String> rowsOfGame = new ArrayList<>();


        for (int i = 8; i > 0; i--) {
            StringBuilder row = new StringBuilder();
            for (int j = 1; j < 9; j++) {
                var piece = board.getPiece(new ChessPosition(i, j));
                String block = getString(i, j, piece);
                row.append(block);

            }

            row.append(EscapeSequences.RESET_BG_COLOR);
            rowsOfGame.add(row.toString());
        }

        if(LocalStorage.getTeamColor() == ChessGame.TeamColor.BLACK){
            for(String row : rowsOfGame){

                System.out.println(row);
            }
        }
        else{
            for(int j = rowsOfGame.size() - 1; j >= 0; j--){
                System.out.println(rowsOfGame.get(j));
            }
        }

//        System.out.println(EscapeSequences.SET_BG_COLOR_BLUE +  "hello"  + EscapeSequences.RESET_BG_COLOR);
    }

    private static String getString(int i, int j, ChessPiece piece) {
        boolean tern = (i % 2 == 0 && j % 2 != 0 || i % 2 != 0 && j % 2 == 0);
        tern = (LocalStorage.getTeamColor() == ChessGame.TeamColor.WHITE) != tern;
        String block =  tern ? EscapeSequences.SET_BG_COLOR_LIGHT_GREY : EscapeSequences.SET_BG_COLOR_DARK_GREY;
        if (piece == null) {
            block += EscapeSequences.EMPTY;
        } else {
            block += getStringForPiece(piece.getPieceType(), piece.getTeamColor());
        }
        return block;
    }

}
