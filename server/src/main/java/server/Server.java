package server;

import com.google.gson.Gson;
import dataaccess.ServiceException;
import spark.*;

import static spark.Spark.before;
import static spark.route.HttpMethod.before;

public class Server {
    private final AuthHandler authHandler;
    private final GameHandler gameHandler;

    public Server() {
        this.authHandler = new AuthHandler();
        this.gameHandler = new GameHandler();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
 // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();
        Spark.delete("/session", this.authHandler::deleteToken);
        Spark.post("/session", this.authHandler::postLogin);
        Spark.post("/user", this.authHandler::postRegister);

        Spark.exception(ServiceException.class, (ServiceException se, Request req, Response res) ->{
            res.status(se.getStatusCode());
            res.body(new Gson().toJson(new ResponseSuper(se.getMessage()));
        });




        Spark.get("/game",  this.gameHandler::listGames);

        Spark.post("/game", this.gameHandler::createGame);




        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}

