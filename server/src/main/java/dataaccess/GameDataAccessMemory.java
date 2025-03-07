package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashMap;
import java.util.Objects;

public class GameDataAccessMemory implements GameDAO {
    private static final HashMap<Integer, GameData> GAME_DATA_MEMORY = new HashMap<>();
    private static int gameID = 1;

    public void clearGameData(){
        GAME_DATA_MEMORY.clear();
    }

    public GameData createGameData(String gameName) throws Exception{
        var gameData = new GameData(gameID, "", "", gameName, new ChessGame());

        for(GameData tempGameData : GAME_DATA_MEMORY.values()){
            if(Objects.equals(tempGameData.gameName(), gameName)){
                throw new DataAccessException("400 Error: Bad Request");
            }
        }

        GAME_DATA_MEMORY.put(gameID, gameData);
        gameID++;

        return gameData;
    }

    public GameData getGame(int gameID) throws Exception{
        if(!GAME_DATA_MEMORY.containsKey(gameID)){
            throw new DataAccessException("400 Error: bad request");
        }
        return GAME_DATA_MEMORY.get(gameID);
    }

    public void updateGame(String playerColor, String username, GameData gameData) throws Exception{
        if(Objects.equals(playerColor, "WHITE")) {
            if(!Objects.equals(gameData.whiteUsername(), "")){
                throw new AlreadyTaken("403 Error: already taken");
            }
            GameData newGameData = new GameData(gameData.gameID(), username, gameData.blackUsername(),
                                                gameData.gameName(), gameData.game());
            GAME_DATA_MEMORY.put(gameData.gameID(), newGameData);
        }
        else if(Objects.equals(playerColor, "BLACK")){
            if(!Objects.equals(gameData.blackUsername(), "")){
                throw new AlreadyTaken("403 Error: already taken");
            }
            GameData newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), username,
                                                gameData.gameName(), gameData.game());
            GAME_DATA_MEMORY.put(gameData.gameID(), newGameData);
        }
        else{
            throw new DataAccessException("400 Error: bad request");
        }
    }

    public HashMap<Integer, GameData> getGameMap(){
        return GAME_DATA_MEMORY;
    }


}
