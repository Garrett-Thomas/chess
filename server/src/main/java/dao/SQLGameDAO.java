package dao;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DbUtils;
import dataaccess.ServiceException;
import model.GameData;
import utils.GsonParent;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class SQLGameDAO implements GameDAO {


    private static final String GET_GAMES_STRINGS = """
            SELECT * FROM games;
            """;
    private static final String CREATE_GAME_STRING = """
                INSERT INTO games (gameID, game) VALUES (?,?) 
            """;

    private static final String GET_GAME_BY_ID_STRING = """
            SELECT * FROM games WHERE gameID = ? LIMIT 1
            """;

    private static final String UPDATE_GAME_STRING = """
            UPDATE games SET game = ? WHERE gameID = ?
            """;

    private static final String CLEAR_TABLE_STRING = """
            TRUNCATE TABLE games 
            """;

    private final Gson gson = GsonParent.getInstance();
    private static SQLGameDAO sqlGameDAO = null;


    public static SQLGameDAO getInstance() {
        if (sqlGameDAO == null) {
            sqlGameDAO = new SQLGameDAO();
        }
        return sqlGameDAO;
    }

    public void updateGame(GameData game, int gameId) throws ServiceException {
        DbUtils.executeUpdate(UPDATE_GAME_STRING, gson.toJson(game), gameId);
    }


    @Override
    public ArrayList<GameData> getGames() {

        ArrayList<GameData> res = new ArrayList<>();
        try {
            var games = DbUtils.executeQuery(GET_GAMES_STRINGS);

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

            DbUtils.executeUpdate(CREATE_GAME_STRING, gameID, gameJson);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return gameID;
    }

    @Override
    public void joinGame(String playerName, String playerColor, int gameID) throws ServiceException {

        GameData game;
        try {

            var res = DbUtils.executeQuery(GET_GAME_BY_ID_STRING, gameID);

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

        var gameJson = gson.toJson(updatedGame);
        DbUtils.executeUpdate(UPDATE_GAME_STRING, gameJson, gameID);
    }


    @Override
    public void clear() {
        try {

            DbUtils.executeUpdate(CLEAR_TABLE_STRING);
        } catch (ServiceException e) {
            System.err.println(e.getMessage());
        }
    }
}
