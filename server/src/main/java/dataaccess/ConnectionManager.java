package dataaccess;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ConnectionManager {

    private static final HashMap<String, Session> connections = new HashMap<>();

    public void broadcast(String excludeName, String msg) throws IOException {
        ArrayList<String> removeList = new ArrayList<>();
        for (String name : connections.keySet()) {
            if (connections.get(name).isOpen()) {

                if (!name.equals(excludeName)) {
                    connections.get(name).getRemote().sendString(msg);
                }

            } else {
                removeList.add(name);
            }
        }

        for (String toRemove : removeList) {
            connections.remove(toRemove);
        }
    }
}
