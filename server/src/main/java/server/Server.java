package server;

import com.google.gson.Gson;
import dataaccess.DbUtils;
import dataaccess.ServiceException;
import model.ResponseSuper;
import spark.*;
import static spark.Spark.webSocket;

public class Server {
    private final AuthHandler authHandler;
    private final GameHandler gameHandler;
    private final WebSocketHandler webSocketHandler;

    public Server() {
        this.authHandler = new AuthHandler();
        this.gameHandler = new GameHandler();
        this.webSocketHandler = new WebSocketHandler();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        // Register your endpoints and handle exceptions here.


        Spark.exception(ServiceException.class, (ServiceException se, Request req, Response res) -> {
            res.status(se.getStatusCode());

            System.err.println(se.getMessage());
            res.body(new Gson().toJson(new ResponseSuper(se.getResponse())));
        });

//        Spark.before("*", (Request req, Response res)->{
//            if(!req.headers("Sec-WebSocket-Extensions").isEmpty()){
//                System.out.println("ooh ahh");
//            }
//        });
//
        // Crucial that this is placed before the after middleware
        // I suspect that it tries to apply the middleware to the ws connection
        // and weird things happen or tries treating the /ws route as http
        webSocket("/ws", webSocketHandler);


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
            DbUtils.clearDB();
            return new Gson().toJson(null);
        });

        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}

