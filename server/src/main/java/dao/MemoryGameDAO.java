package dao;

import dataaccess.ServiceException;
import model.GameData;

import java.util.*;

public class MemoryGameDAO implements GameDAO {

    private static GameDAO gameDAO = null;

    private static Map<String, GameData> gameData = new HashMap<>();


    MemoryGameDAO() {
    }

    @Override
    public ArrayList<GameData> getGames() {
        return new ArrayList<>(gameData.values());
    }

    @Override
    public String createGame(String gameName) {
        String gameID = UUID.randomUUID().toString();
        gameData.put(gameID, new GameData(gameID, null, null, gameName, null));

        return gameID;
    }

    @Override
    public void joinGame(String playerName, String playerColor, String gameID) throws Exception {

        GameData game = gameData.get(gameID);
        GameData updatedGame;

        if (game == null) {
            throw new ServiceException(400, "Game does not exist");
        }

        if (game.blackUsername() != null && game.whiteUsername() != null) {
            throw new ServiceException(403, "Game already taken");
        }

        if (Objects.equals(playerColor, "BLACK")) {
            updatedGame = new GameData(game.gameID(), game.whiteUsername(), playerName, game.gameName(), game.game());
        } else if (Objects.equals(playerColor, "WHITE")) {
            updatedGame = new GameData(game.gameID(), playerName, game.blackUsername(), game.gameName(), game.game());
        } else {
            throw new ServiceException(400, "playerColor not equal to black or white");
        }
        gameData.put(game.gameID(), updatedGame);
    }

    public void clear(){
        gameDAO = new MemoryGameDAO();
        gameData = new HashMap<>();
    }

    public static synchronized GameDAO getInstance() {
        if (gameDAO == null) {
            gameDAO = new MemoryGameDAO();
        }
        return gameDAO;
    }
}
