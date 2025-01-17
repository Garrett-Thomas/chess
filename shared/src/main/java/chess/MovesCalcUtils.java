package chess;

public class MovesCalcUtils {

    public static boolean isPieceNull(ChessPosition newPosition, ChessBoard board) {

        ChessPiece otherPiece = board.getPiece(newPosition);

        return otherPiece == null;

    }

    public static boolean isValidMove(ChessPosition currPosition, ChessPosition newPosition, ChessBoard board) {

        ChessPiece currPiece = board.getPiece(currPosition);
        ChessGame.TeamColor selfColor = currPiece.getTeamColor();
        ChessPiece otherPiece = board.getPiece(newPosition);
        if (otherPiece == null) {
            return true;
        }

        return otherPiece.getTeamColor() != selfColor;

    }

}
