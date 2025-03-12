package dataaccess;

import dataaccess.AuthDataAccessMemory;
import dataaccess.GameDataAccessMemory;
import dataaccess.UserDataAccessMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
import org.mindrot.jbcrypt.BCrypt;
import service.ClearService;
import service.CreateGameService;
import service.RegisterService;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class DataAccessTests {

    @BeforeEach
    public void clearAllTables() throws Exception{
        ClearService clearService = new ClearService();
        clearService.clear();
    }


    @Test
    public void createUserTest() throws Exception{
        UserDatabase userDatabase = new UserDatabase();

        String username = "bencisneros";
        String email = "email";
        String password = "password";
        var expected = new UserData(username, email, password);

        userDatabase.createUser(expected);

        var actual = userDatabase.getUser(expected);

        assertEquals(username, actual.username());
        assertEquals(email, actual.email());
    }

    @Test
    public void createBadUserTest() throws Exception{
        String username = "bencisneros";
        String email = "email";
        String password = "password";
        var expected = new UserData(username, email, password);

        var userDataBase = new UserDatabase();
        userDataBase.createUser(expected);
        assertThrows(Exception.class, () -> {
            userDataBase.createUser(expected);
        });
    }

    @Test
    public void checkPasswordTest() throws Exception{
        UserDatabase userDatabase = new UserDatabase();

        String username = "bencisneros";
        String email = "email";
        String password = "password";
        var user = new UserData(username, email, password);

        userDatabase.createUser(user);
        var actual = userDatabase.getUser(user);

        assertTrue(userDatabase.checkPassword(password, actual.password()));
    }

    @Test
    public void checkBadPasswordTest() throws Exception{
        UserDatabase userDatabase = new UserDatabase();

        String username = "bencisneros";
        String email = "email";
        String password = "password";
        var user = new UserData(username, email, password);

        userDatabase.createUser(user);
        var actual = userDatabase.getUser(user);

        assertFalse(userDatabase.checkPassword("hello", actual.password()));
    }

    @Test
    public void getAuthTest() throws Exception{
        AuthDatabase authDatabase = new AuthDatabase();
        RegisterService registerService = new RegisterService();

        String username = "bencisneros";
        String email = "email";
        String password = "password";
        var user = new UserData(username, email, password);

        var authData = registerService.register(user);

        assertEquals(authData.authToken(), authDatabase.getAuth(authData.authToken()).authToken());
    }

    @Test
    public void badGetAuthTest() throws Exception{
        AuthDatabase authDatabase = new AuthDatabase();
        RegisterService registerService = new RegisterService();

        String username = "bencisneros";
        String email = "email";
        String password = "password";
        var user = new UserData(username, email, password);

        var authData = registerService.register(user);

        assertNotEquals(authData, authDatabase.getAuth(authData.authToken() + " "));
    }

    @Test
    public void deleteAuthTest() throws Exception{
        AuthDatabase authDatabase = new AuthDatabase();
        RegisterService registerService = new RegisterService();

        String username = "bencisneros";
        String email = "email";
        String password = "password";
        var user = new UserData(username, email, password);

        var authData = registerService.register(user);
        authDatabase.deleteAuth(authData);

        assertNull(authDatabase.getAuth(authData.authToken()));
    }

    @Test
    public void badDeleteAuthTest() throws Exception{
        AuthDatabase authDatabase = new AuthDatabase();
        RegisterService registerService = new RegisterService();

        String username = "bencisneros";
        String email = "email";
        String password = "password";
        var user = new UserData(username, email, password);

        var authData = registerService.register(user);
        var fakeAuthData = new AuthData("token", username);
        authDatabase.deleteAuth(fakeAuthData);

        assertNotNull(authDatabase.getAuth(authData.authToken()));
    }

    @Test
    public void createGameTest() throws Exception{
        GameDatabase gameDatabase = new GameDatabase();

        var gameData = gameDatabase.createGameData("game1");

        var actualGame = gameDatabase.getGame(gameData.gameID());

        assertEquals(gameData, actualGame);
    }

    @Test
    public void badCreationTest() throws Exception{
        CreateGameService createGameService = new CreateGameService();
        GameDAO gameDatabase = new GameDatabase();
        gameDatabase.createGameData("game1");
        assertThrows(Exception.class, () -> {
            createGameService.createGame("game1", "badToken");
        });
    }

}



