package dataaccess;

import model.GameData;

import java.util.HashMap;

public interface GameDAO {
    void clearGameData() throws Exception;
    GameData createGameData(String gameName) throws Exception;
    GameData getGame(int gameID) throws Exception;
    void updateGame(String playerColor, String username, GameData gameData) throws Exception;
    HashMap<Integer, GameData> getGameMap() throws Exception;
}
