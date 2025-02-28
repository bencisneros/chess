package DataAccess;

import model.AuthData;
import model.GameData;

import java.util.HashMap;

public class GameDataAccessMemory {
    private static final HashMap<Integer, GameData> gameDataMemory = new HashMap<>();
    private static int gameID = 1;

    public void clearGameData(){
        gameDataMemory.clear();
    }

    public void createGameData(GameData gameData){
        gameDataMemory.put(gameID, gameData);
        gameID++;
    }

    public HashMap<Integer, GameData> getGameMap(){
        return gameDataMemory;
    }
}
