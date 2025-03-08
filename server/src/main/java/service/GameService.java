package service;

import dao.SQLGameDAO;
import model.GamesRequest;
import model.GamesResult;
import dao.GameDAO;
import dao.MemoryGameDAO;

public class GameService {

    private final GameDAO gameDAO;

    public GameService() {
        this.gameDAO = SQLGameDAO.getInstance();
    }


    public GamesResult createGame(GamesRequest gamesRequest) {
        Integer gameID = this.gameDAO.createGame(gamesRequest.gameName());

        return new GamesResult(gameID, null);
    }

    public GamesResult listGames(){

        return new GamesResult(null, this.gameDAO.getGames());
    }


    public void joinGame(GamesRequest gamesRequest, String playerName) throws Exception {

        this.gameDAO.joinGame(playerName, gamesRequest.playerColor(), gamesRequest.gameID());
        new GamesResult(null, null);
    }
}
