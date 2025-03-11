package dataaccess;

import model.AuthData;
import model.UserData;

import java.util.UUID;

public class AuthDatabase implements AuthDAO{

    private final DatabaseManager databaseManager;

    public AuthDatabase() throws Exception{
        databaseManager = new DatabaseManager();
        databaseManager.configureDatabase();
    }

    private static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public AuthData createAuthData(UserData userData) throws Exception {
        var statement = "INSERT INTO authData (authToken, username) VALUES (?, ?)";
        String authToken = generateToken();
        String username = userData.username();
        databaseManager.executeUpdate(statement, authToken, username);
        return new AuthData(authToken, username);
    }

    public void deleteAuth(AuthData authData) {

    }

    public AuthData getAuth(String authToken) {
        return null;
    }

    public void clearAuthData() throws Exception {
        String statement = "TRUNCATE authData";
        databaseManager.executeUpdate(statement);
    }
}
