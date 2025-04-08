package ui.client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import dataaccess.GameDatabase;
import model.AuthData;
import ui.ServerFacade;
import ui.client.websocket.NotificationHandler;
import ui.client.websocket.WebsocketFacade;

import java.util.Arrays;

public class GameplayClient {

    private AuthData authData = null;
    private ServerFacade server;
    public static String color;
    public static int gameId;
    private final WebsocketFacade websocketFacade;
    private ChessGame game;
    private GameDatabase gameDatabase = new GameDatabase();


    public GameplayClient(String serverUrl, NotificationHandler notificationHandler) throws Exception {
        server = new ServerFacade(serverUrl);
        websocketFacade = new WebsocketFacade(serverUrl, notificationHandler);
    }

    public String help() {
        return  "redraw\n" +
                "leave\n" +
                "move <x#> <x#>\n" +
                "resign\n" +
                "highlightMoves <x#>\n" +
                "quit\n" +
                "help";
    }

    public String eval(String line) {
        try {
            var tokens = line.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "redraw" -> redraw();
                case "leave" -> leave();
                case "move" -> move(params);
                case "resign" -> resign();
                case "highlightmoves" -> highlightMoves(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public void setGame(ChessGame game) throws Exception{
        this.game = game;
    }

    private String highlightMoves(String[] params) {
        return "";
    }

    private String resign() {
        return "";
    }

    private String move(String[] params) throws Exception {
        char letter1 = params[0].charAt(0);
        int startRow = Character.getNumericValue(params[0].charAt(1));
        int startCol = 0;
        switch (letter1){
            case 'a' -> startCol = 1;
            case 'b' -> startCol = 2;
            case 'c' -> startCol = 3;
            case 'd' -> startCol = 4;
            case 'e' -> startCol = 5;
            case 'f' -> startCol = 6;
            case 'g' -> startCol = 7;
            case 'h' -> startCol = 8;
        }

        ChessPosition start = new ChessPosition(startRow, startCol);

        int endCol = 0;
        char letter2 = params[1].charAt(0);
        switch (letter2){
            case 'a' -> endCol = 1;
            case 'b' -> endCol = 2;
            case 'c' -> endCol = 3;
            case 'd' -> endCol = 4;
            case 'e' -> endCol = 5;
            case 'f' -> endCol = 6;
            case 'g' -> endCol = 7;
            case 'h' -> endCol = 8;
        }
        int endRow = Character.getNumericValue(params[1].charAt(1));

        ChessPosition end = new ChessPosition(endRow, endCol);
        ChessMove move = new ChessMove(start, end, null);


        websocketFacade.makeMove(authData.username(), authData.authToken(), gameId, move);

        return "moved from " + params[0] + " to " + params[1];
    }

    private String redraw() {
        return PostLoginClient.printBoard(game, color);
    }

    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

    private String leave() {
        return "leaving game";
    }
}
