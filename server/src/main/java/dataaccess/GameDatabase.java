package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.util.HashMap;

public class GameDatabase implements GameDAO{

    private static int gameId = 0;

    private final DatabaseManager databaseManager;

    public GameDatabase() throws Exception{
        databaseManager = new DatabaseManager();
        databaseManager.configureDatabase();
    }

    public void clearGameData() throws Exception {
        String statement = "TRUNCATE gameData";
        databaseManager.executeUpdate(statement);
    }

    public GameData createGameData(String gameName) throws Exception {
        gameId++;
        var statement = "INSERT INTO gameData (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)";
        String whiteUsername = "";
        String blackUsername = "";
        ChessGame chessGame = new ChessGame();
        String gsonGame = new Gson().toJson(chessGame);
        int size = gsonGame.length();
        databaseManager.executeUpdate(statement, gameId, whiteUsername, blackUsername, gameName, gsonGame);
        return new GameData(gameId, whiteUsername, blackUsername, gameName, chessGame);
    }

    public GameData getGame(int gameID) throws Exception {
        return null;
    }

    public void updateGame(String playerColor, String username, GameData gameData) throws Exception {

    }

    public HashMap<Integer, GameData> getGameMap() {
        return null;
    }
}
