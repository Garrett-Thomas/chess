package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator{
    RookMovesCalculator(){

    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> allMoves;

        ChessPiece rookPiece = board.getPiece(position);
        if(rookPiece.getPieceType() != ChessPiece.PieceType.ROOK){
            throw new RuntimeException("Piece is not a rook");
        }

        allMoves = MovesCalcUtils.getCrossMoves(position, board);

        return allMoves;

    }
}
