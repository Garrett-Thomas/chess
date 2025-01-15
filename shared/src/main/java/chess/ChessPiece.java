package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;
    private boolean hasMoved;
    private PieceMovesCalculator calculator;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
        this.hasMoved = false;
        switch(this.type){
            case KING -> this.calculator = new KingMovesCalculator();
            case QUEEN -> this.calculator = new QueenMovesCalculator();
            case BISHOP -> this.calculator = new BishopMovesCalculator();
            case KNIGHT -> this.calculator = new KnightMovesCalculator();
            case ROOK -> this.calculator = new RookMovesCalculator();
            case PAWN -> this.calculator = new PawnMovesCalculator();
        }
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        if( that.getPieceType() != this.type) return false;
        return that.getTeamColor() == this.pieceColor;
    }
    @Override
    public int hashCode(){
        return 31 * (this.pieceColor.ordinal() + this.type.ordinal());
    }
    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return this.calculator.pieceMoves(board, myPosition);
    }

    public boolean getHasMoved(){
        return this.hasMoved;
    }

    public void setHasMoved(){
        this.hasMoved = true;
    }
}
