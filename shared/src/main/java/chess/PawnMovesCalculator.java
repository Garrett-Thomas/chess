package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {

    private final int[][][] diagMoves;
    private final int[][] forwardMoves;
    private final int[][] startMoves;

    PawnMovesCalculator() {

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

        // First if pawn not near either side
        if (pos.getRow() < 7 && pos.getRow() > 2) {

            for (int[] move : this.diagMoves[selector]) {
                toMove = new ChessMove(pos, new ChessPosition(pos.getRow() + move[1], pos.getColumn() + move[0]), null);
                if (PieceUtils.isValidMove(board, toMove) && PieceUtils.isTakeMove(board, toMove)) {
                    allMoves.add(toMove);
                }
            }

            // Check forward moves
            toMove = new ChessMove(pos, new ChessPosition(pos.getRow() + this.forwardMoves[selector][1], pos.getColumn() + this.forwardMoves[selector][0]), null);

            if (PieceUtils.isValidMove(board, toMove) && !PieceUtils.isTakeMove(board, toMove)) {
                allMoves.add(toMove);
            }

        }

        // Check first move is 2 && if move 2 then cannot jump over
        if (pos.getRow() == 2 && selector == 0 || pos.getRow() == 7 && selector == 1) {

            toMove = new ChessMove(pos, new ChessPosition(pos.getRow() + this.startMoves[selector][1], pos.getColumn() + this.startMoves[selector][0]), null);
            ChessPiece intermedPiece = board.getPiece(new ChessPosition(pos.getRow() + this.startMoves[selector][1] + (selector == 0 ? -1 : 1), pos.getColumn() + this.startMoves[selector][0]));

            if (PieceUtils.isValidMove(board, toMove) && intermedPiece == null && !PieceUtils.isTakeMove(board, toMove)) {
                allMoves.add(toMove);
            }

            toMove = new ChessMove(pos, new ChessPosition(pos.getRow() + this.forwardMoves[selector][1], pos.getColumn() + this.forwardMoves[selector][0]), null);
            if (PieceUtils.isValidMove(board, toMove)) {
                allMoves.add(toMove);
            }

            for (int[] possibleMove : this.diagMoves[selector]) {
                toMove = new ChessMove(pos, new ChessPosition(pos.getRow() + possibleMove[1], pos.getColumn() + possibleMove[0]), null);
                if (PieceUtils.isValidMove(board, toMove) && PieceUtils.isTakeMove(board, toMove)) {
                    allMoves.add(toMove);
                }
            }
        }


        // Check edges and promotion
        if (pos.getRow() == 7 && selector == 0 || pos.getRow() == 2 && selector == 1) {
            for (int[] move : this.diagMoves[selector]) {
                toMove = new ChessMove(pos, new ChessPosition(pos.getRow() + move[1], pos.getColumn() + move[0]), null);
                if (PieceUtils.isValidMove(board, toMove) && PieceUtils.isTakeMove(board, toMove)) {
                    allMoves.addAll(addPromotionPiece(toMove));
                }

            }

            toMove = new ChessMove(pos, new ChessPosition(pos.getRow() + this.forwardMoves[selector][1], pos.getColumn() + this.forwardMoves[selector][0]), null);
            if (PieceUtils.isValidMove(board, toMove) && !PieceUtils.isTakeMove(board, toMove)) {
                allMoves.addAll(addPromotionPiece(toMove));
            }

        }

        return allMoves;

    }

}