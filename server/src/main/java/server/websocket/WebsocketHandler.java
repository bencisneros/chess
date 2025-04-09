package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.AuthDatabase;
import dataaccess.GameDatabase;
import model.GameData;
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

        MakeMoveCommand makeMoveCommand = new MakeMoveCommand("", -1, null);
        if(command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE){
            makeMoveCommand = new Gson().fromJson(message, MakeMoveCommand.class);
        }

        if (checkAuth(command.getAuthToken())){
            ErrorMessage errorMessage = new ErrorMessage("Error: unauthorized");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            return;
        }

        String username = getUsername(command.getAuthToken());

        switch (command.getCommandType()) {
            case CONNECT -> connect(command, session, username);
            case MAKE_MOVE -> makeMove(makeMoveCommand, session, username);
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
        GameDatabase gameDatabase = new GameDatabase();

        ChessMove move = makeMoveCommand.getMove();
        int gameId = makeMoveCommand.getGameID();
        GameData gameData = gameDatabase.getGame(gameId);
        ChessGame game = gameData.game();
        ChessGame.TeamColor color = null;

        if(!Objects.equals(username, gameData.whiteUsername()) && !Objects.equals(username, gameData.blackUsername())){
            ErrorMessage errorMessage = new ErrorMessage("Error: observer cannot make move");
            connections.error(errorMessage, username);
            return;
        }

        if(Objects.equals(username, gameData.whiteUsername())){
            color = ChessGame.TeamColor.WHITE;
        }
        if(Objects.equals(username, gameData.blackUsername())){
            color = ChessGame.TeamColor.BLACK;
        }
        if(color != game.team){
            ErrorMessage errorMessage = new ErrorMessage("Error: not your turn");
            connections.error(errorMessage, username);
            return;
        }

        if(game.getStatus()){
            ErrorMessage errorMessage = new ErrorMessage("Error: game is over");
            connections.error(errorMessage, username);
            return;
        }


        var validMoves = game.validMoves(move.startPosition);
        if(!validMoves.contains(move)){
            ErrorMessage errorMessage = new ErrorMessage("Error: invalid move");
            connections.error(errorMessage, username);
            return;
        }
        game.makeMove(move);

        gameDatabase.updateGameBoard(gameId, game);

        String startSpot = "";
        int startRow = move.startPosition.row;
        int startCol = move.startPosition.col;

        switch (startRow){
            case 1 -> startSpot += "a";
            case 2 -> startSpot += "b";
            case 3 -> startSpot += "c";
            case 4 -> startSpot += "d";
            case 5 -> startSpot += "e";
            case 6 -> startSpot += "f";
            case 7 -> startSpot += "g";
            case 8 -> startSpot += "h";
        }
        startSpot += String.valueOf(9 - startCol);

        String endSpot = "";
        int endRow = move.endPosition.row;
        int endCol = move.endPosition.col;

        switch (endRow){
            case 1 -> endSpot += "a";
            case 2 -> endSpot += "b";
            case 3 -> endSpot += "c";
            case 4 -> endSpot += "d";
            case 5 -> endSpot += "e";
            case 6 -> endSpot += "f";
            case 7 -> endSpot += "g";
            case 8 -> endSpot += "h";
        }
        endSpot += String.valueOf(9 - endCol);

        NotificationMessage notificationMessage = new NotificationMessage(username + " moved from " + startSpot + " to " + endSpot);
        connections.broadcast("", notificationMessage, gameId);

        String blackUsername = gameData.blackUsername();
        LoadGameMessage loadBlackGame = new LoadGameMessage(game, "black");
        connections.sendToOneClient(loadBlackGame, blackUsername);

        LoadGameMessage loadGameMessage = new LoadGameMessage(game, "white");
        connections.sendLoadGame(blackUsername, loadGameMessage, gameId);

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

        if(color.equals("an observer")){
            LoadGameMessage loadGameMessage = new LoadGameMessage(game.game(), "white");
            connections.sendToOneClient(loadGameMessage, username);
        }
        else {
            LoadGameMessage loadGameMessage = new LoadGameMessage(game.game(), color);
            connections.sendToOneClient(loadGameMessage, username);
        }

        NotificationMessage notificationMessage = new NotificationMessage(username + " has joined the game as " + color);
        connections.broadcast(username, notificationMessage, gameId);
    }
}