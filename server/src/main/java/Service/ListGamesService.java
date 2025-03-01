package Service;

import DataAccess.AuthDataAccessMemory;
import DataAccess.GameDataAccessMemory;
import DataAccess.Unauthorized;
import model.GameData;

import java.util.ArrayList;

public class ListGamesService {
    public ListGamesService(){
    }

    record gameInfo(int gameID, String whiteUsername, String blackUserName, String gameName) {}

    public ArrayList<gameInfo> listGames(String authToken) throws Exception{
        var listOfGames = new ArrayList<gameInfo>();
        AuthDataAccessMemory authDAO = new AuthDataAccessMemory();
        GameDataAccessMemory gameDAO = new GameDataAccessMemory();

        if(authDAO.getAuth(authToken) == null){
            throw new Unauthorized("401 Error: unauthorized");
        }

        var gamesMap = gameDAO.getGameMap();
        for(GameData gameData : gamesMap.values()){
            int ID = gameData.gameID();
            String whiteUserName = gameData.whiteUsername();
            String blackUsername = gameData.blackUsername();
            String gameName = gameData.gameName();
            listOfGames.add(new gameInfo(ID, whiteUserName, blackUsername, gameName));
        }

        return listOfGames;

    }


}

