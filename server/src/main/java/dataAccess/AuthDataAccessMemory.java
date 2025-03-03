package dataAccess;

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
        authDataMemory.put(authToken, authData);
        return authData;
    }

    public void deleteAuth(AuthData authData){
        authDataMemory.remove(authData.authToken());
    }

    public AuthData getAuth(String authToken){
        if(!authDataMemory.containsKey(authToken)){
            return null;
        }
        return authDataMemory.get(authToken);
    }

    public void clearAuthData(){
        authDataMemory.clear();
    }

    public HashMap<String, AuthData> getAuthMap(){
        return authDataMemory;
    }
}
