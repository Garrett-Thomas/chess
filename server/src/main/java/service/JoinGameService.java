package service;

import model.GamesRequest;
import model.GamesResult;
import server.GameDAO;
import server.MemoryGameDAO;

public class JoinGameService {

    private final GameDAO gameDAO;

    public JoinGameService() {
        this.gameDAO = MemoryGameDAO.getInstance();
    }


    // Assumes that user is authenticated
    public GamesResult createGame(GamesRequest gamesRequest) {
        String gameID = this.gameDAO.createGame(gamesRequest.gameName());

        return new GamesResult(gameID, null);
    }

    public GamesResult listGames(){

        return new GamesResult(null, this.gameDAO.getGames());


    }

}
