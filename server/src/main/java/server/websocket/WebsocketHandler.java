package server.websocket;

import com.google.gson.Gson;
import dataaccess.AuthDatabase;
import dataaccess.GameDatabase;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;


@WebSocket
public class WebsocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        try {
            UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
            String username = getUsername(command.getAuthToken());
            switch (command.getCommandType()) {
                case CONNECT -> connect(command.getGameID(), session, username);
                case MAKE_MOVE -> makeMove(username, session);
                case LEAVE -> leave(username, session);
                case RESIGN -> resign(username, session);
            }
        }
        catch (Exception e){
        }
    }
    private String getUsername(String authToken) throws Exception {
        AuthDatabase authDatabase = new AuthDatabase();
        return authDatabase.getAuth(authToken).username();
    }

    private void resign(String username, Session session) {
    }

    private void leave(String username, Session session) {
    }

    private void makeMove(String username, Session session) {
    }

    public void connect(int gameId, Session session, String username) throws Exception{
        connections.add(gameId, session, username);
        GameDatabase gameDatabase = new GameDatabase();
        var game = gameDatabase.getGame(gameId).game();
        var loadGameMessage = new LoadGameMessage(game);
        var notificationMessage = new NotificationMessage(username + " has joined the game");
        connections.broadcast(username, notificationMessage, gameId);
        connections.sendToSelf(loadGameMessage, username);
    }


}