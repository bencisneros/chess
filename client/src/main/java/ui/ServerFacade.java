package ui;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

public class ServerFacade {

    private final String serverUrl;
    public record GameInfo(int gameID, String whiteUsername, String blackUsername, String gameName, GameData gameData) {}

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public AuthData register(UserData userData) throws Exception {
        try {
            String path = "/user";
            return makeRequest("POST", path, null, userData, AuthData.class);
        } catch (ResponseException e) {
            throw new Exception("username already taken");
        }
    }

    public AuthData login(UserData userData) throws Exception{
        try {
            String path = "/session";
            return makeRequest("POST", path, null, userData, AuthData.class);
        } catch (ResponseException e) {
            throw new Exception("incorrect username or password");
        }
    }

    public void logout(AuthData authData) throws Exception{
        String path = "/session";
        makeRequest("DELETE", path, authData.authToken(), null, null);
    }

    public GameData createGame(AuthData authData, String gameName) throws Exception{
        String path = "/game";
        HashMap<String, String> gameRequest = new HashMap<>();
        gameRequest.put("gameName", gameName);
        return makeRequest("POST", path, authData.authToken(), gameRequest, GameData.class);
    }

    public GameInfo[] listGames(AuthData authData)throws Exception{
        String path = "/game";
        record GameList(GameInfo[] games){}
        var gameList = makeRequest("GET", path, authData.authToken(), null, GameList.class);
        return gameList.games;
    }

    public void joinGame(AuthData authData, String playerColor, int gameId) throws Exception{
        record JoinGameData(String playerColor, int gameID){}
        JoinGameData joinRequest = new JoinGameData(playerColor.toUpperCase(), gameId);
        String path = "/game";
        makeRequest("PUT", path, authData.authToken(), joinRequest, null);
    }

    private <T> T makeRequest(String method, String path, String header, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if(header != null) {
                http.setRequestProperty("Authorization", header);
            }
            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }

            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

}