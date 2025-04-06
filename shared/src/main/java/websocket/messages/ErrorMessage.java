package websocket.messages;

public class ErrorMessage extends ServerMessage{
    private final String message;

    public ErrorMessage(String error) {
        super(ServerMessageType.ERROR);
        this.message = error;
    }

    @Override
    public String getMessage(){
        return message;
    }
}