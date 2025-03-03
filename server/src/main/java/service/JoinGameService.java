package service;

import dataAccess.AuthDataAccessMemory;
import dataAccess.GameDataAccessMemory;
import dataAccess.Unauthorized;

public class JoinGameService {

    public JoinGameService(){
    }

    public void joinGame(String playerColor, int gameID, String authToken) throws Exception{
        AuthDataAccessMemory authDAO = new AuthDataAccessMemory();
        GameDataAccessMemory gameDAO = new GameDataAccessMemory();

        var authData = authDAO.getAuth(authToken);
        if(authData == null){
            throw new Unauthorized("401 Error: unauthorized");
        }

        var gameData = gameDAO.getGame(gameID);
        gameDAO.updateGame(playerColor, authData.username(), gameData);
    }

}

