package websocket.messages;

import chess.ChessGame;

public class NotificationMessage extends ServerMessage{
    private final String notification;

    public NotificationMessage(String notification) {
        super(ServerMessageType.NOTIFICATION);
        this.notification = notification;
    }

    public String getNotification(){
        return notification;
    }
}
