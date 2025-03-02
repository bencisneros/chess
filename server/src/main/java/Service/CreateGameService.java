package Service;

import DataAccess.AuthDataAccessMemory;
import DataAccess.DataAccessException;
import DataAccess.GameDataAccessMemory;
import DataAccess.Unauthorized;
import chess.ChessGame;
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
