package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator implements PieceMovesCalculator{

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> allMoves;
        ChessPiece queenPiece = board.getPiece(position);

        if (queenPiece.getPieceType() != ChessPiece.PieceType.QUEEN) {
            throw new RuntimeException("Piece is not a pawn");
        }
        allMoves = MovesCalcUtils.getCrossMoves(position, board);
        allMoves.addAll(MovesCalcUtils.getDiagonalMoves(position, board));

        return allMoves;
    }

    }
