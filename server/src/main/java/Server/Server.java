package Server;

import DataAccess.DataAccessException;
import DataAccess.NotEnoughInfo;
import com.google.gson.JsonSyntaxException;
import model.AuthData;
import model.UserData;
import com.google.gson.Gson;
import spark.*;
import Service.*;

import java.util.Map;

public class Server {

    private final RegisterService registerService;
    private final ClearService clearService;

    public Server(){
        registerService = new RegisterService();
        clearService = new ClearService();

    }


    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::handleRegister);
        Spark.delete("/db", this::handleClear);
        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

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
            if (e instanceof DataAccessException) {
                String errorMessage = e.getMessage();
                res.status(403);
                return new Gson().toJson(Map.of("message:", errorMessage));
            } else if (e instanceof NotEnoughInfo) {
                String errorMessage = e.getMessage();
                res.status(400);
                return new Gson().toJson(Map.of("message:", errorMessage));
            }
            else{
                String errorMessage = e.getMessage();
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

}
