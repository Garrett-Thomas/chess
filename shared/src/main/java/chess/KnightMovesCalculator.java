package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class KnightMovesCalculator extends CalcType implements PieceMovesCalculator {

    private int[][] knightMoves;

    KnightMovesCalculator() {

       super(KnightMovesCalculator.class.getName());
        this.knightMoves = new int[][]{{2, 1}, {2, -1}, {-2, 1}, {-2, -1}};
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> allMoves = new ArrayList<>();
        ChessMove toMove;
        for (int[] move : this.knightMoves) {
            toMove = new ChessMove(pos, new ChessPosition(pos.getRow() + move[0], pos.getColumn() + move[1]), null);
            if (PieceUtils.isValidMove(board, toMove)) {
                allMoves.add(toMove);
            }

            toMove = new ChessMove(pos, new ChessPosition(pos.getRow() + move[1], pos.getColumn() + move[0]), null);
            if (PieceUtils.isValidMove(board, toMove)) {
                allMoves.add(toMove);
            }

        }


        return allMoves;
    }
}
