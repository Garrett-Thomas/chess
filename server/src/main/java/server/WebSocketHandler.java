package server;

import com.google.gson.Gson;
import dao.SQLAuthDAO;
import dao.SQLGameDAO;
import dao.SQLUserDAO;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Timer;

@WebSocket
public class WebSocketHandler {
    private static SQLUserDAO userDAO = SQLUserDAO.getInstance();
    private static SQLGameDAO gameDAO = SQLGameDAO.getInstance();
    private static SQLAuthDAO authDAO = SQLAuthDAO.getInstance();

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {

            // Implied that the client has already joined a game via http so just need to find user using token
            case CONNECT -> {
                String username = authDAO.getUsername(command.getAuthToken());
                var userData = userDAO.getUser(username);






            }

        }


    }

    @OnWebSocketError
    public void onError(Session session, Throwable error) {
        System.out.printf("%s %s", session.getProtocolVersion(), error.toString());
    }
}
