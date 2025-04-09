package server;

import com.google.gson.Gson;
import utils.GsonParent;
import websocket.commands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    Gson gson = GsonParent.getInstance();

    public WebSocketFacade(String url, String port) throws RuntimeException {

        try {

            url = "ws://" + url + ":" + port;
            URI socketURI = new URI(url + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    GameMessageHandler.handleMessage(message);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }


    public void sendMessage(UserGameCommand command) {
        try {
            session.getBasicRemote().sendText(gson.toJson(command));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
