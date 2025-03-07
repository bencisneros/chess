package service;

import dataaccess.*;
import model.GameData;

public class CreateGameService {


    public CreateGameService(){
    }

    public GameData createGame(String gameName, String authToken) throws Exception{
        AuthDAO authDAO = new AuthDatabase(); //new AuthDataAccessMemory();
        GameDAO gameDAO = new GameDatabase(); //new GameDataAccessMemory();

        var authData = authDAO.getAuth(authToken);
        if(authData == null){
            throw new Unauthorized("401 Error: unauthorized");
        }

        return gameDAO.createGameData(gameName);

    }

}
