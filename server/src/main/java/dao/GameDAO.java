package dao;

import dataaccess.ServiceException;
import model.GameData;

import java.util.ArrayList;

public interface GameDAO {


    public ArrayList<GameData> getGames();

    public Integer createGame(String gameName);

    public void joinGame(String playerName, String playerColor, int gameID) throws ServiceException;
    void clear();

}

