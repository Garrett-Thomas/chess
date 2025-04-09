package server;

import passoff.server.TestServerFacade;
import ui.LocalStorage;
import websocket.commands.UserGameCommand;

import java.util.HashMap;

public class ServerFacade extends TestServerFacade {

    private static ServerFacade server = null;
    private static WebSocketFacade webSocketFacade;
    private static String url;
    private static String port;

    public ServerFacade(String url, String port) {
        super(url, port);
        ServerFacade.url = url;
        ServerFacade.port = port;
        server = this;

    }

    public static ServerFacade getInstance() {
        return server;
    }

    public static void connectSocket() {
        webSocketFacade = new WebSocketFacade(url, port);
        var authToken = LocalStorage.getToken();
        var connect = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, LocalStorage.getCurrGameID());
        webSocketFacade.sendMessage(connect);
    }


}
