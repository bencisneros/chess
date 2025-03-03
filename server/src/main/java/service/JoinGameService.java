package service;

import dataaccess.AuthDataAccessMemory;
import dataaccess.GameDataAccessMemory;
import dataaccess.Unauthorized;

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

