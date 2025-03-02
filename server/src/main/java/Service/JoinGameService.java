package Service;

import DataAccess.AlreadyTaken;
import DataAccess.AuthDataAccessMemory;
import DataAccess.GameDataAccessMemory;
import DataAccess.Unauthorized;
import chess.ChessGame;
import model.GameData;

import java.util.Objects;

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

