package dao;

import dataaccess.ServiceException;
import model.GameData;

import java.util.*;

public class MemoryGameDAO implements GameDAO {

    private static GameDAO gameDAO = null;

    private static Map<Integer, GameData> gameData = new HashMap<>();


    MemoryGameDAO() {
    }

    @Override
    public ArrayList<GameData> getGames() {
        return new ArrayList<>(gameData.values());
    }

    @Override
    public Integer createGame(String gameName) {

        Integer gameID = Math.abs(UUID.randomUUID().hashCode());
        gameData.put(gameID, new GameData(gameID, null, null, gameName, null));

        return gameID;
    }

    @Override
    public void joinGame(String playerName, String playerColor, int gameID) throws Exception {

        GameData game = gameData.get(gameID);
        String blackUsername = game.blackUsername();
        String whiteUsername = game.whiteUsername();

        boolean isBlack = Objects.equals(playerColor, "BLACK");
        boolean isWhite =  Objects.equals(playerColor, "WHITE");
        GameData updatedGame;

        if (blackUsername != null && whiteUsername != null) {
            throw new ServiceException(403, "Game already taken");
        }

        if (isBlack && blackUsername == null) {
            updatedGame = new GameData(game.gameID(), game.whiteUsername(), playerName, game.gameName(), game.game());
        } else if (isWhite && whiteUsername == null) {
            updatedGame = new GameData(game.gameID(), playerName, game.blackUsername(), game.gameName(), game.game());

        }
        else if((isBlack && !blackUsername.isEmpty()) || isWhite && !whiteUsername.isEmpty()){

            throw new ServiceException(403, "Color already taken");
        }
        else {
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
