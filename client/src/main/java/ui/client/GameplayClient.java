package ui.client;

import model.AuthData;
import ui.ServerFacade;

import java.util.Arrays;

public class GameplayClient {

    private AuthData authData = null;
    private final ServerFacade server;

    public GameplayClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public String help() {
        return "leave\n" +
                "logout\n" +
                "quit\n" +
                "help";
    }

    public String eval(String line) {
        try {
            var tokens = line.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "leave" -> leave();
                case "logout" -> logout();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

    private String logout() throws Exception{
        server.logout(authData);
        return "logged out";
    }

    private String leave() {
        return "leaving game";
    }
}
