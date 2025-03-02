package DataAccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;

import java.util.HashMap;
import java.util.Objects;

public class GameDataAccessMemory {
    private static final HashMap<Integer, GameData> gameDataMemory = new HashMap<>();
    private static int gameID = 1;

    public void clearGameData(){
        gameDataMemory.clear();
    }

    public GameData createGameData(String gameName) throws Exception{
        var gameData = new GameData(gameID, "", "", gameName, new ChessGame());

        for(GameData tempGameData : gameDataMemory.values()){
            if(Objects.equals(tempGameData.gameName(), gameName)){
                throw new DataAccessException("400 Error: Bad Request");
            }
        }

        gameDataMemory.put(gameID, gameData);
        gameID++;

        return gameData;
    }

    public HashMap<Integer, GameData> getGameMap(){
        return gameDataMemory;
    }
}
