package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;


@WebSocket
public class WebsocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getUsername(), session);
            case MAKE_MOVE -> makeMove(command.getUsername(), session);
            case LEAVE -> leave(command.getUsername(), session);
            case RESIGN -> resign(command.getUsername(), session);
        }
    }

    private void resign(String username, Session session) {
    }

    private void leave(String username, Session session) {
    }

    private void makeMove(String username, Session session) {
    }

    public void connect(String username, Session session) throws Exception{
        connections.add(username, session);
        String message = username += " has joined the game";
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        connections.broadcast(username, notification);
    }


}