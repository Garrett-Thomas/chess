package dao;

import dataaccess.ServiceException;
import model.GameData;

import java.util.*;

public class MemoryGameDAO implements GameDAO {

    private static GameDAO gameDAO = null;

    private final Map<String, GameData> gameData;


    MemoryGameDAO() {
        this.gameData = new HashMap<>();
    }

    @Override
    public ArrayList<GameData> getGames() {
        return new ArrayList<>(this.gameData.values());
    }

    @Override
    public String createGame(String gameName) {
        String gameID = UUID.randomUUID().toString();
        this.gameData.put(gameID, new GameData(gameID, null, null, gameName, null));

        return gameID;
    }

    @Override
    public void joinGame(String playerName, String playerColor, String gameID) throws Exception {

        GameData game = this.gameData.get(gameID);
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
        this.gameData.put(game.gameID(), updatedGame);
    }

    public void clear(){
        gameDAO = new MemoryGameDAO();
    }

    public static synchronized GameDAO getInstance() {
        if (gameDAO == null) {
            gameDAO = new MemoryGameDAO();
        }
        return gameDAO;
    }
}
