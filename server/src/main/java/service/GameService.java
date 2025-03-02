package service;

import model.GamesRequest;
import model.GamesResult;
import dao.AuthDAO;
import dao.GameDAO;
import dao.MemoryAuthDAO;
import dao.MemoryGameDAO;

public class GameService {

    private final GameDAO gameDAO;

    private final AuthDAO authDAO;
    public GameService() {
        this.gameDAO = MemoryGameDAO.getInstance();
        this.authDAO = MemoryAuthDAO.getInstance();

    }


    public GamesResult createGame(GamesRequest gamesRequest) {
        String gameID = this.gameDAO.createGame(gamesRequest.gameName());

        return new GamesResult(gameID, null);
    }

    public GamesResult listGames(){

        return new GamesResult(null, this.gameDAO.getGames());
    }


    public GamesResult joinGame(GamesRequest gamesRequest, String playerName) throws Exception {

        this.gameDAO.joinGame(playerName, gamesRequest.playerColor(), gamesRequest.gameID());
        return new GamesResult(null, null);
    }
}
