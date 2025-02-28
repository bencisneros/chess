import DataAccess.AuthDataAccessMemory;
import DataAccess.DataAccessException;
import DataAccess.GameDataAccessMemory;
import DataAccess.UserDataAccessMemory;
import Service.ClearService;
import Service.RegisterService;
import chess.ChessGame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
import static org.junit.jupiter.api.Assertions.*;

public class service {

    @BeforeEach
    public void clearMaps(){

    }

    @Test
    public void correctRegisterTest() throws Exception {
        String username = "bencisneros";
        String email = "bcis2@byu.edu";
        String password = "abcd";

        UserData user = new UserData(username, email, password);

        RegisterService register = new RegisterService();
        AuthData auth = register.register(user);

        assertSame(username, auth.username());
        assertNotNull(auth.authToken());
    }

    @Test
    public void invalidUsernameTest() {
        String username = "bencisneros";
        String email = "bcis2@byu.edu";
        String password = "abcd";

        UserData user = new UserData(username, email, password);

        RegisterService register = new RegisterService();

        try {
            register.register(user);
            register.register(user);
        } catch (Exception e) {
            assertEquals("403 Error: already taken", e.getMessage());
        }
    }

    @Test
    public void clearTest() throws Exception{
        var clearService = new ClearService();

        var authDataAccessMemory = new AuthDataAccessMemory();
        var userDataAccessMemory = new UserDataAccessMemory();
        var gameDataAccessMemory = new GameDataAccessMemory();

        var authData = new AuthData("1234", "username");
        var authData1 = new AuthData("4321", "nameuser");
        authDataAccessMemory.createAuthData(authData);
        authDataAccessMemory.createAuthData(authData1);

        var userData = new UserData("name","email", "password");
        var userData1 = new UserData("name1","email1", "password1");
        userDataAccessMemory.createUser(userData);
        userDataAccessMemory.createUser(userData1);

        var gameData = new GameData(1, "white", "black","game", new ChessGame());
        var gameData1 = new GameData(2, "black", "white","game1", new ChessGame());
        gameDataAccessMemory.createGameData(gameData);
        gameDataAccessMemory.createGameData(gameData1);

        clearService.clear();

        assertTrue(authDataAccessMemory.getAuthMap().isEmpty());
        assertTrue(userDataAccessMemory.getUserMap().isEmpty());
        assertTrue(gameDataAccessMemory.getGameMap().isEmpty());
    }


}
