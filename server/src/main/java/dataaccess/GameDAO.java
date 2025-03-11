package dataaccess;

import model.GameData;

import java.util.HashMap;

public interface GameDAO {
    public void clearGameData() throws Exception;
    public GameData createGameData(String gameName) throws Exception;
    public GameData getGame(int gameID) throws Exception;
    public void updateGame(String playerColor, String username, GameData gameData) throws Exception;
    public HashMap<Integer, GameData> getGameMap();
}
