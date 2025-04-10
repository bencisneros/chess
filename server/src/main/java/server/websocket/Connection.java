package server.websocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    public int gameId;
    public Session session;
    public String username;

    public Connection(int gameId, Session session, String username) {
        this.gameId = gameId;
        this.session = session;
        this.username = username;
    }

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}