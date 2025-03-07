package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends CalcType implements PieceMovesCalculator {

    private final int[][][] diagMoves;
    private final int[][] forwardMoves;
    private final int[][] startMoves;

    PawnMovesCalculator() {
        super(PawnMovesCalculator.class.getName());

        // White first then black
        this.diagMoves = new int[][][]{{{1, 1}, {-1, 1}}, {{-1, -1}, {1, -1}}};
        this.forwardMoves = new int[][]{{0, 1}, {0, -1}};
        this.startMoves = new int[][]{{0, 2}, {0, -2}};
    }

    private Collection<ChessMove> addPromotionPiece(ChessMove move) {
        Collection<ChessMove> moves = new ArrayList<>();
        moves.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.KNIGHT));
        moves.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.ROOK));
        return moves;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> allMoves = new ArrayList<>();
        ChessGame.TeamColor pieceCol = board.getPiece(pos).getTeamColor();
        int selector = pieceCol == ChessGame.TeamColor.WHITE ? 0 : 1;
        ChessMove toMove;

        int row;
        int col;
        // First if pawn not near either side
        if (pos.getRow() < 7 && pos.getRow() > 2) {

            for (int[] move : this.diagMoves[selector]) {
                row = pos.getRow() + move[1];
                col = pos.getColumn() + move[0];
                toMove = new ChessMove(pos, new ChessPosition(row, col), null);
                if (PieceUtils.isValidMove(board, toMove) && PieceUtils.isTakeMove(board, toMove)) {
                    allMoves.add(toMove);
                }
            }

            // Check forward moves
            row = pos.getRow() + this.forwardMoves[selector][1];
            col = pos.getColumn() + this.forwardMoves[selector][0];
            toMove = new ChessMove(pos, new ChessPosition(row, col), null);

            if (PieceUtils.isValidMove(board, toMove) && !PieceUtils.isTakeMove(board, toMove)) {
                allMoves.add(toMove);
            }

        }

        // Check first move is 2 && if move 2 then cannot jump over
        if (pos.getRow() == 2 && selector == 0 || pos.getRow() == 7 && selector == 1) {

            row = pos.getRow() + this.startMoves[selector][1];
            col = pos.getColumn() + this.startMoves[selector][0];

            toMove = new ChessMove(pos, new ChessPosition(row, col), null);

            row = pos.getRow() + this.startMoves[selector][1] + (selector == 0 ? -1 : 1);
            col = pos.getColumn() + this.startMoves[selector][0];

            ChessPiece intermedPiece = board.getPiece(new ChessPosition(row, col));

            if (PieceUtils.isValidMove(board, toMove) && intermedPiece == null && !PieceUtils.isTakeMove(board, toMove)) {
                allMoves.add(toMove);
            }

            row = pos.getRow() + this.forwardMoves[selector][1];
            col = pos.getColumn() + this.forwardMoves[selector][0];
            toMove = new ChessMove(pos, new ChessPosition(row, col), null);

            if (PieceUtils.isValidMove(board, toMove)) {
                allMoves.add(toMove);
            }

            for (int[] possibleMove : this.diagMoves[selector]) {

                row = pos.getRow() + possibleMove[1];
                col = pos.getColumn() + possibleMove[0];
                toMove = new ChessMove(pos, new ChessPosition(row, col), null);
                if (PieceUtils.isValidMove(board, toMove) && PieceUtils.isTakeMove(board, toMove)) {
                    allMoves.add(toMove);
                }
            }
        }


        // Check edges and promotion
        if (pos.getRow() == 7 && selector == 0 || pos.getRow() == 2 && selector == 1) {
            for (int[] move : this.diagMoves[selector]) {

                row = pos.getRow() + move[1];
                col = pos.getColumn() + move[0];
                toMove = new ChessMove(pos, new ChessPosition(row, col), null);
                if (PieceUtils.isValidMove(board, toMove) && PieceUtils.isTakeMove(board, toMove)) {
                    allMoves.addAll(addPromotionPiece(toMove));
                }

            }

            row = pos.getRow() + this.forwardMoves[selector][1];
            col = pos.getColumn() + this.forwardMoves[selector][0];
            toMove = new ChessMove(pos, new ChessPosition(row, col), null);
            if (PieceUtils.isValidMove(board, toMove) && !PieceUtils.isTakeMove(board, toMove)) {
                allMoves.addAll(addPromotionPiece(toMove));
            }

        }

        return allMoves;

    }

}