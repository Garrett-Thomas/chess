package dataaccess;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Need to be able to broadcast to each member of a game and exclude people in the game
 * Perhaps a better way to do this is to map the gameID's to a SockConnection object that
 * has the session and name
 */
public class ConnectionManager {

    private final HashMap<Integer, ArrayList<SockConnection>> connMap = new HashMap<>();


    private SockConnection getUserByName(Integer gameID, String username) {
        var connList = connMap.get(gameID);
        SockConnection playerConn = null;

        for (var conn : connList) {
            if (conn.name().equals(username)) {
                playerConn = conn;
            }
        }

        if (playerConn == null) {
            throw new RuntimeException(String.format("%s not connected", username));
        }

        return playerConn;
    }


    public void disconnect(Integer gameID, String username) {
        try {
            var userConn = getUserByName(gameID, username);
            userConn.session().disconnect();

        } catch (IOException e) {
            System.err.println(String.format("Could not disconnect %s", username));
        }


    }

    public ArrayList<String> findUserByConnection(Session user) {

        ArrayList<String> res = new ArrayList<>();
        for (var gameID : connMap.keySet()) {
            for (var conn : connMap.get(gameID)) {
                if (conn.session().equals(user)) {
                    res.add(conn.name());
                    res.add(gameID.toString());
                    return res;
                }
            }
        }

        return null;
    }


    public void addConnection(Integer gameID, SockConnection sockConnection) {
        var connList = connMap.get(gameID);
        if (connList == null) {
            connList = new ArrayList<>();
        }
        connList.add(sockConnection);
        connMap.put(gameID, connList);
    }

    public void removeConnection(Integer gameID, String username) {
        var connList = connMap.get(gameID);
        var userConn = getUserByName(gameID, username);
        connList.remove(userConn);
        connMap.put(gameID, connList);
    }

    public void broadcast(Set<String> toExclude, Integer gameID, String msg) throws IOException {
        ArrayList<SockConnection> removeList = new ArrayList<>();
        var connList = connMap.get(gameID);
        for (var conn : connList) {
            if (conn.session().isOpen()) {

                if (toExclude == null || !toExclude.contains(conn.name())) {

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
