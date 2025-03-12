package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.UUID;

public interface AuthDAO {
    public AuthData createAuthData(UserData userData) throws Exception;
    public void deleteAuth(AuthData authData) throws Exception;
    public AuthData getAuth(String authToken) throws Exception;
    public void clearAuthData() throws Exception;
}
