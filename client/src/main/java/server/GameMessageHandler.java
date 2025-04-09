package server;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import ui.GamePlay;
import ui.StringUtils;
import utils.GsonParent;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public class GameMessageHandler {

    private static Gson gson = GsonParent.getInstance();

    public static void handleMessage(String msg) {
        var command = gson.fromJson(msg, ServerMessage.class);

        switch (command.getServerMessageType()) {
            case LOAD_GAME -> {
                var gameCommand = gson.fromJson(msg, LoadGameMessage.class);


                // Lol this is a gameData string
                var gameData = gson.fromJson(gameCommand.getGame(), GameData.class);
                GamePlay.setGame(gameData.game());
                GamePlay.drawBoard();
            }

            case NOTIFICATION -> {

                var note = gson.fromJson(msg, NotificationMessage.class);
                var styledNote = StringUtils.getPositiveString(note.message);
                System.out.println(styledNote);
            }

            case ERROR -> {
                var error = gson.fromJson(msg, ErrorMessage.class);
                var styledError = StringUtils.getNegativeString(error.errorMessage);
                System.out.println(styledError);
            }

        }


    }


}
