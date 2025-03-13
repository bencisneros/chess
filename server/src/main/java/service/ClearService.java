package service;

import dataaccess.*;

public class ClearService {

    private final AuthDAO authDataAccessMemory;
    private final GameDAO gameDataAccessMemory;
    private final UserDAO userDataAccessMemory;

    public ClearService() throws Exception{
        authDataAccessMemory = new AuthDatabase();
        gameDataAccessMemory = new GameDatabase();
        userDataAccessMemory = new UserDatabase();

    }
    public void clear() throws Exception{
        authDataAccessMemory.clearAuthData();
        gameDataAccessMemory.clearGameData();
        userDataAccessMemory.clearUserData();
    }

}
