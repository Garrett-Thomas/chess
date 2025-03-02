package server;

import com.google.gson.Gson;
import dao.MemoryAuthDAO;
import dao.MemoryGameDAO;
import dao.MemoryUserDAO;
import dataaccess.ServiceException;
import model.ResponseSuper;
import spark.*;

import static spark.Spark.before;

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
        Spark.exception(ServiceException.class, (ServiceException se, Request req, Response res) -> {
            res.status(se.getStatusCode());

            System.err.println(se.getMessage());
            res.body(new Gson().toJson(new ResponseSuper(se.getResponse())));
        });

        Spark.after("*", (Request req, Response res) -> {
            res.type("application/json");
        });

        Spark.delete("/session", this.authHandler::deleteToken);
        Spark.post("/session", this.authHandler::postLogin);
        Spark.post("/user", this.authHandler::postRegister);
        Spark.put("/game", this.gameHandler::joinGame);
        Spark.get("/game", this.gameHandler::listGames);
        Spark.post("/game", this.gameHandler::createGame);
        Spark.delete("/db", (Request req, Response res) -> {
            MemoryUserDAO.getInstance().clear();
            MemoryGameDAO.getInstance().clear();
            MemoryAuthDAO.getInstance().clear();
            return new Gson().toJson(null);
        });


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}

