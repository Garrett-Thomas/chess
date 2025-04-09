package utils;

import chess.ChessPosition;

public class CoordinateParser {


    public static String parseCoordinates(ChessPosition pos) {


        var col = pos.getColumn();
        var row = pos.getRow();
        var colStr = (char) (col - 1 + 'a');
        var rowStr = row + "";

        return colStr + rowStr;
    }
}
