package server;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dao.GameDAO;
import dao.SQLAuthDAO;
import dao.SQLGameDAO;
import dao.SQLUserDAO;
import dataaccess.*;
import model.GameData;
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
    private final static Gson gson = GsonParent.getInstance();


    @OnWebSocketMessage
    public void onMessage(Session user, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        var username = authDAO.getUsername(command.getAuthToken());
        var gameID = command.getGameID();

        switch (command.getCommandType()) {

            // Implied that the client has already joined a game via http so just need to find user using token
            case CONNECT -> {
                var gameList = gameDAO.getGames();
                for (var game : gameList) {

                    // Want to "join" this game
                    if (game.gameID() == command.getGameID()) {
                        user.getRemote().sendString(new Gson().toJson(new LoadGameMessage(LOAD_GAME, new Gson().toJson(game))));
                        var sockConnection = new SockConnection(username, user);
                        connections.addConnection(game.gameID(), sockConnection);
                    }
                }
            }


            case MAKE_MOVE -> {

                var moveCommand = GsonParent.getInstance().fromJson(message, MakeMoveCommand.class);
                var gameList = gameDAO.getGames();

                ChessGame.TeamColor teamColor = null;

                GameData gameData = null;
                ChessGame chessGame = null;

                for (var game : gameList) {
                    if (game.gameID() == moveCommand.getGameID()) {
                        chessGame = game.game();
                        gameData = game;
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


                    // Add logic to check for checkmate, stalemate etc
                    chessGame.makeMove(moveCommand.getMove());

                    var updatedGame = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), chessGame);

                    gameDAO.updateGame(updatedGame, gameID);

                    String loadGame = new Gson().toJson(new LoadGameMessage(LOAD_GAME, gameJson));
                    connections.broadcast(null, gameID, loadGame);

                    String notify = String.format("Player %s moved a piece!", username);
                    connections.broadcast(Collections.singleton(username), gameID, gson.toJson(new NotificationMessage(NOTIFICATION, notify)));
                } catch (InvalidMoveException e) {
                    throw new RuntimeException(e);
                }
                // If the db update fails
                catch (ServiceException e) {
                    throw new RuntimeException(e);
                }

            }
            case LEAVE -> {
                String note = String.format("Player %s has left", username);
                var notification = gson.toJson(new NotificationMessage(NOTIFICATION, note));
//                gameDAO.
                connections.broadcast(Collections.singleton(username), gameID, notification);
                connections.disconnect(gameID, username);
                connections.removeConnection(gameID, username);

            }

            case RESIGN -> {
                String note = String.format("Player %s has resigned", username);
                var notification = gson.toJson(new NotificationMessage(NOTIFICATION, note));
                connections.broadcast(Collections.singleton(username), gameID, notification);
                connections.disconnect(gameID, username);
                connections.removeConnection(gameID, username);


            }
        }
    }

    @OnWebSocketError
    public void onError(Session session, Throwable error) {
        System.out.printf("%s %s", session.getProtocolVersion(), error.toString());
    }
}
