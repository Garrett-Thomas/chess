package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator implements PieceMovesCalculator{

    private final int[][] kingMoves;
    KingMovesCalculator(){
        this.kingMoves = new int[][]{
                {-1, -1}, {-1, 0}, {-1, 1},{0, 1}, {1, 1},{1, 0},{1, -1},{0, -1}
        };
    }

        // Need to check the cross and the diagonal for this
     public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){

        Collection<ChessMove> allMoves = new ArrayList<ChessMove>();




        // col is x, row is y
        for(int[] direction : this.kingMoves){

            int col = position.getColumn();
            int row = position.getRow();

            col += direction[1];
            row += direction[0];

            if (col > 7 || col < 0 || row > 7 || row < 0){
                continue;
            }


        }







        return allMoves;
    }
}
