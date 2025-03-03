package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
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
        this.board.resetBoard();
    }

    private Collection<ChessPosition> getEnemyPositions(TeamColor enemyColor) {

        Collection<ChessPosition> enemyPos = new ArrayList<>();
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition pos = new ChessPosition(i, j);

                if (this.board.getPiece(pos) != null && this.board.getPiece(pos).getTeamColor() == enemyColor) {
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
        if (currPiece == null) {
            return new ArrayList<>();
        }

        TeamColor currColor = currPiece.getTeamColor();
        ChessPosition teamKingPos = this.board.getKing(currColor);


        Collection<ChessMove> possibleMoves = currPiece.pieceMoves(this.board, startPosition);
        Collection<ChessMove> validatedMoves = new ArrayList<>();


        // Need to only allow moves that don't move king into check

        // I really want to move a piece, see if the other pieces can attack the king then throw away that move
        for (ChessMove move : possibleMoves) {
            ChessBoard testBoard = new ChessBoard(this.board);
            boolean moveMade = testBoard.movePiece(move);

            if (!moveMade) {
                continue;
            }

            boolean res;

            // Special rules apply to king
            if (currPiece.getPieceType() == ChessPiece.PieceType.KING) {

                res = testBoard.isValidBoard(move.getEndPosition());
            } else {

                res = testBoard.isValidBoard(teamKingPos);
            }
            if (res) {
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
        ChessPiece currPiece = this.board.getPiece(move.getStartPosition());


        if (currPiece == null || currPiece.getTeamColor() != this.teamTurn) {
            throw new InvalidMoveException();
        }

        if (!validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException();
        }
        if (!this.board.movePiece(move)) {
            throw new InvalidMoveException();
        }

        setTeamTurn(this.teamTurn == TeamColor.BLACK ? TeamColor.WHITE : TeamColor.BLACK);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        ChessPosition king = this.board.getKing(teamColor);

        Collection<ChessMove> kingMoves = validMoves(king);

        if (isInCheckmate(teamColor)) {
            return true;
        }

        if (kingMoves.isEmpty()) {
            return false;
        }

        Collection<ChessPosition> enemyPos = getEnemyPositions(teamColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);

        for (ChessPosition pos : enemyPos) {
            Collection<ChessMove> posMoves = validMoves(pos);

            Predicate<ChessMove> moveEndIsKing = move -> (move.getEndPosition().equals(king));
            BiPredicate<ChessMove, ChessMove> compKing = (kingMove, move) -> kingMove.getEndPosition().equals(move.getStartPosition());
            Predicate<ChessMove> intermed = move -> kingMoves.stream().noneMatch(kingMove -> compKing.test(kingMove, move));
            Predicate<ChessMove> kingEndIsStart = move -> moveEndIsKing.test(move) && intermed.test(move);

            var res = posMoves.stream().anyMatch(kingEndIsStart);
            if (res) {
                return true;
            }
        }

        return false;
        // Need to see if the king can move and if he is included in at least one of the opponents valid moves

    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */

    public boolean isInCheckmate(TeamColor teamColor) {

        ChessPosition king = this.board.getKing(teamColor);

        Collection<ChessMove> kingMoves = validMoves(king);


        // If no valid king moves then its in checkmate
        if (!kingMoves.isEmpty()) {
            return false;
        }


        // Gets all enemy positions
        Collection<ChessPosition> enemyPos = getEnemyPositions(teamColor == TeamColor.WHITE ? TeamColor.BLACK : TeamColor.WHITE);

        // Gets all friendly positions
        Collection<ChessPosition> friendlyPos = getEnemyPositions(teamColor);

        Collection<ChessMove> friendlyMoves = new ArrayList<>();

        // Calculates all possible moves friendly pieces can make
        for (ChessPosition pos : friendlyPos) {
            friendlyMoves.addAll(validMoves(pos));
        }

        Collection<ChessMove> enemyMoves = new ArrayList<>();

        for (ChessPosition pos : enemyPos) {
            // Get all moves that have the King as their end position

            Predicate<ChessMove> getMov = move -> move.getEndPosition().equals(king);

            enemyMoves.addAll(validMoves(pos).stream().filter(getMov).collect(Collectors.toCollection(ArrayList::new)));
        }

        // AKA initially the king can't move but no other piece can capture him
        if (enemyMoves.isEmpty()) {
            return false;
        }


        Predicate<ChessMove> canKill = move -> enemyMoves.stream().anyMatch(enemyMove -> move.getEndPosition().equals(enemyMove.getStartPosition()));

        // Now this contains only the moves that can take the pieces that can take the king
        friendlyMoves = friendlyMoves.stream().filter(canKill).collect(Collectors.toCollection(ArrayList::new));


        // Now need to iterate through friendly moves by making the move, then seeing if that leads to a checkmate
        for (ChessMove friendlyMove : friendlyMoves) {

            ChessBoard testBoard = new ChessBoard(this.board);


            testBoard.movePiece(friendlyMove);

            ChessGame testGame = new ChessGame();
            testGame.setBoard(testBoard);

            if (!testGame.validMoves(king).isEmpty()) {
                return false;
            }


        }


        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

        // Mutually exclusive check and stalemate states
        if (isInCheck(TeamColor.WHITE) || isInCheck(TeamColor.BLACK)) {
            return false;
        }


        Collection<ChessPosition> allMoves = getEnemyPositions(teamColor);

        ArrayList<Collection<ChessMove>> res = (allMoves.stream().map(this::validMoves)).collect(Collectors.toCollection(ArrayList::new));

        // If every piece has no moves then in stalemate
        return res.stream().allMatch(Collection::isEmpty);

    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {

        // This ensures that we have an empty ChessBoard
        this.board = new ChessBoard();
        this.board = new ChessBoard(board);
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
