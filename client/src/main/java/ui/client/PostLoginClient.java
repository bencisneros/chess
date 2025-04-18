package ui.client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import model.AuthData;
import ui.ServerFacade;
import ui.ServerFacade.GameInfo;
import ui.client.websocket.NotificationHandler;
import ui.client.websocket.WebsocketFacade;

import java.util.Arrays;
import java.util.Objects;

import static ui.EscapeSequences.*;

public class PostLoginClient {

    private final ServerFacade server;
    private final WebsocketFacade websocketFacade;
    private AuthData authData = null;
    private final NotificationHandler notificationHandler;
    private String color;
    private int gameId;


    public PostLoginClient(String serverUrl, NotificationHandler notificationHandler) throws Exception {
        server = new ServerFacade(serverUrl);
        this.notificationHandler = notificationHandler;
        websocketFacade = new WebsocketFacade(serverUrl, notificationHandler);
    }

    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

    public int getGameId(){
        return gameId;
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

        if(list.length < userId || userId <= 0){
            websocketFacade.joinGame(authData.username(), authData.authToken(), -1);
            return "";
        }


        gameId = list[userId - 1].gameID();
        color = params[1];



        if(!Objects.equals(color, "white") && !Objects.equals(color, "black")){
            throw new Exception(SET_TEXT_COLOR_RED + "expected: <ID> <WHITE/BLACK>");
        }
        try {
            server.joinGame(authData, color, gameId);
            websocketFacade.joinGame(authData.username(), authData.authToken(), gameId);
        } catch (Exception e) {
            throw new Exception(SET_TEXT_COLOR_RED + "pick valid color");
        }

        return "joining game " + userId + "\n";
    }

    public static String getPiece(ChessPiece[][] board, int i, int j) {
        var piece = board[i][j];
        if(piece == null){
            return " ";
        }

        String color;
        if (piece.pieceColor == ChessGame.TeamColor.BLACK){
            color = SET_TEXT_COLOR_BLUE;
        }
        else{
            color = SET_TEXT_COLOR_RED;
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

    public static ChessPiece[][] flipBoard(ChessBoard board) {
        ChessPiece[][] tempBoard = new ChessBoard(board).board;
        int n = tempBoard.length - 1;
        for (int i = 0; i < (n + 1) / 2; i++) {
            for (int j = 0; j < n; j++) {
                ChessPiece temp = tempBoard[i][j];
                tempBoard[i][j] = tempBoard[n - i][n - j];
                tempBoard[n - i][n - j] = temp;
            }
        }
        return tempBoard;
    }

    public static String printBoard(ChessGame game, String tempColor) {

        ChessGame tempGame = new ChessGame(game);


        String board = "";
        var tempGameBoard = tempGame.board.board;

        if(Objects.equals(tempColor, "white")) {
            board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + "    a  b  c  d  e  f  g  h    " + RESET_BG_COLOR + "\n";
        }
        else{
            tempGameBoard = flipBoard(tempGame.board);
            board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + "    h  g  f  e  d  c  b  a    " + RESET_BG_COLOR + "\n";
        }




        for(int i = 8; i > 0; i--){
            for(int j = 0; j < 10; j++){
                if(j == 0 || j == 9){
                    board += printBoarder(tempColor, i);
                    if (j == 9){
                        board += RESET_BG_COLOR + "\n";
                    }
                }
                else{
                    if((i + j) % 2 == 0){
                        board += SET_BG_COLOR_BLACK + " " + getPiece(tempGameBoard, i, j) + " ";
                    }
                    else{
                        board += SET_BG_COLOR_WHITE + " " + getPiece(tempGameBoard, i, j) + " ";
                    }
                }
            }
        }
        if(Objects.equals(tempColor, "white")) {
            board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + "    a  b  c  d  e  f  g  h    " + RESET_BG_COLOR + "\n";
        }
        else{
            board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + "    h  g  f  e  d  c  b  a    " + RESET_BG_COLOR + "\n";
        }

        return board;
    }

    public static String printBoarder(String tempColor, int i){
        String board = "";
        if(Objects.equals(tempColor, "black")){
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
        }
        else{
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
        }


        return board;
    }


    private String observe(String[] params) throws Exception{

        int userId;
        try{
            userId = Integer.parseInt(params[0]);
        } catch (Exception e) {
            throw new Exception(SET_TEXT_COLOR_RED + "expected: <ID>");
        }

        GameInfo[] list = server.listGames(authData);

        if(list.length < userId || userId <= 0){
            throw new Exception(SET_TEXT_COLOR_RED + "enter valid index");
        }

        var game = list[userId - 1].gameData().game();
        int gameId = list[userId - 1].gameID();

        websocketFacade.joinGame(authData.username(), authData.authToken(), gameId);

        return "observing game " + userId + "\n" +  printBoard(game, "white");

    }

    private String logout() throws Exception {
        server.logout(authData);
        return "logged out";
    }
}
