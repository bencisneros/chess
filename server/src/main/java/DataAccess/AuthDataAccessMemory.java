package DataAccess;

import model.AuthData;

import java.util.HashMap;

public class AuthDataAccessMemory {

    private final HashMap<String, AuthData> authDataMemory;

    public AuthDataAccessMemory(){
        authDataMemory = new HashMap<>();
    }

    public void createAuthData(AuthData authData){
        authDataMemory.put(authData.username(), authData);
    }
}
