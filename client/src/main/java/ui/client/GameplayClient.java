package ui.client;

import chess.*;
import model.AuthData;
import model.GameData;
import ui.ServerFacade;
import ui.client.websocket.NotificationHandler;
import ui.client.websocket.WebsocketFacade;
import static ui.EscapeSequences.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class GameplayClient {

    private AuthData authData = null;
    private ServerFacade server;
    public String color;
    public int gameId;
    private final WebsocketFacade websocketFacade;


    public GameplayClient(String serverUrl, NotificationHandler notificationHandler) throws Exception {
        server = new ServerFacade(serverUrl);
        websocketFacade = new WebsocketFacade(serverUrl, notificationHandler);
    }

    public String help() {
        return  "redraw\n" +
                "leave\n" +
                "move <x#> <x#> <promotionPiece> (promotion piece can be left blank)\n" +
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

    public void setGameId(int id){
        gameId = id;
    }

    private String highlightMoves(String[] params) throws Exception {
        setColor();
        if(params.length != 1){
            throw new Exception(SET_TEXT_COLOR_RED + "expected: <#x>");
        }

        char letter1;
        int startRow;
        try {
            letter1 = params[0].charAt(0);
            startRow = Character.getNumericValue(params[0].charAt(1));
        } catch (Exception e) {
            throw new Exception(SET_TEXT_COLOR_RED + "expected: <#x>");
        }

        if(startRow  < 1 || startRow > 8){
            throw new Exception(SET_TEXT_COLOR_RED + "expected: <#x>");
        }

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
            default -> throw new Exception(SET_TEXT_COLOR_RED + "expected: <#x>");
        }

        ChessPosition start = new ChessPosition(startRow, startCol);

        return printValidMoves(start);

    }

    private String resign() throws Exception{
        websocketFacade.resign(authData.username(), authData.authToken(), gameId);
        return "";
    }

    private String move(String[] params) throws Exception {
        if(params.length != 2 && params.length != 3){
            throw new Exception(SET_TEXT_COLOR_RED + "expected: <#x> <#x> <promotionPiece> (promotion piece can be left blank)");
        }


        char letter1;
        int startRow;
        try {
            letter1 = params[0].charAt(0);
            startRow = Character.getNumericValue(params[0].charAt(1));
        } catch (Exception e) {
            throw new Exception(SET_TEXT_COLOR_RED + "expected: <#x> <#x> <promotionPiece> (promotion piece can be left blank)");
        }

        if(startRow  < 1 || startRow > 8){
            throw new Exception(SET_TEXT_COLOR_RED + "expected: <#x> <#x> <promotionPiece> (promotion piece can be left blank)");
        }

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
            default -> throw new Exception(SET_TEXT_COLOR_RED + "expected: <#x> <#x> <promotionPiece> (promotion piece can be left blank)");
        }

        ChessPosition start = new ChessPosition(startRow, startCol);

        char letter2;
        int endRow;
        try {
            letter2 = params[1].charAt(0);
            endRow = Character.getNumericValue(params[1].charAt(1));
        } catch (Exception e) {
            throw new Exception(SET_TEXT_COLOR_RED + "expected: <#x> <#x> <promotionPiece> (promotion piece can be left blank)");
        }

        if(endRow < 1 || endRow > 8){
            throw new Exception(SET_TEXT_COLOR_RED + "expected: <#x> <#x> <promotionPiece> (promotion piece can be left blank)");
        }

        int endCol = 0;
        switch (letter2){
            case 'a' -> endCol = 1;
            case 'b' -> endCol = 2;
            case 'c' -> endCol = 3;
            case 'd' -> endCol = 4;
            case 'e' -> endCol = 5;
            case 'f' -> endCol = 6;
            case 'g' -> endCol = 7;
            case 'h' -> endCol = 8;
            default -> throw new Exception(SET_TEXT_COLOR_RED + "expected: <#x> <#x> <promotionPiece> (promotion piece can be left blank)");
        }

        ChessPosition end = new ChessPosition(endRow, endCol);


        ChessMove move = getChessMove(params, start, end);


        websocketFacade.makeMove(authData.username(), authData.authToken(), gameId, move);
        return "";
    }

    private static ChessMove getChessMove(String[] params, ChessPosition start, ChessPosition end) throws Exception {
        String promotionPieceString;
        ChessPiece.PieceType pieceType = null;

        if(params.length == 3) {
            promotionPieceString = params[2];
        }
        else{
            promotionPieceString = null;
        }


        switch (promotionPieceString){
            case "rook" -> pieceType = ChessPiece.PieceType.ROOK;
            case "bishop" -> pieceType = ChessPiece.PieceType.BISHOP;
            case "knight" -> pieceType = ChessPiece.PieceType.KNIGHT;
            case "queen" -> pieceType = ChessPiece.PieceType.QUEEN;
            case null  -> pieceType = null;
            default -> throw new Exception(SET_TEXT_COLOR_RED + "invalid promotion piece");
        }

        ChessMove move = new ChessMove(start, end, pieceType);
        return move;
    }

    private String redraw() throws Exception {
        var list = server.listGames(authData);
        ChessGame game = null;
        for(ServerFacade.GameInfo gameInfo : list){
            game = gameInfo.gameData().game();
        }
        setColor();
        return PostLoginClient.printBoard(game, color);
    }

    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

    public void setColor() throws Exception {
        String username = authData.username();
        var list = server.listGames(authData);
        ChessGame game = null;
        GameData gameData = null;
        for(ServerFacade.GameInfo gameInfo : list){
            game = gameInfo.gameData().game();
            gameData = gameInfo.gameData();
        }
        if(Objects.equals(username, gameData.blackUsername())){
            color = "black";
        }
        else{
            color = "white";
        }
    }

    private String leave() throws Exception {
        websocketFacade.leave(authData.username(), authData.authToken(), gameId);
        return "leaving game";
    }

    private String getPiece(ChessPiece[][] board, int i, int j) {
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

    private ChessPiece[][] flipBoard(ChessBoard board) {
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

    private ArrayList<ChessPosition> flipPositions(ArrayList<ChessPosition> positions){
        ArrayList<ChessPosition> tempArray = new ArrayList<>();

        for(ChessPosition position : positions){
            int tempRow = 9 - position.row;
            int tempCol = 9 - position.col;
            tempArray.add(new ChessPosition(tempRow, tempCol));
        }

        return tempArray;
    }

    public String printValidMoves(ChessPosition position) throws Exception {
        var list = server.listGames(authData);
        ChessGame game = null;
        for(ServerFacade.GameInfo gameInfo : list){
            game = gameInfo.gameData().game();
        }

        if(game.getBoard().getPiece(position) == null){
            throw new Exception(SET_TEXT_COLOR_RED + "pick valid piece");
        }

        var validMoves = game.validMoves(position);

        ArrayList<ChessPosition> validEnds = new ArrayList<>();

        for(ChessMove moves : validMoves){
            validEnds.add(moves.endPosition);
        }


        String board = "";
        var tempGameBoard = game.board.board;


        if(Objects.equals(color, "white")) {
            board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + "    a  b  c  d  e  f  g  h    " + RESET_BG_COLOR + "\n";
        }
        else{
            tempGameBoard = flipBoard(game.board);
            validEnds = flipPositions(validEnds);
            board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + "    h  g  f  e  d  c  b  a    " + RESET_BG_COLOR + "\n";
        }




        for(int i = 8; i > 0; i--){
            for(int j = 0; j < 10; j++){
                var currentPosition = new ChessPosition(i,j);
                if(j == 0 || j == 9){
                    board += printBoarder(color, i);
                    if (j == 9){
                        board += RESET_BG_COLOR + "\n";
                    }
                }
                else{
                    if((i + j) % 2 == 0 && validEnds.contains(currentPosition)){
                        board += SET_BG_COLOR_DARK_GREEN + " " + getPiece(tempGameBoard, i, j) + " ";
                    }
                    else if((i + j) % 2 == 0){
                        board += SET_BG_COLOR_BLACK + " " + getPiece(tempGameBoard, i, j) + " ";
                    }
                    else if((i + j) % 2 == 1 && validEnds.contains(currentPosition)){
                        board += SET_BG_COLOR_GREEN + " " + getPiece(tempGameBoard, i, j) + " ";
                    }
                    else{
                        board += SET_BG_COLOR_WHITE + " " + getPiece(tempGameBoard, i, j) + " ";
                    }
                }
            }
        }
        if(Objects.equals(color, "white")) {
            board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + "    a  b  c  d  e  f  g  h    " + RESET_BG_COLOR + "\n";
        }
        else{
            board += SET_BG_COLOR_DARK_GREY + RESET_TEXT_COLOR + "    h  g  f  e  d  c  b  a    " + RESET_BG_COLOR + "\n";
        }

        return board;
    }

    private String printBoarder(String tempColor, int i){
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

}
