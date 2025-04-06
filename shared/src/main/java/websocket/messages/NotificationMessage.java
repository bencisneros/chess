package websocket.messages;

import com.google.gson.Gson;

public class NotificationMessage extends ServerMessage{
    private final String message;

    public NotificationMessage(String notification) {
        super(ServerMessageType.NOTIFICATION);
        this.message = notification;
    }

    @Override
    public String getMessage(){
        return message;
    }
}
