package server;

import dataAccess.AlreadyTaken;
import dataAccess.DataAccessException;
import dataAccess.NotEnoughInfo;
import dataAccess.Unauthorized;
import model.GameData;
import model.UserData;
import com.google.gson.Gson;
import spark.*;
import service.*;

import java.util.Map;

public class Server {

    private final RegisterService registerService;
    private final ClearService clearService;
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final CreateGameService createGameService;
    private final ListGamesService listGamesService;
    private final JoinGameService joinGameService;

    public Server(){
        registerService = new RegisterService();
        clearService = new ClearService();
        loginService = new LoginService();
        logoutService = new LogoutService();
        createGameService = new CreateGameService();
        listGamesService = new ListGamesService();
        joinGameService = new JoinGameService();
    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::handleRegister);
        Spark.delete("/db", this::handleClear);
        Spark.post("/session", this::handleLogin);
        Spark.delete("/session", this::handleLogout);
        Spark.post("/game", this::handleCreateGame);
        Spark.get("/game", this::handleListGames);
        Spark.put("/game", this::handleJoinGame);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object handleRegister(Request req, Response res) {
        try {
            var user = new Gson().fromJson(req.body(), UserData.class);
            var auth = registerService.register(user);
            return new Gson().toJson(auth);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if (e instanceof DataAccessException) {
                res.status(403);
                return new Gson().toJson(Map.of("message", errorMessage));
            } else if (e instanceof NotEnoughInfo) {
                res.status(400);
                return new Gson().toJson(Map.of("message", errorMessage));
            }
            else{
                res.status(500);
                return new Gson().toJson(Map.of("message:", errorMessage));
            }
        }
    }

    private Object handleClear(Request req, Response res){
        try{
            clearService.clear();
            return new Gson().toJson(new Object());
        }
        catch (Exception e){
            String errorMessage = e.getMessage();
            res.status(500);
            return new Gson().toJson(Map.of("message:", errorMessage));
        }
    }

    private Object handleLogin(Request req, Response res){
        try {
            var user = new Gson().fromJson(req.body(), UserData.class);
            var auth = loginService.login(user);
            return new Gson().toJson(auth);
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if(e instanceof DataAccessException){
                res.status(401);
                return new Gson().toJson(Map.of("message", errorMessage));
            }
            else{
                res.status(500);
                return new Gson().toJson(Map.of("message:", errorMessage));
            }
        }
    }

    private Object handleLogout(Request req, Response res){
        try {
            String authToken = req.headers("authorization");
            logoutService.logout(authToken);
            return new Gson().toJson(new Object());
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if(e instanceof Unauthorized){
                res.status(401);
                return new Gson().toJson(Map.of("message", errorMessage));
            }
            else{
                res.status(500);
                return new Gson().toJson(Map.of("message:", errorMessage));
            }
        }
    }

    private Object handleCreateGame(Request req, Response res){
        try {
            String authToken = req.headers("authorization");
            GameData tempGameData = new Gson().fromJson(req.body(), GameData.class);
            var gameData = createGameService.createGame(tempGameData.gameName(), authToken);
            return new Gson().toJson(Map.of("gameID", gameData.gameID()));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if(e instanceof Unauthorized){
                res.status(401);
                return new Gson().toJson(Map.of("message", errorMessage));
            }
            else if(e instanceof DataAccessException){
                res.status(400);
                return new Gson().toJson(Map.of("message", errorMessage));
            }
            else{
                res.status(500);
                return new Gson().toJson(Map.of("message:", errorMessage));
            }
        }
    }

    private Object handleListGames(Request req, Response res){
        try {
            String authToken = req.headers("authorization");
            var listOfGames = listGamesService.listGames(authToken);
            return new Gson().toJson(Map.of("games", listOfGames));
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if(e instanceof Unauthorized){
                res.status(401);
            }
            else{
                res.status(500);
            }
            return new Gson().toJson(Map.of("message", errorMessage));
        }
    }

    private Object handleJoinGame(Request req, Response res){
        record JoinGameData(String playerColor, int gameID){}
        try {
            String authToken = req.headers("authorization");
            JoinGameData joinGameData = new Gson().fromJson(req.body(), JoinGameData.class);
            joinGameService.joinGame(joinGameData.playerColor(), joinGameData.gameID(), authToken);
            return new Gson().toJson(new Object());
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            if(e instanceof Unauthorized){
                res.status(401);
                return new Gson().toJson(Map.of("message", errorMessage));
            }
            else if(e instanceof AlreadyTaken){
                res.status(403);
                return new Gson().toJson(Map.of("message", errorMessage));
            }
            else if(e instanceof DataAccessException){
                res.status(400);
                return new Gson().toJson(Map.of("message", errorMessage));
            }
            else{
                res.status(500);
                return new Gson().toJson(Map.of("message", errorMessage));
            }
        }
    }

}
