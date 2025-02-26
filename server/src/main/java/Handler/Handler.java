package Handler;

import DataAccess.UserDataAccess;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class Handler {
    private Object register(Request req, Response res) throws ResponseException {
        var user = new Gson().fromJson(req.body(), UserDataAccess.class);
        user = service.addPet(pet);
        webSocketHandler.makeNoise(pet.name(), pet.sound());
        return new Gson().toJson(pet);
    }
}
