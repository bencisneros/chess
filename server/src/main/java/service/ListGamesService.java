package service;

import dataAccess.AuthDataAccessMemory;
import dataAccess.GameDataAccessMemory;
import dataAccess.Unauthorized;
import model.GameData;

import java.util.ArrayList;
import java.util.Objects;

public class ListGamesService {
    public ListGamesService(){
    }

    public record GameInfo(int gameID, String whiteUsername, String blackUsername, String gameName) {}

    public ArrayList<GameInfo> listGames(String authToken) throws Exception{
        var listOfGames = new ArrayList<GameInfo>();
        AuthDataAccessMemory authDAO = new AuthDataAccessMemory();
        GameDataAccessMemory gameDAO = new GameDataAccessMemory();

        if(authDAO.getAuth(authToken) == null){
            throw new Unauthorized("401 Error: unauthorized");
        }

        var gamesMap = gameDAO.getGameMap();
        for(GameData gameData : gamesMap.values()){
            int ID = gameData.gameID();
            String whiteUserName = gameData.whiteUsername();
            if(Objects.equals(gameData.whiteUsername(), "")){
                whiteUserName = null;
            }
            String blackUsername = gameData.blackUsername();
            if (Objects.equals(gameData.blackUsername(), "")){
                blackUsername = null;
            }
            String gameName = gameData.gameName();
            listOfGames.add(new GameInfo(ID, whiteUserName, blackUsername, gameName));
        }

        return listOfGames;

    }


}

