package Server;

import DataAccess.DataAccessException;
import DataAccess.NotEnoughInfo;
import DataAccess.Unauthorized;
import com.google.gson.JsonSyntaxException;
import model.AuthData;
import model.GameData;
import model.UserData;
import com.google.gson.Gson;
import spark.*;
import Service.*;

import java.util.Map;

public class Server {

    private final RegisterService registerService;
    private final ClearService clearService;
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final CreateGameService createGameService;

    public Server(){
        registerService = new RegisterService();
        clearService = new ClearService();
        loginService = new LoginService();
        logoutService = new LogoutService();
        createGameService = new CreateGameService();
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
            var authToken = new Gson().fromJson(req.headers("authorization"), String.class);
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
            String authToken = new Gson().fromJson(req.headers("authorization"), String.class);
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
}
