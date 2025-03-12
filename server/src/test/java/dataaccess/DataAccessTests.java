package dataaccess;

import chess.ChessGame;
import dataaccess.AuthDataAccessMemory;
import dataaccess.GameDataAccessMemory;
import dataaccess.UserDataAccessMemory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
import org.mindrot.jbcrypt.BCrypt;
import service.*;

import java.util.HashMap;
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

    @Test
    public void updateGameTest() throws Exception{
        GameDatabase gameDatabase = new GameDatabase();
        var gameData = gameDatabase.createGameData("game1");
        String whiteUsername = "username";
        gameDatabase.updateGame("WHITE", whiteUsername, gameData);
        var newGameData = gameDatabase.getGame(gameData.gameID());

        assertEquals(whiteUsername, newGameData.whiteUsername());
    }

    @Test
    public void badUpdateGameTest() throws Exception{
        GameDatabase gameDatabase = new GameDatabase();
        var gameData = gameDatabase.createGameData("game1");
        assertThrows(Exception.class, () -> {
            gameDatabase.updateGame("not a color", "", gameData);
        });
    }

    @Test
    public void listGamesTest() throws Exception{
        GameDatabase gameDatabase = new GameDatabase();
        var temp = gameDatabase.createGameData("game1");
        var temp1 = gameDatabase.createGameData("game2");
        var temp2 = gameDatabase.createGameData("game3");

        GameData gameData = new GameData(temp.gameID(), "", "", "game1", new ChessGame());
        GameData gameData1 = new GameData(temp1.gameID(), "", "", "game2", new ChessGame());
        GameData gameData2 = new GameData(temp2.gameID(), "", "", "game3", new ChessGame());

        var map = new HashMap<Integer, GameData>();
        map.put(1, gameData);
        map.put(2, gameData1);
        map.put(3, gameData2);

        assertEquals(map, gameDatabase.getGameMap());
    }

    public void badListGamesTest() throws Exception{
        ListGamesService listGamesService = new ListGamesService();
        assertThrows(Exception.class, () -> {
            listGamesService.listGames("bad token");
        });
    }

}



