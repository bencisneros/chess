package service;

import dataaccess.*;

public class ClearService {

    private final AuthDAO authDataAccessMemory;
    private final GameDAO gameDataAccessMemory;
    private final UserDAO userDataAccessMemory;

    public ClearService() throws Exception{
        authDataAccessMemory = new AuthDatabase(); // new AuthDataAccessMemory();
        gameDataAccessMemory = new GameDatabase(); // new GameDataAccessMemory();
        userDataAccessMemory = new UserDatabase(); // new UserDataAccessMemory();

    }
    public void clear(){
        authDataAccessMemory.clearAuthData();
        gameDataAccessMemory.clearGameData();
        userDataAccessMemory.clearUserData();
    }

}
