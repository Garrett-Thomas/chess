package chess;

import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator extends CalcType implements PieceMovesCalculator {

    BishopMovesCalculator() {
        super(BishopMovesCalculator.class.getName());
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition pos) {
        return PieceUtils.getDiagonalMoves(board, pos);
    }
}
