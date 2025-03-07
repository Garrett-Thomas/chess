package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueenMovesCalculator extends CalcType implements PieceMovesCalculator {
    QueenMovesCalculator() {
        super(QueenMovesCalculator.class.getName());
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> allMoves = new ArrayList<>();
        allMoves.addAll(PieceUtils.getDiagonalMoves(board, pos));
        allMoves.addAll(PieceUtils.getCrossMoves(board, pos));
        return allMoves;
    }
}
