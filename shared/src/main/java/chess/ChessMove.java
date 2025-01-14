package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    private ChessPosition startPosition;
    private ChessPosition endPosition;
    private ChessPiece.PieceType promotionPiece;

    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.promotionPiece = promotionPiece;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessMove that = (ChessMove) o;

        boolean intermedResult = this.startPosition.equals(that.getStartPosition()) && this.endPosition.equals(that.getEndPosition());

        if (this.promotionPiece == null && that.getPromotionPiece() == null){
            return intermedResult;
        }
        else if(this.promotionPiece == null || that.getPromotionPiece() == null){
            return false;
        }
        else{
            return intermedResult && this.promotionPiece.equals(that.getPromotionPiece());
        }
    }

    @Override
    public int hashCode() {

        int promPieceVal = 0;

        if (this.promotionPiece != null){
            promPieceVal = this.promotionPiece.hashCode();
        }

        return 31 * ( this.startPosition.hashCode() + this.endPosition.hashCode() + promPieceVal);
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return this.startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return this.endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return this.promotionPiece;
    }
}
