package dataaccess;

import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.ResultSet;

public class UserDatabase implements UserDAO{

    private final DatabaseManager databaseManager;

    public UserDatabase() throws Exception{
        databaseManager = new DatabaseManager();
        databaseManager.configureDatabase();
    }

    public UserData getUser(UserData user) throws Exception {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, email, password FROM userData WHERE username=?";
            try (var c = conn.prepareStatement(statement)) {
                c.setString(1,user.username());
                try (var rs = c.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public void createUser(UserData user) throws Exception {
        var statement = "INSERT INTO userdata (username, email, password) VALUES (?, ?, ?)";
        String username = user.username();
        String email = user.email();
        String password = hashUserPassword(user.password());
        databaseManager.executeUpdate(statement, username, email, password);
    }

    private String hashUserPassword(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }

    public boolean checkPassword(String normalPassword, String hashedPassword){
        return BCrypt.checkpw(normalPassword, hashedPassword);
    }


    public void clearUserData() throws Exception {
        String statement = "TRUNCATE userdata";
        databaseManager.executeUpdate(statement);
    }

    private UserData readUser(ResultSet res) throws Exception {
        String username = res.getString("username");
        String email = res.getString("email");
        String password = res.getString("password");
        return new UserData(username, email, password);
    }
}
