package service;

import dataaccess.*;

public class ClearService {

    private final AuthDAO authDataAccessMemory;
    private final GameDAO gameDataAccessMemory;
    private final UserDAO userDataAccessMemory;

    public ClearService(){
        authDataAccessMemory = new AuthDataAccessMemory();
        gameDataAccessMemory = new GameDataAccessMemory();
        userDataAccessMemory = new UserDataAccessMemory();

    }
    public void clear(){
        authDataAccessMemory.clearAuthData();
        gameDataAccessMemory.clearGameData();
        userDataAccessMemory.clearUserData();
    }

}
