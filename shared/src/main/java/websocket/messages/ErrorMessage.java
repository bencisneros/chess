package websocket.messages;

public class ErrorMessage extends ServerMessage{
    private final String errorMessage;

    public ErrorMessage(String error) {
        super(ServerMessageType.ERROR);
        errorMessage = error;
    }

    @Override
    public String getMessage(){
        return errorMessage;
    }
}