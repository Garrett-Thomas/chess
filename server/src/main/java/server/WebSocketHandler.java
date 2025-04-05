package server;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;
import java.util.Timer;

    @WebSocket
    public class WebSocketHandler {

        @OnWebSocketMessage
        public void onMessage(Session session, String message) throws IOException {
            System.out.println(message);
        }

        private void enter(String visitorName, Session session) throws IOException {
        }

        private void exit(String visitorName) throws IOException {
        }

    }
