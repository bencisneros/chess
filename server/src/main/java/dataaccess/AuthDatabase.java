package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.ResultSet;
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

    public void deleteAuth(AuthData authData) throws Exception {
        String authToken = authData.authToken();
        var statement = "DELETE FROM authData WHERE authToken=?";
        databaseManager.executeUpdate(statement, authToken);
    }

    public AuthData getAuth(String authToken) throws Exception {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username FROM authData WHERE authToken=?";
            try (var c = conn.prepareStatement(statement)) {
                c.setString(1,authToken);
                try (var rs = c.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    private AuthData readAuth(ResultSet rs) throws Exception{
        String authToken = rs.getString("authToken");
        String username = rs.getString("username");
        return new AuthData(authToken, username);
    }

    public void clearAuthData() throws Exception {
        String statement = "TRUNCATE authData";
        databaseManager.executeUpdate(statement);
    }
}
