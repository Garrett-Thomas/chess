package server;

import com.google.gson.Gson;
import dao.SQLAuthDAO;
import dataaccessclasses.ServiceException;
import model.GamesRequest;
import model.GamesResult;
import service.GameService;
import spark.Request;
import spark.Response;

public class GameHandler {

    private final GameService gameService;

    GameHandler() {
        this.gameService = new GameService();
    }

    public Object listGames(Request req, Response res) throws ServiceException {
        ServerUtils.authUser(req, res);
        var gameRes = gameService.listGames();
        return new Gson().toJson(gameRes);
    }


    public Object createGame(Request req, Response res) throws ServiceException {
        ServerUtils.authUser(req, res);
        GamesRequest gameReq = new Gson().fromJson(req.body(), GamesRequest.class);
        if (gameReq.gameName() == null || gameReq.gameName().isEmpty()) {
            throw new ServiceException(400, "gameName is null");
        }

        GamesResult gameRes = gameService.createGame(gameReq);

        return new Gson().toJson(gameRes);


    }

    public Object joinGame(Request req, Response res) throws Exception {
        ServerUtils.authUser(req, res);

        GamesRequest gameReq = new Gson().fromJson(req.body(), GamesRequest.class);

        if (gameReq.gameID() == null || gameReq.playerColor() == null) {
            throw new ServiceException(400, "Bad Body");
        }
        String token = req.headers("authorization");

        String username = SQLAuthDAO.getInstance().getUsername(token);


        this.gameService.joinGame(gameReq, username);

        return new Gson().toJson(null);

    }
}
