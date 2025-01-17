package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCalculator implements PieceMovesCalculator{
    RookMovesCalculator(){

    }

    private boolean IsPieceNull(ChessPosition newPosition, ChessBoard board) {

        ChessPiece otherPiece = board.getPiece(newPosition);

        return otherPiece == null;

    }

    private boolean isValidMove(ChessPosition currPosition, ChessPosition newPosition, ChessBoard board) {

        ChessPiece currPiece = board.getPiece(currPosition);
        ChessGame.TeamColor selfColor = currPiece.getTeamColor();
        ChessPiece otherPiece = board.getPiece(newPosition);
        if (otherPiece == null) {
            return true;
        }

        return otherPiece.getTeamColor() != selfColor;

    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> allMoves = new ArrayList<ChessMove>();

        ChessPiece rookPiece = board.getPiece(position);
        ChessPosition toMove;
        if(rookPiece.getPieceType() != ChessPiece.PieceType.ROOK){
            throw new RuntimeException("Piece is not a rook");
        }

        // Check rows going up
        for(int i = 1; i + position.getBoardRow() <= 8; i++){


            // First if checks if the toMove position has null piece or enemy piece.
            // Else statement breaks if thats not true because it means that there is a friendly piece there
            // Second if breaks if toMove is the position of an enemy piece that we just took.
           toMove = new ChessPosition(position.getBoardRow() + i, position.getBoardColumn());
            if(isValidMove(position, toMove, board)){
            allMoves.add(new ChessMove(position, toMove, null));
            }
            else{
                break;
            }

            if(!IsPieceNull(toMove, board)){
                break;
            }
        }

        // Check backwards rows for black, forwards for white
        for(int i = -1; i + position.getBoardRow() >= 1; i--){

            toMove = new ChessPosition(position.getBoardRow() + i, position.getBoardColumn());
            if(isValidMove(position, toMove, board)){
                allMoves.add(new ChessMove(position, toMove, null));
            }
            else{
                break;
            }
            if(!IsPieceNull(toMove, board)){
                break;
            }
        }

        // Check left columns for black,white
        for(int i = 1; i + position.getBoardColumn() <= 8; i++){

            toMove = new ChessPosition(position.getBoardRow(), position.getBoardColumn() + i);
            if(isValidMove(position, toMove, board)){
                allMoves.add(new ChessMove(position, toMove, null));
            }
            else{
                break;
            }
            if(!IsPieceNull(toMove, board)){
                break;
            }
        }

        // Check right columns for black, white
        for(int i = -1; i + position.getBoardColumn() >= 1; i--){

            toMove = new ChessPosition(position.getBoardRow(), position.getBoardColumn() + i);
            if(isValidMove(position, toMove, board)){
                allMoves.add(new ChessMove(position, toMove, null));
            }
            else{
                break;
            }
            if(!IsPieceNull(toMove, board)){
                break;
            }
        }



        return allMoves;

    }
}
