package websocket.messages;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import java.util.Objects;

import static websocket.EscapeSequences.*;

public class LoadGameMessage extends ServerMessage{
    private final ChessGame game;
    private String color;

    public LoadGameMessage(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    @Override
    public String getMessage(){
        return "\n" + printBoard(game, color);
    }

    public void setColor(String color){
        if(Objects.equals(color, "an observer")){
            color = "white";
        }
        this.color = color;
    }

    public ChessGame getGame() {
        return game;
    }

    private static String getPiece(ChessPiece[][] board, int i, int j) {
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

    private static ChessPiece[][] flipBoard(ChessBoard board) {
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
                        board += SET_BG_COLOR_WHITE + " " + getPiece(tempGameBoard, i, j) + " ";
                    }
                    else{
                        board += SET_BG_COLOR_BLACK + " " + getPiece(tempGameBoard, i, j) + " ";
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

    private static String printBoarder(String tempColor, int i){
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
