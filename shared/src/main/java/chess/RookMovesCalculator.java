package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator extends CalcType implements PieceMovesCalculator {

    RookMovesCalculator() {
         super(RookMovesCalculator.class.getName());
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition pos) {
        return PieceUtils.getCrossMoves(board, pos);
    }


}
