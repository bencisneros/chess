package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(int gameId, Session session, String username) {
        var connection = new Connection(gameId, session, username);
        connections.put(username, connection);
    }

    public void remove(String username) {
        connections.remove(username);
    }

    public void broadcast(String excludeUsername, ServerMessage message, int gameId) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!Objects.equals(c.username, excludeUsername) && c.gameId == gameId) {
                    Gson gson = new Gson();
                    c.send(gson.toJson(message));
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.username);
        }
    }

    public void sendToOneClient(LoadGameMessage message, String username) throws IOException {
        for(ConcurrentHashMap.Entry<String, Connection> entry : connections.entrySet()){
            String tempUsername = entry.getKey();
            Connection c = entry.getValue();

            if(Objects.equals(tempUsername, username)){
                Gson gson = new Gson();
                c.send(gson.toJson(message));
            }
        }
    }

    public void error(ErrorMessage message, String username) throws IOException {
        for(ConcurrentHashMap.Entry<String, Connection> entry : connections.entrySet()){
            String tempUsername = entry.getKey();
            Connection c = entry.getValue();
            if(Objects.equals(tempUsername, username)){
                Gson gson = new Gson();
                c.send(gson.toJson(message));
            }
        }
    }
}
