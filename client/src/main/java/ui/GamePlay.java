package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.ArrayList;

public class GamePlay {

    private static ChessGame game;
    private static ArrayList<String> header = genHeader();
    private static final int asciiA = 97;

    public static void eval(String cmd, ArrayList<String> params) {
        switch (cmd) {
            case "help" -> printHelp();
            case "move" -> executeMove(params);
            default -> drawBoard();
        }
    }

    private static void printHelp() {
        System.out.println("""
                help -> this message
                move -> [from] [to] e.g. A6 B5
                """);
    }

    private static ChessPosition parseStringToPosition(String pos, ChessGame.TeamColor playerColor) {
        int col = (int) pos.toCharArray()[0] - asciiA + 1;
        int row = Integer.valueOf(pos.toCharArray()[1] + "");

        // Must invert coordinates
        if (playerColor == ChessGame.TeamColor.BLACK) {
            col = 9 - col;
            row = 9 - row;
        }

        return new ChessPosition(row, col);

    }

    private static ChessMove parseStringsToChessMove(String from, String to, ChessPiece.PieceType promotionPiece, ChessGame.TeamColor playerColor) {
        var fromPos = parseStringToPosition(from, playerColor);
        var toPos = parseStringToPosition(to, playerColor);

        return new ChessMove(fromPos, toPos, promotionPiece);
    }

    private static void executeMove(ArrayList<String> move) {
        if (move.size() < 2 || move.size() > 3) {
            throw new RuntimeException("Invalid Chess Move");
        }

        String from = move.get(0).toLowerCase();
        String to = move.get(1).toLowerCase();

        // TODO: third parameter can be the promotion piece
        var parsedMove = parseStringsToChessMove(from, to, null, LocalStorage.getTeamColor());

    }


    public static void setGame(ChessGame game) {
        GamePlay.game = game;
    }

    private static ArrayList<String> genHeader() {
        int a = 97;
        ArrayList<String> header = new ArrayList<String>();
        header.add(EscapeSequences.SET_TEXT_BOLD);
        header.add(EscapeSequences.SET_BG_COLOR_WHITE + "   " + EscapeSequences.RESET_BG_COLOR);
        for (int k = 0; k < 8; k++) {

            header.add(EscapeSequences.SET_BG_COLOR_WHITE + " " + (char) a++ + " " + EscapeSequences.RESET_BG_COLOR);
        }


        header.add(EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.EMPTY + EscapeSequences.RESET_BG_COLOR);
        return header;
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


        ArrayList<ArrayList<String>> gameBoard = new ArrayList<>();
        gameBoard.add(header);
        for (int i = 8; i > 0; i--) {
            ArrayList<String> row = new ArrayList<>();

            String col = EscapeSequences.SET_BG_COLOR_WHITE + " " + (i) + " " + EscapeSequences.RESET_BG_COLOR;
            row.add(col);
            for (int j = 1; j < 9; j++) {
                var piece = board.getPiece(new ChessPosition(i, j));
                String block = getString(i, j, piece);
                row.add(block);

            }

            row.add(col);
            gameBoard.add(row);
        }

        gameBoard.add(header);
        if (LocalStorage.getTeamColor() == ChessGame.TeamColor.WHITE) {
            for (ArrayList<String> row : gameBoard) {
                System.out.println(String.join("", row));
            }

        } else {


            for (int i = gameBoard.size() - 1; i >= 0; i--) {
                StringBuilder reversedRow = new StringBuilder();
                for (int j = gameBoard.get(i).size() - 1; j >= 0; j--) {
                    reversedRow.append(gameBoard.get(i).get(j));
                }
                System.out.println(reversedRow);
            }
        }
    }

    private static String getString(int i, int j, ChessPiece piece) {
        boolean tern = (i % 2 == 0 && j % 2 != 0 || i % 2 != 0 && j % 2 == 0);
        String block = tern ? EscapeSequences.SET_BG_COLOR_LIGHT_GREY : EscapeSequences.SET_BG_COLOR_DARK_GREY;
        if (piece == null) {
            block += EscapeSequences.EMPTY;
        } else {
            block += getStringForPiece(piece.getPieceType(), piece.getTeamColor());
        }
        return block;
    }

}
