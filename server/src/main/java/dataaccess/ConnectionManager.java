package dataaccess;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Need to be able to broadcast to each member of a game and exclude people in the game
 * Perhaps a better way to do this is to map the gameID's to a Connection object that
 * has the session and name
 */
public class ConnectionManager {

    private final HashMap<Integer, ArrayList<Connection>> connMap = new HashMap<>();

    public void broadcast(Set<String> toExclude, Integer gameID, String msg) throws IOException {
        ArrayList<Connection> removeList = new ArrayList<>();
        var connList = connMap.get(gameID);
        for (var conn : connList) {
            if (conn.session().isOpen()) {

                if (!toExclude.contains(conn.name())) {

                    conn.session().getRemote().sendString(msg);
                }

            } else {
                removeList.add(conn);
            }
        }

        connList.removeAll(removeList);
        connMap.put(gameID, connList);
    }
}
