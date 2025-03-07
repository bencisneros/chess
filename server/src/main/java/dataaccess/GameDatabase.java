package dataaccess;

import model.GameData;

import java.util.HashMap;

public class GameDatabase implements GameDAO{

    public GameDatabase() throws Exception{
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.configureDatabase();
    }

    public void clearGameData() {

    }

    public GameData createGameData(String gameName) throws Exception {
        return null;
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
