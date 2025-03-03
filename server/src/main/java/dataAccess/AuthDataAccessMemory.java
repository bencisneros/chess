package dataAccess;

import model.AuthData;
import model.UserData;

import java.util.HashMap;
import java.util.UUID;

public class AuthDataAccessMemory {

    private static final HashMap<String, AuthData> AUTH_DATA_MEMORY = new HashMap<>();

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData createAuthData(UserData userData){
        String authToken = generateToken();
        AuthData authData = new AuthData(authToken, userData.username());
        AUTH_DATA_MEMORY.put(authToken, authData);
        return authData;
    }

    public void deleteAuth(AuthData authData){
        AUTH_DATA_MEMORY.remove(authData.authToken());
    }

    public AuthData getAuth(String authToken){
        if(!AUTH_DATA_MEMORY.containsKey(authToken)){
            return null;
        }
        return AUTH_DATA_MEMORY.get(authToken);
    }

    public void clearAuthData(){
        AUTH_DATA_MEMORY.clear();
    }

    public HashMap<String, AuthData> getAuthMap(){
        return AUTH_DATA_MEMORY;
    }
}
