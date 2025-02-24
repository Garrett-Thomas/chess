package server;

import com.google.gson.Gson;
import spark.*;
import model.UserData;

public class Server {
   private final LoginHandler loginHandler;

    public Server(){
       this.loginHandler = new LoginHandler();
    }
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();
        Spark.post("/session", this.loginHandler::postLogin);
        Spark.post("/user", this.loginHandler::postRegister);
        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

}

