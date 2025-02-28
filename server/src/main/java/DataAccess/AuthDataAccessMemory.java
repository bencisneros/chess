package DataAccess;

import model.AuthData;

import java.util.HashMap;

public class AuthDataAccessMemory {

    private static final HashMap<String, AuthData> authDataMemory = new HashMap<>();

    public void createAuthData(AuthData authData){
        authDataMemory.put(authData.username(), authData);
    }
    public void clearAuthData(){
        authDataMemory.clear();
    }
    public HashMap<String, AuthData> getAuthMap(){
        return authDataMemory;
    }
}
