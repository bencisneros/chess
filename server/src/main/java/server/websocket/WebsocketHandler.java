package server.websocket;

import com.google.gson.Gson;
import dataaccess.AuthDatabase;
import dataaccess.GameDatabase;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.util.Objects;


@WebSocket
public class WebsocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        if (checkAuth(command.getAuthToken())){
            ErrorMessage errorMessage = new ErrorMessage("Error: unauthorized");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }

        String username = getUsername(command.getAuthToken());

        switch (command.getCommandType()) {
            case CONNECT -> connect(command, session, username);
            case MAKE_MOVE -> makeMove((MakeMoveCommand) command, session, username);
            case LEAVE -> leave(username, session);
            case RESIGN -> resign(username, session);
        }
    }


    private boolean checkAuth(String authToken) throws Exception {
        AuthDatabase authDatabase = new AuthDatabase();
        return authDatabase.getAuth(authToken) == null;
    }

    private String getUsername(String authToken) throws Exception {
        AuthDatabase authDatabase = new AuthDatabase();
        return authDatabase.getAuth(authToken).username();
    }

    private void resign(String username, Session session) {
    }

    private void leave(String username, Session session) {
    }

    private void makeMove(MakeMoveCommand makeMoveCommand, Session session, String username) throws Exception {

    }

    public void connect(UserGameCommand command, Session session, String username) throws Exception{
        int gameId = command.getGameID();
        String authToken = command.getAuthToken();
        GameDatabase gameDatabase = new GameDatabase();
        AuthDatabase authDatabase = new AuthDatabase();

        connections.add(gameId, session, username);

        if (authDatabase.getAuth(authToken) == null){
            ErrorMessage errorMessage = new ErrorMessage("Error: enter valid index");
            connections.error(errorMessage, username);
            return;
        }


        if (gameDatabase.getGame(gameId) == null){
            ErrorMessage errorMessage = new ErrorMessage("Error: enter valid index");
            connections.error(errorMessage, username);
            return;
        }

        var game = gameDatabase.getGame(gameId);
        String color;
        if(Objects.equals(game.blackUsername(), username)){
            color = "black";
        }
        else if(Objects.equals(game.whiteUsername(), username)){
            color = "white";
        }
        else{
            color = "an observer";
        }
        LoadGameMessage loadGameMessage = new LoadGameMessage(game.game());
        NotificationMessage notificationMessage = new NotificationMessage(username + " has joined the game as " + color);
        connections.broadcast(username, notificationMessage, gameId);
        connections.sendToSelf(loadGameMessage, username);
    }

    public void sendUnauthorized(String authToken){

    }


}