package dao;

import chess.ChessGame;
import chess.PieceMovesCalculator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataaccess.DbUtils;
import dataaccess.ServiceException;
import model.GameData;
import utils.GameSerializer;
import utils.GsonParent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class SQLGameDAO implements GameDAO {

    private static final String getGamesStrings = """
            SELECT * FROM games;
            """;
    private static final String createGameString = """
                INSERT INTO games (gameID, game) VALUES (?,?) 
            """;

    private static final String getGameByIdString = """
            SELECT * FROM games WHERE gameID = ? LIMIT 1
            """;

    private static final String updateGameString = """
            UPDATE games SET game = ? WHERE gameID = ?
            """;

    private static final String clearTableString = """
            TRUNCATE TABLE games 
            """;

    private final Gson gson = GsonParent.getInstance();

    @Override
    public ArrayList<GameData> getGames() {

        ArrayList<GameData> res = new ArrayList<>();
        try {
            var games = DbUtils.executeQuery(getGamesStrings);

            while (games.next()) {
                var gameJson = games.getString("game");
                res.add(gson.fromJson(gameJson, GameData.class));

            }

            return res;

        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ArrayList<>();

        }

    }

    @Override
    public Integer createGame(String gameName) {
        Integer gameID = Math.abs(UUID.randomUUID().hashCode());

        var game = new GameData(gameID, null, null, gameName, new ChessGame());
        var gameJson = gson.toJson(game);
        try {

            DbUtils.executeUpdate(createGameString, gameID, gameJson);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return gameID;
    }

    @Override
    public void joinGame(String playerName, String playerColor, int gameID) throws ServiceException {

        GameData game;
        try {

            var res = DbUtils.executeQuery(getGameByIdString, gameID);

            if (res.next()) {
                game = gson.fromJson(res.getString("game"), GameData.class);
            } else {
                throw new ServiceException(400, "gameID does not exist");
            }
        } catch (SQLException e) {
            throw new ServiceException(500, e.getMessage());
        }
        String blackUsername = game.blackUsername();
        String whiteUsername = game.whiteUsername();

        boolean isBlack = Objects.equals(playerColor, "BLACK");
        boolean isWhite = Objects.equals(playerColor, "WHITE");
        GameData updatedGame;

        if (blackUsername != null && whiteUsername != null) {
            throw new ServiceException(403, "Game already taken");
        }

        if (isBlack && blackUsername == null) {
            updatedGame = new GameData(game.gameID(), game.whiteUsername(), playerName, game.gameName(), game.game());
        } else if (isWhite && whiteUsername == null) {
            updatedGame = new GameData(game.gameID(), playerName, game.blackUsername(), game.gameName(), game.game());

        } else if ((isBlack && !blackUsername.isEmpty()) || isWhite && !whiteUsername.isEmpty()) {

            throw new ServiceException(403, "Color already taken");
        } else {
            throw new ServiceException(400, "playerColor not equal to black or white");
        }

        var gameJson = new Gson().toJson(updatedGame);
        DbUtils.executeUpdate(updateGameString, gameJson, gameID);
    }


    @Override
    public void clear() {
        try {

            DbUtils.executeUpdate(clearTableString);
        } catch (ServiceException e) {
            System.err.println(e.getMessage());
        }
    }
}
