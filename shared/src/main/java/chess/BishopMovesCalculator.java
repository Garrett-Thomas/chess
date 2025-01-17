package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCalculator implements PieceMovesCalculator{

    BishopMovesCalculator(){

    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> allMoves = new ArrayList<ChessMove>();
        ChessPiece bishPiece= board.getPiece(position);
        ChessPosition toMove;

        if (bishPiece.getPieceType() != ChessPiece.PieceType.BISHOP) {
            throw new RuntimeException("Piece is not a pawn");
        }



       // Check Right Down Diagonal

       for(int i = 1; (i + position.getBoardColumn() <= 8) && (position.getBoardRow() - i >= 1); i++){
          toMove = new ChessPosition(position.getBoardRow() - i, position.getBoardColumn() + i);


          if(MovesCalcUtils.isValidMove(position, toMove, board)){
              allMoves.add(new ChessMove(position, toMove, null));
          }
            else{
                break;
          }
            if(!MovesCalcUtils.isPieceNull(toMove, board)){
               break;
            }
       }

       // Check Right Up Diagonal
        for(int i = 1; (i + position.getBoardColumn() <= 8) && (i + position.getBoardRow() <= 8); i++){
            toMove = new ChessPosition(position.getBoardRow() + i, position.getBoardColumn() + i);


            if(MovesCalcUtils.isValidMove(position, toMove, board)){
                allMoves.add(new ChessMove(position, toMove, null));
            }
            else{
                break;
            }
            if(!MovesCalcUtils.isPieceNull(toMove, board)){
                break;
            }
        }

        // Left Up Diagonal
        for(int i = 1; (position.getBoardColumn() - i >= 1) && (i + position.getBoardRow() <= 8); i++){
            toMove = new ChessPosition(position.getBoardRow() + i, position.getBoardColumn() - i);


            if(MovesCalcUtils.isValidMove(position, toMove, board)){
                allMoves.add(new ChessMove(position, toMove, null));
            }
            else{
                break;
            }
            if(!MovesCalcUtils.isPieceNull(toMove, board)){
                break;
            }
        }

        // Left Down Diagonal
        for(int i = 1; (position.getBoardColumn() - i >= 1) && (position.getBoardRow() - i >= 1); i++){
            toMove = new ChessPosition(position.getBoardRow() - i, position.getBoardColumn() - i);


            if(MovesCalcUtils.isValidMove(position, toMove, board)){
                allMoves.add(new ChessMove(position, toMove, null));
            }
            else{
                break;
            }
            if(!MovesCalcUtils.isPieceNull(toMove, board)){
                break; } }


        return allMoves;
    }

}
