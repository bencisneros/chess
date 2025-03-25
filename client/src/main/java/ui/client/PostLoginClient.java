package ui.client;
import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
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
                "help";
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
                case "quit" -> "quit";
                default -> help();
            };
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    private String create(String[] params) throws Exception{
        if(params.length != 1){
            throw new Exception(SET_TEXT_COLOR_RED + "expected: <name>");
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
            throw new Exception(SET_TEXT_COLOR_RED + "expected: <ID> <WHITE/BLACK>");
        }

        int userId;
        try{
            userId = Integer.parseInt(params[0]);
        } catch (Exception e) {
            throw new Exception(SET_TEXT_COLOR_RED + "expected: <ID> <WHITE/BLACK>");
        }


        GameInfo[] list = server.listGames(authData);

        if(list.length < userId){
            throw new Exception(SET_TEXT_COLOR_RED + "enter valid index");
        }

        ChessGame game = list[userId - 1].gameData().game();
        int gameId = list[userId - 1].gameID();
        String color = params[1];



        if(!Objects.equals(color, "white") && !Objects.equals(color, "black")){
            throw new Exception(SET_TEXT_COLOR_RED + "expected: <ID> <WHITE/BLACK>");
        }
        try {
            server.joinGame(authData, color, gameId);
        } catch (Exception e) {
            throw new Exception(SET_TEXT_COLOR_RED + "pick valid color");
        }

        if(color.equals("white")) {
            return "joining game " + userId + "\n" + printWhiteBoard(game);
        }
        else{
            return "joining game " + userId + "\n" + printBlackBoard(game);
        }
    }

    private String printBlackBoard(ChessGame game){
        var gameBoard = game.board.board;
        String board = "";
        board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + "    h  g  f  e  d  c  b  a    " + RESET_BG_COLOR + "\n";
        for(int i = 1; i < 9; i++){
            for(int j = 0; j < 10; j++){
                if(j == 0 || j == 9){
                    switch (i){
                        case 1: board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 1 "; break;
                        case 2: board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 2 "; break;
                        case 3: board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 3 "; break;
                        case 4: board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 4 "; break;
                        case 5: board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 5 "; break;
                        case 6: board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 6 "; break;
                        case 7: board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 7 "; break;
                        case 8: board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 8 "; break;
                    }
                    if (j == 9){
                        board += RESET_BG_COLOR + "\n";
                    }
                }
                else{
                    if((i + j) % 2 == 0){
                        board += SET_BG_COLOR_WHITE + " " + getPiece(gameBoard, i, j, true) + " ";
                    }
                    else{
                        board += SET_BG_COLOR_BLACK + " " + getPiece(gameBoard, i, j, true) + " ";
                    }
                }
            }
        }
        board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + "    h  g  f  e  d  c  b  a    " + RESET_BG_COLOR + "\n";


        return board;
    }

    private String getPiece(ChessPiece[][] board, int i, int j, boolean reverse) {
        var piece = board[i][j];
        if(piece == null){
            return " ";
        }

        String color;
        if (piece.pieceColor == ChessGame.TeamColor.BLACK){
            if(reverse) {
                color = SET_TEXT_COLOR_BLUE;
            }
            else{
                color = SET_TEXT_COLOR_RED;
            }
        }
        else{
            if(reverse){
                color = SET_TEXT_COLOR_RED;
            }
            else {
                color = SET_TEXT_COLOR_BLUE;
            }
        }


        switch (piece.type){
            case ChessPiece.PieceType.PAWN -> {
                return color + "P";
            }
            case ChessPiece.PieceType.ROOK -> {
                return color + "R";
            }
            case ChessPiece.PieceType.KNIGHT -> {
                return color + "N";
            }
            case ChessPiece.PieceType.BISHOP -> {
                return color + "B";
            }
            case ChessPiece.PieceType.KING -> {
                return color + "K";
            }
            case ChessPiece.PieceType.QUEEN -> {
                return color + "Q";
            }
        }
        return "";
    }

    private String printWhiteBoard(ChessGame game) {
        var gameBoard = game.board.board;
        String board = "";
        board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + "    a  b  c  d  e  f  g  h    " + RESET_BG_COLOR + "\n";
        for(int i = 1; i < 9; i++){
            for(int j = 0; j < 10; j++){
                if(j == 0 || j == 9){
                    switch (i){
                        case 1: board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 8 "; break;
                        case 2: board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 7 "; break;
                        case 3: board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 6 "; break;
                        case 4: board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 5 "; break;
                        case 5: board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 4 "; break;
                        case 6: board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 3 "; break;
                        case 7: board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 2 "; break;
                        case 8: board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 1 "; break;
                    }
                    if (j == 9){
                        board += RESET_BG_COLOR + "\n";
                    }
                }
                else{
                    if((i + j) % 2 == 0){
                        board += SET_BG_COLOR_WHITE + " " + getPiece(gameBoard, i, j, false) + " ";
                    }
                    else{
                        board += SET_BG_COLOR_BLACK + " " + getPiece(gameBoard, i, j, false) + " ";
                    }
                }
            }
        }
        board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + "    a  b  c  d  e  f  g  h    " + RESET_BG_COLOR + "\n";


        return board;
    }

    private String printWhiteBottomPawnRow(){
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

    private String printBlackTopPawnRow(){
        return  SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 2 " + SET_TEXT_COLOR_RED +
                SET_BG_COLOR_BLACK + " P " +
                SET_BG_COLOR_WHITE + " P " +
                SET_BG_COLOR_BLACK + " P " +
                SET_BG_COLOR_WHITE + " P " +
                SET_BG_COLOR_BLACK + " P " +
                SET_BG_COLOR_WHITE + " P " +
                SET_BG_COLOR_BLACK + " P " +
                SET_BG_COLOR_WHITE + " P " +
                SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 2 " + RESET_BG_COLOR + "\n";
    }

    private String printBlackBottomPawnRow(){
        return  SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 7 " + SET_TEXT_COLOR_BLUE +
                SET_BG_COLOR_WHITE + " P " +
                SET_BG_COLOR_BLACK + " P " +
                SET_BG_COLOR_WHITE + " P " +
                SET_BG_COLOR_BLACK + " P " +
                SET_BG_COLOR_WHITE + " P " +
                SET_BG_COLOR_BLACK + " P " +
                SET_BG_COLOR_WHITE + " P " +
                SET_BG_COLOR_BLACK + " P " +
                SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " 7 " + RESET_BG_COLOR + "\n";
    }

    private String printWhiteTopPawnRow(){
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
                printFiveRows() +
                SET_BG_COLOR_WHITE + "   " +
                SET_BG_COLOR_BLACK + "   " +
                SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " " + rowNum + " " + RESET_BG_COLOR + "\n";
    }

    private String printStartBlack(String rowNum){
        return  SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " " + rowNum + " " + SET_TEXT_COLOR_BLUE +
                SET_BG_COLOR_BLACK + "   " +
                printFiveRows() +
                SET_BG_COLOR_WHITE + "   " +
                SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + " " + rowNum + " " + RESET_BG_COLOR + "\n";
    }

    private String printFiveRows(){
        return  SET_BG_COLOR_WHITE + "   " +
                SET_BG_COLOR_BLACK + "   " +
                SET_BG_COLOR_WHITE + "   " +
                SET_BG_COLOR_BLACK + "   " +
                SET_BG_COLOR_WHITE + "   " +
                SET_BG_COLOR_BLACK + "   " ;
    }



    private String observe(String[] params) throws Exception{

        int userId;
        try{
            userId = Integer.parseInt(params[0]);
        } catch (Exception e) {
            throw new Exception(SET_TEXT_COLOR_RED + "expected: <ID>");
        }

        GameInfo[] list = server.listGames(authData);

        if(list.length < userId){
            throw new Exception(SET_TEXT_COLOR_RED + "enter valid index");
        }

        var game = list[userId - 1].gameData().game();

        return "observing game " + userId + "\n" +  printWhiteBoard(game);

    }

    private String logout() throws Exception {
        server.logout(authData);
        return "logged out";
    }
}
