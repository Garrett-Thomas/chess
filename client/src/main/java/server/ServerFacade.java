package server;

import passoff.server.TestServerFacade;

import java.util.HashMap;

public class ServerFacade extends TestServerFacade {

    private static ServerFacade server = null;
    private static String authToken = null;
    private static HashMap<String, String> gameNumToID = null;


    public ServerFacade(String url, String port) {
        super(url, port);
        server = this;
    }

    public static ServerFacade getInstance() {
        return server;
    }

    public static String getToken() {
        return authToken;
    }

    public static void setToken(String token) {
        authToken = token;
    }
}
