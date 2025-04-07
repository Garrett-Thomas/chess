package server;

import chess.ChessGame;
import com.google.gson.Gson;
import dao.SQLAuthDAO;
import dao.SQLGameDAO;
import dao.SQLUserDAO;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;

import java.io.IOException;
import java.util.Timer;

import static websocket.messages.ServerMessage.ServerMessageType.LOAD_GAME;

@WebSocket
public class WebSocketHandler {
    private static SQLUserDAO userDAO = SQLUserDAO.getInstance();
    private static SQLGameDAO gameDAO = SQLGameDAO.getInstance();
    private static SQLAuthDAO authDAO = SQLAuthDAO.getInstance();

    @OnWebSocketMessage
    public void onMessage(Session user, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {

            // Implied that the client has already joined a game via http so just need to find user using token
            case CONNECT -> {
//                String username = authDAO.getUsername(command.getAuthToken());
//                var userData = userDAO.getUser(username);
                var gameList = gameDAO.getGames();
                for (var game : gameList) {
                    // Want to "join" this game
                    if (game.gameID() == command.getGameID()) {
                        user.getRemote().sendString(new Gson().toJson(new LoadGameMessage(LOAD_GAME, new Gson().toJson(game))));
                    }
                }
                break;
            }


            case MAKE_MOVE -> {
                var gameList = gameDAO.getGames();
                var username = authDAO.getUsername(command.getAuthToken());
                var userData = userDAO.getUser(username);
                ChessGame chessGame = null;
                for(var game : gameList){
                    if (game.gameID() == command.getGameID()){
                        chessGame = game.game();
                    }
                }

                if(chessGame == null){
                    throw new IOException("Can't find game");
                }






            }

        }
    }

    @OnWebSocketError
    public void onError(Session session, Throwable error) {
        System.out.printf("%s %s", session.getProtocolVersion(), error.toString());
    }
}
