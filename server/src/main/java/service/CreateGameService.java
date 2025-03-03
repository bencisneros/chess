package service;

import dataAccess.AuthDataAccessMemory;
import dataAccess.GameDataAccessMemory;
import dataAccess.Unauthorized;
import model.GameData;

public class CreateGameService {


    public CreateGameService(){
    }

    public GameData createGame(String gameName, String authToken) throws Exception{
        AuthDataAccessMemory authDAO = new AuthDataAccessMemory();
        GameDataAccessMemory gameDAO = new GameDataAccessMemory();

        var authData = authDAO.getAuth(authToken);
        if(authData == null){
            throw new Unauthorized("401 Error: unauthorized");
        }

        return gameDAO.createGameData(gameName);

    }

}
