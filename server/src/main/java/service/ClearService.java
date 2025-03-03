package service;

import dataaccess.AuthDataAccessMemory;
import dataaccess.GameDataAccessMemory;
import dataaccess.UserDataAccessMemory;

public class ClearService {

    private final AuthDataAccessMemory authDataAccessMemory;
    private final GameDataAccessMemory gameDataAccessMemory;
    private final UserDataAccessMemory userDataAccessMemory;

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
