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
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import utils.GsonParent;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
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

    private GameData getGameByID(Integer gameID) throws DataAccessException {
        var gameList = gameDAO.getGames();
        for (var game : gameList) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        throw new DataAccessException("No game exists with that ID");
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) throws IOException {

        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            var username = authDAO.getUsername(command.getAuthToken());
            if (username == null) {
                throw new ServiceException(401, "Bad token");
            }
            var gameID = command.getGameID();
            var game = getGameByID(gameID);

            switch (command.getCommandType()) {

                // Implied that the client has already joined a game via http so just need to find user using token
                case CONNECT -> {
                    user.getRemote().sendString(new Gson().toJson(new LoadGameMessage(LOAD_GAME, new Gson().toJson(game))));
                    var sockConnection = new SockConnection(username, user);
                    connections.addConnection(game.gameID(), sockConnection);

                    String colorOrObserver = "";
                    if (Objects.equals(game.whiteUsername(), username)) {
                        colorOrObserver = "White";
                    } else if (Objects.equals(game.blackUsername(), username)) {
                        colorOrObserver = "Black";
                    } else {
                        colorOrObserver = "an observer";
                    }

                    String notify = String.format("%s joined as %s", username, colorOrObserver);

                    String notification = gson.toJson(new NotificationMessage(notify));

                    connections.broadcast(Collections.singleton(username), gameID, notification);

                }


                case MAKE_MOVE -> {
                    var moveCommand = GsonParent.getInstance().fromJson(message, MakeMoveCommand.class);
                    ChessGame.TeamColor teamColor = null;

                    GameData gameData = null;
                    ChessGame chessGame = null;
                    chessGame = game.game();
                    gameData = game;

                    if (chessGame.isGameOver()) {
                        throw new InvalidMoveException("Cannot make move while game is over");
                    }

                    if (game.blackUsername() == null || game.whiteUsername() == null) {
                        throw new RuntimeException("Game has not started yet");
                    }
                    if (game.blackUsername().equals(username)) {
                        teamColor = ChessGame.TeamColor.BLACK;
                    } else if (game.whiteUsername().equals(username)) {
                        teamColor = ChessGame.TeamColor.WHITE;
                    }

                    if (chessGame == null) {
                        throw new IOException("Can't find game");
                    }


                    if (chessGame.getTeamTurn() != teamColor) {
                        throw new InvalidMoveException("Wrong team turn");
                    }


                    chessGame.makeMove(moveCommand.getMove());

                    String note = null;
                    if (chessGame.isInCheck(ChessGame.TeamColor.BLACK) || chessGame.isInCheck(ChessGame.TeamColor.WHITE)) {
                        note = "Game in check";
                    }

                    if (chessGame.isInCheckmate(ChessGame.TeamColor.BLACK) || chessGame.isInCheckmate(ChessGame.TeamColor.WHITE)) {
                        note = "Game in checkmate";
                        chessGame.setGameOver(true);
                    }
                    if (chessGame.isInStalemate(ChessGame.TeamColor.WHITE) || chessGame.isInStalemate(ChessGame.TeamColor.BLACK)) {
                        note = "Game in stalemate";
                        chessGame.setGameOver(true);
                    }
                    var updatedGame = new GameData(gameID, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), chessGame);
                    gameDAO.updateGame(updatedGame, gameID);

                    String loadGame = new Gson().toJson(new LoadGameMessage(LOAD_GAME, gson.toJson(updatedGame)));
                    connections.broadcast(null, gameID, loadGame);

                    String notify = String.format("Player %s made this move: %s!", username, moveCommand.getMove().toString());
                    connections.broadcast(Collections.singleton(username), gameID, gson.toJson(new NotificationMessage(notify)));


                    if (note != null) {
                        connections.broadcast(null, gameID, gson.toJson(new NotificationMessage(note)));
                    }

                }
                case LEAVE -> {

                    var updatedGame = removePlayer(game, username, false);
                    gameDAO.updateGame(updatedGame, gameID);
                    String note = String.format("Player %s has left", username);
                    var notification = gson.toJson(new NotificationMessage(note));
                    connections.broadcast(Collections.singleton(username), gameID, notification);
                    connections.disconnect(gameID, username);
                    connections.removeConnection(gameID, username);


                }

                case RESIGN -> {

                    if (!Objects.equals(game.whiteUsername(), username) && !Objects.equals(game.blackUsername(), username)) {
                        throw new ServiceException(401, "Cannot resign while an observer");
                    }

                    // Implies that someone has already resigned
                    if (game.whiteUsername() == null || game.blackUsername() == null) {
                        throw new ServiceException(400, "Cannot resign");
                    }

                    var updatedGame = removePlayer(game, username, true);
                    gameDAO.updateGame(updatedGame, gameID);

                    String note = String.format("Player %s has resigned", username);
                    var notification = gson.toJson(new NotificationMessage(note));

                    connections.broadcast(null, gameID, notification);
                    connections.disconnect(gameID, username);
                    connections.removeConnection(gameID, username);


                }
            }

        } catch (IOException | InvalidMoveException | DataAccessException | ServiceException e) {

            String err = "Error: " + e.getMessage();

            String msg = gson.toJson(new ErrorMessage(err));
            user.getRemote().sendString(msg);

        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }

    public GameData removePlayer(GameData game, String username, boolean isOver) throws DataAccessException {
        GameData updatedGame = null;
        ChessGame.TeamColor leavingColor = null;
        var chessGame = game.game();

        if (isOver) {
            chessGame.setGameOver(true);
        }

        if (Objects.equals(game.whiteUsername(), username)) {
            leavingColor = ChessGame.TeamColor.WHITE;
        }
        if (Objects.equals(game.blackUsername(), username)) {
            leavingColor = ChessGame.TeamColor.BLACK;
        }

        if (leavingColor != null) {
            switch (leavingColor) {
                case WHITE -> {
                    updatedGame = new GameData(game.gameID(), null, game.blackUsername(), game.gameName(), game.game());
                }
                case BLACK -> {
                    updatedGame = new GameData(game.gameID(), game.whiteUsername(), null, game.gameName(), game.game());
                }
            }

            return updatedGame;
        } else {
            throw new DataAccessException("Cannot remove player " + username);
        }
    }
}
