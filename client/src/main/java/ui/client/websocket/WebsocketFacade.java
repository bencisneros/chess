package ui.client.websocket;

import com.google.gson.Gson;
import com.sun.nio.sctp.NotificationHandler;
import server.websocket.ConnectionManager;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import websocket.commands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebsocketFacade extends Endpoint {

    private final ConnectionManager connections = new ConnectionManager();
    Session session;
    ui.client.websocket.NotificationHandler notificationHandler;


    public WebsocketFacade(String url, ui.client.websocket.NotificationHandler notificationHandler) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    if(message.contains("NOTIFICATION")){
                        NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
                        notificationHandler.notify(notification);
                    }
                    else if(message.contains("LOAD_GAME")){
                        LoadGameMessage notification = new Gson().fromJson(message, LoadGameMessage.class);
                        notificationHandler.notify(notification);
                    }
                    else if(message.contains("ERROR")){
                        ErrorMessage notification = new Gson().fromJson(message, ErrorMessage.class);
                        notificationHandler.notify(notification);
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void joinGame(String username, String authToken, int gameId) throws Exception {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameId);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new Exception(ex.getMessage());
        }
    }

}