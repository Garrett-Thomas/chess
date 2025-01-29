package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator{

   public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition pos){
        return PieceUtils.getCrossMoves(board, pos);
   }



}
