package service;

import dataaccess.AuthDataAccessMemory;
import dataaccess.GameDataAccessMemory;
import dataaccess.Unauthorized;
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
