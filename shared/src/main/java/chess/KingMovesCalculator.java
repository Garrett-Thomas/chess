package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator{

    KingMovesCalculator(){
    }

        // Need to check the cross and the diagonal for this
     public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){

         Collection<ChessMove> allMoves = new ArrayList<ChessMove>();
         ChessPiece kingPiece = board.getPiece(position);
         ChessPosition toMove;
         int[][] kingMoves = new int[][]{
                 {-1, -1}, {-1, 0}, {-1, 1},{0, 1}, {1, 1},{1, 0},{1, -1},{0, -1}
         };

         if (kingPiece.getPieceType() != ChessPiece.PieceType.KING) {
             throw new RuntimeException("Piece is not a pawn");
         }

        for(int [] arr : kingMoves){

            toMove = new ChessPosition(position.getBoardRow() + arr[0], position.getBoardColumn() + arr[1]);

            if(MovesCalcUtils.isInBounds(toMove) && MovesCalcUtils.isValidMove(position, toMove, board)){
                allMoves.add(new ChessMove(position, toMove, null));
            }



        }

        return allMoves;
    }
}
