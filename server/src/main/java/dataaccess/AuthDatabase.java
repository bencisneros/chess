package dataaccess;

import model.AuthData;
import model.UserData;

public class AuthDatabase implements AuthDAO{

    public AuthDatabase() throws Exception{
        DatabaseManager databaseManager = new DatabaseManager();
        databaseManager.configureDatabase();
    }

    public AuthData createAuthData(UserData userData) {
        return null;
    }

    public void deleteAuth(AuthData authData) {

    }

    public AuthData getAuth(String authToken) {
        return null;
    }

    public void clearAuthData() {

    }
}
