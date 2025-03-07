package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends CalcType implements PieceMovesCalculator {

    private int[][] kingMoves;

    KingMovesCalculator() {
        super(KingMovesCalculator.class.getName());
        this.kingMoves = new int[][]{{-1, -1}, {0, -1}, {1, -1}, {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}};
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> allMoves = new ArrayList<>();
        ChessMove toMove;
        for (int i = 0; i < this.kingMoves.length; i++) {
            toMove = new ChessMove(pos, new ChessPosition(pos.getRow() + this.kingMoves[i][0], pos.getColumn() + this.kingMoves[i][1]), null);
            if (PieceUtils.isValidMove(board, toMove)) {
                allMoves.add(toMove);
            }
        }

        return allMoves;
    }
}
