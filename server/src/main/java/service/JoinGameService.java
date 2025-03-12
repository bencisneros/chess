package service;

import dataaccess.*;

public class JoinGameService {

    public JoinGameService(){
    }

    public void joinGame(String playerColor, int gameID, String authToken) throws Exception{
        AuthDAO authDAO = new AuthDatabase(); // new AuthDataAccessMemory();
        GameDAO gameDAO = new GameDatabase();// new GameDataAccessMemory();

        var authData = authDAO.getAuth(authToken);
        if(authData == null){
            throw new Unauthorized("401 Error: unauthorized");
        }

        var gameData = gameDAO.getGame(gameID);
        if(gameData == null){
            throw new DataAccessException("400 Error: unauthorized");
        }
        gameDAO.updateGame(playerColor, authData.username(), gameData);
    }

}

