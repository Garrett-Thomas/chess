package chess;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;

public class PieceUtils {

    public static boolean isValidMove(ChessBoard board, ChessMove toMove) {
        ChessPiece currPiece = board.getPiece(toMove.getStartPosition());
        ChessPosition endPos = toMove.getEndPosition();
        // Out of bounds
        if (endPos.getColumn() > 8 || endPos.getColumn() < 1 || endPos.getRow() > 8 || endPos.getRow() < 1) {
            return false;
        }
        ChessPiece endPiece = board.getPiece(endPos);
        if (endPiece == null) {
            return true;
        }
        return endPiece.getTeamColor() != currPiece.getTeamColor();

    }

    public static boolean isTakeMove(ChessBoard board, ChessMove toMove) {
        ChessPiece currPiece = board.getPiece(toMove.getStartPosition());
        ChessPiece endPiece = board.getPiece(toMove.getEndPosition());

        if (endPiece == null) {
            return false;
        }

        return endPiece.getTeamColor() != currPiece.getTeamColor();

    }

    public static Collection<ChessMove> getDiagonalMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> allMoves = new ArrayList<>();

        // Check right down diagonal
        int i = 1;
        ChessMove toMove = new ChessMove(pos, new ChessPosition(pos.getRow() - i, pos.getColumn() + i), null);
        while (isValidMove(board, toMove)) {
            allMoves.add(toMove);
            if (isTakeMove(board, toMove)) {
                break;
            }
            i++;
            toMove = new ChessMove(pos, new ChessPosition(pos.getRow() - i, pos.getColumn() + i), null);
        }

        // Check right up diagonal
        i = 1;
        toMove = new ChessMove(pos, new ChessPosition(pos.getRow() + i, pos.getColumn() + i), null);
        while (isValidMove(board, toMove)) {
            allMoves.add(toMove);
            if (isTakeMove(board, toMove)) {
                break;
            }
            i++;
            toMove = new ChessMove(pos, new ChessPosition(pos.getRow() + i, pos.getColumn() + i), null);
        }

        // Check left up diagonal
        i = 1;
        toMove = new ChessMove(pos, new ChessPosition(pos.getRow() + i, pos.getColumn() - i), null);
        while (isValidMove(board, toMove)) {
            allMoves.add(toMove);
            if (isTakeMove(board, toMove)) {
                break;
            }
            i++;
            toMove = new ChessMove(pos, new ChessPosition(pos.getRow() + i, pos.getColumn() - i), null);
        }

        // Check left down diagonal
        i = 1;
        toMove = new ChessMove(pos, new ChessPosition(pos.getRow() - i, pos.getColumn() - i), null);
        while (isValidMove(board, toMove)) {
            allMoves.add(toMove);
            if (isTakeMove(board, toMove)) {
                break;
            }
            i++;
            toMove = new ChessMove(pos, new ChessPosition(pos.getRow() - i, pos.getColumn() - i), null);
        }

        return allMoves;
    }

    public static Collection<ChessMove> getCrossMoves(ChessBoard board, ChessPosition pos) {
        Collection<ChessMove> allMoves = new ArrayList<>();


        // Check right

        int i = pos.getColumn() + 1;
        ChessMove toMove = new ChessMove(pos, new ChessPosition(pos.getRow(), i), null);
        while (isValidMove(board, toMove)) {
            allMoves.add(toMove);
            if (isTakeMove(board, toMove)) {
                break;
            }
            toMove = new ChessMove(pos, new ChessPosition(pos.getRow(), ++i), null);
        }
        // Check Left

        i = pos.getColumn() - 1;
        toMove = new ChessMove(pos, new ChessPosition(pos.getRow(), i), null);
        while (isValidMove(board, toMove)) {
            allMoves.add(toMove);
            if (isTakeMove(board, toMove)) {
                break;
            }
            toMove = new ChessMove(pos, new ChessPosition(pos.getRow(), --i), null);
        }


        // Check Down
        i = pos.getRow() - 1;
        toMove = new ChessMove(pos, new ChessPosition(i, pos.getColumn()), null);
        while (isValidMove(board, toMove)) {
            allMoves.add(toMove);
            if (isTakeMove(board, toMove)) {
                break;
            }
            toMove = new ChessMove(pos, new ChessPosition(--i, pos.getColumn()), null);
        }


        // Check Up

        i = pos.getRow() + 1;
        toMove = new ChessMove(pos, new ChessPosition(i, pos.getColumn()), null);
        while (isValidMove(board, toMove)) {
            allMoves.add(toMove);
            if (isTakeMove(board, toMove)) {
                break;
            }
            toMove = new ChessMove(pos, new ChessPosition(++i, pos.getColumn()), null);
        }


        return allMoves;
    }


}
