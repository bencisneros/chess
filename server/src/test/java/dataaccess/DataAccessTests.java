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
    public void getUserTest() throws Exception{
        UserDatabase userDatabase = new UserDatabase();
        String username = "username";
        String email = "byu.edu";
        String password = "cougs";
        var user = new UserData(username, email, password);
        userDatabase.createUser(user);

        var actual = userDatabase.getUser(user);
        assertEquals(username, actual.username());
        assertEquals(email, actual.email());
        assertNotEquals(password, actual.password());
    }

    @Test
    public void badGetUserTest() throws Exception{
        UserDatabase userDatabase = new UserDatabase();
        String username = "username";
        String email = "byu.edu";
        String password = "cougs";
        var user = new UserData(username, email, password);
        userDatabase.createUser(user);

        var fakeUser = new UserData("not a real username", email, password);

        assertNull(userDatabase.getUser(fakeUser));
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
    public void createAuthTest() throws Exception{
        AuthDatabase authDatabase = new AuthDatabase();
        UserData user = new UserData("ben", "byu.edu", "word");
        var authData = authDatabase.createAuthData(user);
        assertEquals(authDatabase.getAuth(authData.authToken()), authData);
    }

    @Test
    public void badCreateAuthTest() throws Exception{
        RegisterService registerService = new RegisterService();
        UserData user = new UserData("ben", "byu.edu", "word");
        registerService.register(user);


        assertThrows(Exception.class, () -> {
            registerService.register(user);
        });
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
    public void getGameTest() throws Exception{
        GameDAO gameDatabase = new GameDatabase();
        var game = gameDatabase.createGameData("game1");
        assertEquals(game, gameDatabase.getGame(game.gameID()));
    }

    @Test
    public void badGetGameTest() throws Exception{
        GameDAO gameDatabase = new GameDatabase();
        gameDatabase.createGameData("game1");
        assertNull(gameDatabase.getGame(0));
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
        map.put(temp.gameID(), gameData);
        map.put(temp1.gameID(), gameData1);
        map.put(temp2.gameID(), gameData2);

        assertEquals(map, gameDatabase.getGameMap());
    }

    @Test
    public void badListGamesTest() throws Exception{
        ListGamesService listGamesService = new ListGamesService();
        assertThrows(Exception.class, () -> {
            listGamesService.listGames("bad token");
        });
    }

    @Test
    public void clearAuthDataTest() throws Exception{
        AuthDatabase authDatabase = new AuthDatabase();
        var authData = authDatabase.createAuthData(new UserData("username", "email", "password"));
        authDatabase.clearAuthData();
        assertNull(authDatabase.getAuth(authData.authToken()));
    }

    @Test
    public void clearGameDataTest() throws Exception{
        GameDatabase gameDatabase = new GameDatabase();
        var game = gameDatabase.createGameData("game");
        gameDatabase.clearGameData();
        assertNull(gameDatabase.getGame(game.gameID()));
    }

    @Test
    public void clearUserDataTest() throws Exception{
        UserDatabase userDatabase = new UserDatabase();
        UserData user = new UserData("username", "email", "password");
        userDatabase.createUser(user);
        userDatabase.clearUserData();
        assertNull(userDatabase.getUser(user));
    }



}



