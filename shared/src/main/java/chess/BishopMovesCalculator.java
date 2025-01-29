package chess;

import java.util.Collection;
import java.util.List;

public class BishopMovesCalculator implements PieceMovesCalculator {

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition pos) {
        return PieceUtils.getDiagonalMoves(board, pos);
    }
}
