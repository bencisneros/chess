package ui;

import ui.client.*;
import ui.client.websocket.NotificationHandler;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.*;

import java.util.Objects;
import java.util.Scanner;

public class Repl implements NotificationHandler {

    private final GameplayClient gameplayClient;
    private final PostLoginClient postLoginClient;
    private final PreLoginClient preLoginClient;

    public Repl(String serverUrl) throws Exception {
        gameplayClient = new GameplayClient(serverUrl);
        postLoginClient = new PostLoginClient(serverUrl, this);
        preLoginClient = new PreLoginClient(serverUrl);
    }

    public void run() {
        System.out.print(preLoginClient.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        int state = 0;
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            if (state == 0) {
                try {
                    result = preLoginClient.eval(line);
                    System.out.print(SET_TEXT_COLOR_YELLOW + result);
                    if(Objects.equals(result.split(" ")[0], "Signed")){
                        state = 1;
                        postLoginClient.setAuthData(preLoginClient.getAuthData());
                        gameplayClient.setAuthData(preLoginClient.getAuthData());
                    }
                } catch (Throwable e) {
                    var msg = e.toString();
                    System.out.print(msg);
                }
            }

            else if (state == 1){
                try{
                    result = postLoginClient.eval(line);
                    System.out.print(SET_TEXT_COLOR_YELLOW + result);
                    if(Objects.equals(result.split(" ")[0], "joining") || Objects.equals(result.split(" ")[0], "observing")){
                        state = 2;
                    }
                    else if (Objects.equals(result.split(" ")[0], "logged")){
                        state = 0;
                    }
                } catch (Throwable e) {
                    var msg = e.toString();
                    System.out.print(msg);
                }
            }

            else if (state == 2){
                try{
                    result = gameplayClient.eval(line);
                    System.out.print(SET_TEXT_COLOR_LIGHT_GREY + result);
                    if(Objects.equals(result.split(" ")[0], "leaving")){
                        state = 1;
                    }
                    else if(Objects.equals(result.split(" ")[0], "logged")){
                        state = 0;
                    }
                } catch (Throwable e) {
                    var msg = e.toString();
                    System.out.print(msg);
                }
            }


        }
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_BLUE);
    }

    @Override
    public void notify(ServerMessage notification) {
        System.out.println(SET_TEXT_COLOR_RED + notification);
        printPrompt();
    }
}
