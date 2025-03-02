package server;

import dataaccess.DataAccessException;
import model.GameData;

import java.util.ArrayList;

public interface GameDAO {


    public ArrayList<GameData> getGames();

    public String createGame(String gameName);

    public String joinGame(String authData, String playerColor, String gameID) throws Exception;
}
