package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] board;

    public ChessBoard() {
        this.board = new ChessPiece[8][8];

    }

    public ChessBoard(ChessBoard board) {
        this.board = new ChessPiece[8][8];


        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {

                if (board.board[i][j] == null) {
                    this.board[i][j] = null;
                    continue;
                }

                this.board[i][j] = new ChessPiece(board.board[i][j]);
            }
        }

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */


    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.board[position.getColumn() - 1][8 - position.getRow()] = new ChessPiece(piece);

    }

    public ChessPosition getKing(ChessGame.TeamColor color) {

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPiece currPiece = this.board[i][j];

                if (currPiece != null && currPiece.getPieceType() == ChessPiece.PieceType.KING && currPiece.getTeamColor() == color) {
                    return new ChessPosition(8 - j, i + 1);
                }
            }
        }

        return null;
    }


    public boolean isValidBoard(ChessPosition teamKingPos) {

        ChessGame.TeamColor kingColor = getPiece(teamKingPos).getTeamColor();

        for (int i = 0; i < 8; i++) {
            Collection<ChessMove> pieceMoves = new ArrayList<ChessMove>();
            for (int j = 0; j < 8; j++) {

                ChessPosition currPos = new ChessPosition(i + 1, j + 1);
                ChessPiece currPiece = getPiece(currPos);

                if (currPiece != null && currPiece.getTeamColor() != kingColor) {
                    pieceMoves.addAll(currPiece.pieceMoves(this, currPos));
                    boolean containsKing = pieceMoves.stream().anyMatch(currMove -> currMove.getEndPosition().equals(teamKingPos));
                    if (containsKing) {
                        return false;
                    }
                }


            }
        }

        return true;

    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */


    // 1 -> 7
    // 8 -> 0
    public ChessPiece getPiece(ChessPosition position) {
        return this.board[position.getColumn() - 1][8 - position.getRow()];
    }

    public boolean movePiece(ChessMove move) {
        ChessPiece startPiece = getPiece(move.getStartPosition());
        ChessPiece endPiece = getPiece(move.getEndPosition());
        int row;
        int col;
        if (endPiece != null && startPiece.getTeamColor() == endPiece.getTeamColor()) {
            return false;
        }

        col = move.getEndPosition().getColumn() - 1;
        row = 8 - move.getEndPosition().getRow();
        int colStart = move.getStartPosition().getColumn() - 1;
        int rowStart = 8 - move.getStartPosition().getRow();

        if (move.getPromotionPiece() == null) {

            this.board[col][row] = this.board[colStart][rowStart];

            this.board[colStart][rowStart] = null;
        } else {

            this.board[col][row] = new ChessPiece(startPiece.getTeamColor(), move.getPromotionPiece());
            this.board[colStart][rowStart] = null;
        }

        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        for (int i = 0; i < 8; i++) {

            this.board[i][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);

            this.board[i][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }

        this.board[0][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        this.board[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        this.board[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        this.board[7][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);

        this.board[1][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        this.board[6][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        this.board[1][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        this.board[6][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);

        this.board[2][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        this.board[5][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        this.board[2][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        this.board[5][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);

        this.board[3][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        this.board[3][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);

        this.board[4][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        this.board[4][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);


    }
}
