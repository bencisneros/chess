package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.UUID;

public interface AuthDAO {
    AuthData createAuthData(UserData userData) throws Exception;
    void deleteAuth(AuthData authData) throws Exception;
    AuthData getAuth(String authToken) throws Exception;
    void clearAuthData() throws Exception;
}
