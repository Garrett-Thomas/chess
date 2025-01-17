package chess;


import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.ArrayList;

public class KnightMovesCalculator implements PieceMovesCalculator {
    KnightMovesCalculator() {

    }


    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        Collection<ChessMove> allMoves = new ArrayList<ChessMove>();

        ChessPiece knightPiece = board.getPiece(position);
        ChessPosition toMove;

        if (knightPiece.getPieceType() != ChessPiece.PieceType.KNIGHT) {
            throw new RuntimeException("Piece is not a pawn");
        }

        int[][] kMoves = {{-1, 2}, {-1, -2}, {1, 2}, {1, -2}};


        for(int[] arr : kMoves){

            toMove = new ChessPosition(position.getBoardRow() + arr[0], position.getBoardColumn() + arr[1]);

            if(MovesCalcUtils.isInBounds(toMove) && MovesCalcUtils.isValidMove(position, toMove,  board)){
                allMoves.add(new ChessMove(position, toMove, null));
            }

            toMove = new ChessPosition(position.getBoardRow() + arr[1], position.getBoardColumn() + arr[0]);
            if(MovesCalcUtils.isInBounds(toMove) && MovesCalcUtils.isValidMove(position,toMove, board)){
                allMoves.add(new ChessMove(position, toMove, null));
            }
        }


        return allMoves;
    }
}