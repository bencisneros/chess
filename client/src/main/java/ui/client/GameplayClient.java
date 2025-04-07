package ui.client;

import model.AuthData;
import ui.ServerFacade;

import java.util.Arrays;

public class GameplayClient {

    private AuthData authData = null;
    private final ServerFacade server;
    private String color;


    public GameplayClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }
    public void setColor(String color){
        this.color = color;
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

    private String highlightMoves(String[] params) {
        return "";
    }

    private String resign() {
        return "";
    }

    private String move(String[] params) {
        return "";
    }

    private String redraw() {
        return "";
    }

    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

    private String leave() {
        return "leaving game";
    }
}
