package dataaccess;

import org.eclipse.jetty.websocket.api.Session;

public record SockConnection(String name, Session session) {
}
