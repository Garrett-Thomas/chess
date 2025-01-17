package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MovesCalcUtils {

    public static boolean isPieceNull(ChessPosition newPosition, ChessBoard board) {

        ChessPiece otherPiece = board.getPiece(newPosition);

        return otherPiece == null;

    }

    public static boolean isValidMove(ChessPosition currPosition, ChessPosition newPosition, ChessBoard board) {

        ChessPiece currPiece = board.getPiece(currPosition);
        ChessGame.TeamColor selfColor = currPiece.getTeamColor();
        ChessPiece otherPiece = board.getPiece(newPosition);
        if (otherPiece == null) {
            return true;
        }

        return otherPiece.getTeamColor() != selfColor;

    }

    public static Collection<ChessMove> getDiagonalMoves(ChessPosition position, ChessBoard board){
       Collection<ChessMove> allMoves = new ArrayList<>();
        ChessPosition toMove;
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

public static Collection<ChessMove> getCrossMoves(ChessPosition position, ChessBoard board){
    // Check rows going up

        Collection<ChessMove> allMoves = new ArrayList<ChessMove>();
        ChessPosition toMove;
        for(int i = 1; i + position.getBoardRow() <= 8; i++){

        // First if checks if the toMove position has null piece or enemy piece.
        // Else statement breaks if thats not true because it means that there is a friendly piece there
        // Second if breaks if toMove is the position of an enemy piece that we just took.
        toMove = new ChessPosition(position.getBoardRow() + i, position.getBoardColumn());
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

    // Check backwards rows for black, forwards for white
        for(int i = -1; i + position.getBoardRow() >= 1; i--){

        toMove = new ChessPosition(position.getBoardRow() + i, position.getBoardColumn());
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

    // Check left columns for black,white
        for(int i = 1; i + position.getBoardColumn() <= 8; i++){

        toMove = new ChessPosition(position.getBoardRow(), position.getBoardColumn() + i);
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

    // Check right columns for black, white
        for(int i = -1; i + position.getBoardColumn() >= 1; i--){

        toMove = new ChessPosition(position.getBoardRow(), position.getBoardColumn() + i);
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

    return allMoves;
}

}
