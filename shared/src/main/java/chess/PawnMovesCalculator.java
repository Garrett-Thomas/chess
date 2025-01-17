package chess;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator implements PieceMovesCalculator {
    PawnMovesCalculator() {

    }

    /***
     *
     * @param board
     * @param position
     * @return Collection of moves that the piece can make
     *
     * Current Issue is that I am not distinguishing between board position and array position.
     *
     */

    private void addPromotePieces(Collection<ChessMove> moves, ChessPosition curr, ChessPosition toMove) {
        moves.add(new ChessMove(curr, toMove, ChessPiece.PieceType.KNIGHT));
        moves.add(new ChessMove(curr, toMove, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(curr, toMove, ChessPiece.PieceType.QUEEN));
        moves.add(new ChessMove(curr, toMove, ChessPiece.PieceType.BISHOP));
    }


    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {

        Collection<ChessMove> allMoves = new ArrayList<ChessMove>();
        ChessPiece pawnPiece = board.getPiece(position);
        ChessPosition toMove;

        if (pawnPiece.getPieceType() != ChessPiece.PieceType.PAWN) {
            throw new RuntimeException("Piece is not a pawn");
        }

        boolean isBlack = pawnPiece.getTeamColor() == ChessGame.TeamColor.BLACK;
        boolean possiblePromotion = (isBlack && position.getRow() >= 6) || (!isBlack && position.getRow() <= 1);
        int moveTwo = isBlack ? -2 : 2;
        int moveOne = isBlack ? -1 : 1;
        int startingRow = isBlack ? 1 : 6;


        int[][] moveDiagonal = isBlack ? new int[][]{{-1, 1}, {-1, -1}} : new int[][]{{1, 1}, {1, -1}};


        // If not on left side or not on top or bottom can check the left diagonal.
        if (position.getColumn() > 0 && ((position.getRow() > 1 && position.getRow() < 6) || !possiblePromotion)) {

            if (isBlack) {
                toMove = new ChessPosition(position.getBoardRow() + moveDiagonal[1][0], position.getBoardColumn() + moveDiagonal[1][1]);
            } else {
                toMove = new ChessPosition(position.getBoardRow() + moveDiagonal[1][0], position.getBoardColumn() + moveDiagonal[1][1]);
            }

                if (MovesCalcUtils.isValidMove(position, toMove, board) && !MovesCalcUtils.isPieceNull(toMove, board)) {
                    allMoves.add(new ChessMove(position, toMove, null));
                }

            }

            // If not on right edge and not on top nor on bottom can check the right diagonal
            if (position.getColumn() < 7 && ((position.getRow() > 1 && position.getRow() < 6) || !possiblePromotion)) {

                if (isBlack) {
                    toMove = new ChessPosition(position.getBoardRow() + moveDiagonal[0][0], position.getBoardColumn() + moveDiagonal[0][1]);
                } else {

                    toMove = new ChessPosition(position.getBoardRow() + moveDiagonal[0][0], position.getBoardColumn() + moveDiagonal[0][1]);

                }
                if (MovesCalcUtils.isValidMove(position, toMove, board) && !MovesCalcUtils.isPieceNull(toMove, board)) {
                    allMoves.add(new ChessMove(position, toMove, null));
                }
            }

            if (!pawnPiece.getHasMoved() && position.getRow() == startingRow) {
                toMove = new ChessPosition(position.getBoardRow() + moveTwo, position.getBoardColumn());

                // Need to ensure that pawn doesn't jump over a piece
                ChessPosition intermedPos = new ChessPosition(position.getBoardRow() + moveOne, position.getBoardColumn());

                if (MovesCalcUtils.isValidMove(position, toMove, board) && MovesCalcUtils.isPieceNull(toMove, board) && MovesCalcUtils.isPieceNull(intermedPos, board)) {
                    allMoves.add(new ChessMove(position, toMove, null));
                }

            }


            // Check for Promotions
            // This checks to see if the pawn is one turn away from the edge but doesn't check the corners.
            if ((position.getRow() == 1 || position.getRow() == 6) && position.getColumn() > 0 && position.getColumn() < 7 && possiblePromotion) {
                // Check Right Diagonal
                toMove = new ChessPosition(position.getBoardRow() + moveDiagonal[0][0], position.getBoardColumn() + moveDiagonal[0][1]);
                if (MovesCalcUtils.isValidMove(position, toMove, board) && !MovesCalcUtils.isPieceNull(toMove, board)) {
                    addPromotePieces(allMoves, position, toMove);
                }
                // Check Left Diagonal
                toMove = new ChessPosition(position.getBoardRow() + moveDiagonal[1][0], position.getBoardColumn() + moveDiagonal[1][1]);
                if (MovesCalcUtils.isValidMove(position, toMove, board) && !MovesCalcUtils.isPieceNull(toMove, board)) {
                    addPromotePieces(allMoves, position, toMove);
                }


            }

            // Check corners
            // Left Corners
            if (possiblePromotion && position.getColumn() == 0 && (position.getRow() == 1 || position.getRow() == 6)) {

                // Need to see if they can make right diagonal
                toMove = new ChessPosition(position.getBoardRow() + moveDiagonal[0][0], position.getBoardColumn() + moveDiagonal[0][1]);
                if (MovesCalcUtils.isValidMove(position, toMove, board) && !MovesCalcUtils.isPieceNull(toMove, board)) {
                    addPromotePieces(allMoves, position, toMove);
                }
            }

            // Right Corners
            if (possiblePromotion && position.getColumn() == 7 && (position.getRow() == 1 || position.getRow() == 6)) {

                toMove = new ChessPosition(position.getBoardRow() + moveDiagonal[0][0], position.getBoardColumn() + moveDiagonal[0][1]);
                if (MovesCalcUtils.isValidMove(position, toMove, board) && !MovesCalcUtils.isPieceNull(toMove, board)) {
                    addPromotePieces(allMoves, position, toMove);
                }
            }


            // Forward Promotion
            if(possiblePromotion && (position.getRow() == 6 || position.getRow() == 1)){
                toMove = new ChessPosition(position.getBoardRow() + moveOne, position.getBoardColumn());

                if(MovesCalcUtils.isValidMove(position, toMove, board) && MovesCalcUtils.isPieceNull(toMove, board)){
                    addPromotePieces(allMoves, position, toMove);
                }
            }

            // Forward movement
            if(!possiblePromotion){
               toMove = new ChessPosition(position.getBoardRow() + moveOne, position.getBoardColumn());

               if(MovesCalcUtils.isValidMove(position, toMove, board) && MovesCalcUtils.isPieceNull(toMove, board)){
                   allMoves.add(new ChessMove(position, toMove, null));
               }

            }
            return allMoves;
        
    }
}
