package ui.client;
import model.AuthData;
import ui.ServerFacade;
import static ui.EscapeSequences.*;

import java.util.Arrays;
import java.util.Objects;

import ui.ServerFacade.GameInfo;

public class PostLoginClient {

    private final ServerFacade server;
    private AuthData authData = null;

    public PostLoginClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public void setAuthData(AuthData authData) {
        this.authData = authData;
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
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "logout" -> logout();
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String create(String[] params) throws Exception{
        if(params.length != 1){
            throw new Exception("expected: <name>");
        }

        String gameName = params[0];

        server.createGame(authData, gameName);

        return "created " + gameName;
    }

    private String list() throws Exception {
        GameInfo[] list = server.listGames(authData);
        String returnString = "Games:\n";
        int gameNumber = 1;
        for(GameInfo gameInfo : list) {
            returnString += gameNumber + ". Game Name: " + gameInfo.gameName() +
                                         "\n   White: " + gameInfo.whiteUsername() +
                                         "\n   Black: " + gameInfo.blackUsername() + "\n\n";
            gameNumber ++;
        }
        return returnString;
    }

    private String join(String[] params) throws Exception{
        if(params.length != 2){
            throw new Exception("expected: <ID> <WHITE/BLACK>");
        }

        int userId = 0;
        try{
            userId = Integer.parseInt(params[0]);
        } catch (Exception e) {
            throw new Exception("expected: <ID> <WHITE/BLACK>");
        }


        GameInfo[] list = server.listGames(authData);

        int gameId = list[userId - 1].gameID();
        String color = params[1];

        if(!Objects.equals(color, "white") && !Objects.equals(color, "black")){
            throw new Exception("expected: <ID> <WHITE/BLACK>");
        }

        server.joinGame(authData, color, gameId);
        if(color.equals("white")) {
            return (printWhiteBoard(color));
        }
        return "";
    }

    private String printWhiteBoard(String color) {
        String board = "";
        board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + "    a  b  c  d  e  f  g  h    " + RESET_BG_COLOR + "\n";
        board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 8 " + SET_TEXT_COLOR_BLUE +
                 SET_BG_COLOR_WHITE + " R " +
                 SET_BG_COLOR_BLACK + " N " +
                 SET_BG_COLOR_WHITE + " B " +
                 SET_BG_COLOR_BLACK + " Q " +
                 SET_BG_COLOR_WHITE + " K " +
                 SET_BG_COLOR_BLACK + " B " +
                 SET_BG_COLOR_WHITE + " N " +
                 SET_BG_COLOR_BLACK + " R " +
                 SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 8 " + RESET_BG_COLOR + "\n";
        board += printTopPawnRow();
        board += printStartWhite("6");
        board += printStartBlack("5");
        board += printStartWhite("4");
        board += printStartBlack("3");
        board += printBottomPawnRow();
        board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 8 " + SET_TEXT_COLOR_RED +
                 SET_BG_COLOR_BLACK + " R " +
                 SET_BG_COLOR_WHITE + " N " +
                 SET_BG_COLOR_BLACK + " B " +
                 SET_BG_COLOR_WHITE + " Q " +
                 SET_BG_COLOR_BLACK + " K " +
                 SET_BG_COLOR_WHITE + " B " +
                 SET_BG_COLOR_BLACK + " N " +
                 SET_BG_COLOR_WHITE + " R " +
                SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 8 " + RESET_BG_COLOR + "\n";
        board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + "    a  b  c  d  e  f  g  h    " + RESET_BG_COLOR + "\n";

        return board;
    }

    private String printBottomPawnRow(){
        return  SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 2 " + SET_TEXT_COLOR_RED +
                SET_BG_COLOR_WHITE + " P " +
                SET_BG_COLOR_BLACK + " P " +
                SET_BG_COLOR_WHITE + " P " +
                SET_BG_COLOR_BLACK + " P " +
                SET_BG_COLOR_WHITE + " P " +
                SET_BG_COLOR_BLACK + " P " +
                SET_BG_COLOR_WHITE + " P " +
                SET_BG_COLOR_BLACK + " P " +
                SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 2 " + RESET_BG_COLOR + "\n";
    }

    private String printTopPawnRow(){
        return  SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 7 " + SET_TEXT_COLOR_BLUE +
                SET_BG_COLOR_BLACK + " P " +
                SET_BG_COLOR_WHITE + " P " +
                SET_BG_COLOR_BLACK + " P " +
                SET_BG_COLOR_WHITE + " P " +
                SET_BG_COLOR_BLACK + " P " +
                SET_BG_COLOR_WHITE + " P " +
                SET_BG_COLOR_BLACK + " P " +
                SET_BG_COLOR_WHITE + " P " +
                SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 7 " + RESET_BG_COLOR + "\n";
    }

    private String printStartWhite(String rowNum){
        return  SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " " + rowNum + " " + SET_TEXT_COLOR_BLUE +
                SET_BG_COLOR_WHITE + "   " +
                SET_BG_COLOR_BLACK + "   " +
                SET_BG_COLOR_WHITE + "   " +
                SET_BG_COLOR_BLACK + "   " +
                SET_BG_COLOR_WHITE + "   " +
                SET_BG_COLOR_BLACK + "   " +
                SET_BG_COLOR_WHITE + "   " +
                SET_BG_COLOR_BLACK + "   " +
                SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " " + rowNum + " " + RESET_BG_COLOR + "\n";
    }

    private String printStartBlack(String rowNum){
        return  SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " " + rowNum + " " + SET_TEXT_COLOR_BLUE +
                SET_BG_COLOR_BLACK + "   " +
                SET_BG_COLOR_WHITE + "   " +
                SET_BG_COLOR_BLACK + "   " +
                SET_BG_COLOR_WHITE + "   " +
                SET_BG_COLOR_BLACK + "   " +
                SET_BG_COLOR_WHITE + "   " +
                SET_BG_COLOR_BLACK + "   " +
                SET_BG_COLOR_WHITE + "   " +
                SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " " + rowNum + " " + RESET_BG_COLOR + "\n";
    }



    private String observe(String[] params) {
        return null;
    }

    private String logout() {
        return "";
    }
}
