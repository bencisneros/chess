package websocket.messages;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import java.util.Objects;

import static websocket.EscapeSequences.*;

public class LoadGameMessage extends ServerMessage{
    private final ChessGame game;
    private final String color;

    public LoadGameMessage(ChessGame game, String color) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        this.color = color;
    }

    @Override
    public String getMessage(){
        return "";
    }

    public String getColor(){
        return color;
    }

    public ChessGame getGame() {
        return game;
    }

}
