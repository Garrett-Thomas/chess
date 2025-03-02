package server;

import com.google.gson.Gson;
import model.GamesRequest;
import service.JoinGameService;
import spark.Request;
import spark.Response;

public class GameHandler {

    private final JoinGameService joinGameService;

    GameHandler() {
        this.joinGameService = new JoinGameService();
    }

    public Object listGames(Request req, Response res) {

        var gameRes = joinGameService.listGames();
        return new Gson().toJson(gameRes);
    }


    public Object createGame(Request req, Response res) {

        GamesRequest gameReq = new Gson().fromJson(req.body(), GamesRequest.class);
        if (gameReq.gameName() == null ) {
            return new Gson().toJson(new ResponseSuper("Error: bad request"));

        }

        var gameRes = joinGameService.createGame(gameReq);

        return new Gson().toJson(gameRes);


    }


}
