package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    TeamColor teamTurn;
    ChessBoard board;
    public ChessGame() {
    this.teamTurn = TeamColor.WHITE;
    this.board = new ChessBoard();
    }

    private Collection<ChessPosition> getEnemyPositions(TeamColor enemyColor){

        Collection<ChessPosition> enemyPos = new ArrayList<>();
        for(int i = 1; i < 9; i++){
            for(int j = 1 ; j < 9; j++){
               ChessPosition pos = new ChessPosition(i, j);

               if(this.board.getPiece(pos) != null && this.board.getPiece(pos).getTeamColor() == enemyColor){
                  enemyPos.add(pos);
               }
            }
        }

       return enemyPos;
    }
    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */

    public Collection<ChessMove> validMoves(ChessPosition startPosition) {

        // This is mostly equivalent to piece moves - all moves that put the King in a
        // position to be killed
        // I could iterate over all piece moves of each piece when I chang the position of the currPiece
        // Then I simply check if the Kings position is part of the set and if it is then I can't do it

        ChessPiece currPiece = this.board.getPiece(startPosition);
        if(currPiece == null) return null;

        TeamColor currColor = currPiece.getTeamColor();
        ChessPosition teamKingPos = this.board.getKing(currColor);


           Collection<ChessMove> possibleMoves = currPiece.pieceMoves(this.board, startPosition);
            Collection<ChessMove> validatedMoves = new ArrayList<>();


            // I really want to move a piece, see if the other pieces can attack the king then throw away that move
            for(ChessMove move : possibleMoves){
                ChessBoard temp = new ChessBoard(this.board);
                boolean moveMade = temp.movePiece(move);

                if(!moveMade) continue;

                boolean res = temp.isValidBoard(teamKingPos);
                if(res){
                    validatedMoves.add(move);
                }
            }

    return validatedMoves;

    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
