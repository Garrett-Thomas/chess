package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator{

    BishopMovesCalculator(){

    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> allMoves;
        ChessPiece bishPiece= board.getPiece(position);

        if (bishPiece.getPieceType() != ChessPiece.PieceType.BISHOP) {
            throw new RuntimeException("Piece is not a pawn");
        }

        allMoves = MovesCalcUtils.getDiagonalMoves(position, board);
        return allMoves;
    }

}
