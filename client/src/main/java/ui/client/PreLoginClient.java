package ui.client;

import java.util.Arrays;

import model.UserData;
import ui.ServerFacade;

public class PreLoginClient {

    private final ServerFacade server;

    public PreLoginClient(String serverUrl){
        server = new ServerFacade(serverUrl);
    }

    public String help() {
        return "register: <username> <email> <password>\n" +
                "signIn: <username> <password>\n" +
                "quit\n" +
                "help\n";
    }

    public String eval(String line) {
        try {
            var tokens = line.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "signIn" -> signIn(params);
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String register(String[] params) throws Exception{
        if(params.length != 3){
            throw new Exception("expected: <username> <email> <password>");
        }
        String username = params[0];
        String email = params[1];
        String password = params[2];

        UserData user = new UserData(username, email, password);

        server.register(user);

        return "Signed in as " + username;
    }

    private String signIn(String[] params) {
        return "";
    }

}
