package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dao.SQLAuthDAO;
import dao.SQLGameDAO;
import dao.SQLUserDAO;
import dataaccess.ConnectionManager;
import dataaccess.DatabaseManager;
import dataaccess.DbUtils;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import utils.GsonParent;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.Timer;

import static websocket.messages.ServerMessage.ServerMessageType.LOAD_GAME;
import static websocket.messages.ServerMessage.ServerMessageType.NOTIFICATION;

@WebSocket
public class WebSocketHandler {
    private static SQLUserDAO userDAO = SQLUserDAO.getInstance();
    private static SQLGameDAO gameDAO = SQLGameDAO.getInstance();
    private static SQLAuthDAO authDAO = SQLAuthDAO.getInstance();
    private final static ConnectionManager connections = new ConnectionManager();

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

                var moveCommand = GsonParent.getInstance().fromJson(message, MakeMoveCommand.class);
                var gameList = gameDAO.getGames();
                var username = authDAO.getUsername(moveCommand.getAuthToken());
                var gameID = moveCommand.getGameID();

                ChessGame.TeamColor teamColor = null;
                ChessGame chessGame = null;

                for (var game : gameList) {
                    if (game.gameID() == moveCommand.getGameID()) {
                        chessGame = game.game();

                        if (game.blackUsername() == null || game.whiteUsername() == null) {
                            throw new RuntimeException("Game has not started yet");
                        }
                        if (game.blackUsername().equals(username)) {
                            teamColor = ChessGame.TeamColor.BLACK;
                        } else if (game.whiteUsername().equals(username)) {
                            teamColor = ChessGame.TeamColor.WHITE;
                        }
                    }
                }

                if (chessGame == null) {
                    throw new IOException("Can't find game");
                }

                try {


                    if (chessGame.getTeamTurn() != teamColor) {
                        throw new RuntimeException("Wrong team turn");
                    }


                    chessGame.makeMove(moveCommand.getMove());
                    String loadGame = new Gson().toJson(new LoadGameMessage(LOAD_GAME, new Gson().toJson(chessGame)));
                    connections.broadcast(null, gameID, loadGame);

                    String notify = String.format("Player %s moved a piece!", username);
                    connections.broadcast(Collections.singleton(username), gameID, new Gson().toJson(new NotificationMessage(NOTIFICATION, notify)));
                } catch (InvalidMoveException e) {
                    throw new RuntimeException(e);
                }

            }

        }
    }

    @OnWebSocketError
    public void onError(Session session, Throwable error) {
        System.out.printf("%s %s", session.getProtocolVersion(), error.toString());
    }
}
