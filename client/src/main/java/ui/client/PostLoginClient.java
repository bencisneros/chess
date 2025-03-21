package ui.client;
import java.util.Arrays;

import static ui.EscapeSequences.*;

public class PostLoginClient {
    public PostLoginClient(String serverUrl) {
    }

    public String help() {
        return "create: <name>\n" +
                "list\n" +
                "join: <ID> <WHITE/BLACK>\n" +
                "observe: <ID>\n" +
                "logout\n" +
                "quit\n" +
                "help\n";
    }

    public String eval(String line) {
        try {
            var tokens = line.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(params);
                case "list" -> list(params);
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String create(String[] params) {
        return "";
    }

    private String list(String[] params) {
        return null;
    }

    private String join(String[] params) {
        return null;
    }

    private String observe(String[] params) {
        return null;
    }

    private String logout() {
        return "";
    }
}
