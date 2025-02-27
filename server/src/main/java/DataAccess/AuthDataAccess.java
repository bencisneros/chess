package DataAccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;

public class AuthDataAccess {

    private final HashMap<String, AuthData> authDataMemory;

    public AuthDataAccess(){
        authDataMemory = new HashMap<>();
    }

    public void createAuthData(AuthData authData){
        authDataMemory.put(authData.username(), authData);
    }
}
