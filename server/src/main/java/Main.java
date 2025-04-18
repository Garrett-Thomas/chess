import chess.*;
import server.Server;
import spark.Spark;

import static spark.Spark.webSocket;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);

        Server server = new Server();

        server.run(8000);
    }
}