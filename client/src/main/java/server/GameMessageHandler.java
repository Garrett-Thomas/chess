package server;

import chess.ChessGame;
import com.google.gson.Gson;
import ui.GamePlay;
import utils.GsonParent;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

public class GameMessageHandler {

    private static Gson gson = GsonParent.getInstance();

    public static void handleMessage(String msg) {
        var command = gson.fromJson(msg, ServerMessage.class);

        switch (command.getServerMessageType()) {
            case LOAD_GAME -> {
                var gameCommand = gson.fromJson(msg, LoadGameMessage.class);
                GamePlay.setGame(gson.fromJson(gameCommand.getGame(), ChessGame.class));
                GamePlay.drawBoard();
//                System.out.println("loading game...");
            }
            case NOTIFICATION -> {
                System.out.println("Notification");
            }
            case ERROR -> {
                System.out.println("Error");
            }


        }


    }


}
