package dao;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {


    public ArrayList<GameData> getGames();

    public String createGame(String gameName);

    public void joinGame(String playerName, String playerColor, String gameID) throws Exception;
    void clear();

}

