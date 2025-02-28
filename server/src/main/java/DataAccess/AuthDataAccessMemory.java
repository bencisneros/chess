package DataAccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.UUID;

public class AuthDataAccessMemory {

    private static final HashMap<String, AuthData> authDataMemory = new HashMap<>();

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData createAuthData(UserData userData){
        String authToken = generateToken();
        AuthData authData = new AuthData(authToken, userData.username());
        authDataMemory.put(authData.username(), authData);
        return authData;
    }

    public void clearAuthData(){
        authDataMemory.clear();
    }

    public HashMap<String, AuthData> getAuthMap(){
        return authDataMemory;
    }
}
