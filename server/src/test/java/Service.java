import DataAccess.AuthDataAccessMemory;
import DataAccess.GameDataAccessMemory;
import DataAccess.UserDataAccessMemory;
import Service.*;
import chess.ChessGame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import model.*;
import static org.junit.jupiter.api.Assertions.*;

public class Service {

    @BeforeEach
    public void clearMaps(){
        ClearService clear = new ClearService();
        clear.clear();
    }

    @Test
    public void correctRegisterTest() throws Exception {
        String username = "bencisneros";
        String email = "bcis2@byu.edu";
        String password = "abcd";

        UserData user = new UserData(username, email, password);

        RegisterService registerService = new RegisterService();
        AuthData auth = registerService.register(user);

        assertSame(username, auth.username());
        assertNotNull(auth.authToken());
    }

    @Test
    public void invalidUsernameRegisterTest() {
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

        var userData = new UserData("name","email", "password");
        var userData1 = new UserData("name1","email1", "password1");
        userDataAccessMemory.createUser(userData);
        userDataAccessMemory.createUser(userData1);

        authDataAccessMemory.createAuthData(userData);
        authDataAccessMemory.createAuthData(userData1);

        gameDataAccessMemory.createGameData("game1");
        gameDataAccessMemory.createGameData("game2");

        clearService.clear();

        assertTrue(authDataAccessMemory.getAuthMap().isEmpty());
        assertTrue(userDataAccessMemory.getUserMap().isEmpty());
        assertTrue(gameDataAccessMemory.getGameMap().isEmpty());
    }

    @Test
    public void loginTest() throws Exception {
        String username = "bencisneros";
        String email = "bcis2@byu.edu";
        String password = "abcd";

        UserData user = new UserData(username, email, password);

        RegisterService registerService = new RegisterService();
        registerService.register(user);

        LoginService loginService = new LoginService();
        AuthData authData = loginService.login(user);

        assertSame(username, authData.username());
        assertNotNull(authData.authToken());
    }

    @Test
    public void unauthorizedLoginTest() {
        String username = "bencisneros";
        String email = "bcis2@byu.edu";
        String password = "abcd";

        UserData user = new UserData(username, email, password);

        RegisterService registerService = new RegisterService();
        LoginService loginService = new LoginService();

        UserData nonRegisteredUser = new UserData("abc", email, password);

        try {
            registerService.register(user);
            loginService.login(nonRegisteredUser);
        } catch (Exception e) {
            assertEquals("401 Error: unauthorized", e.getMessage());
        }
    }

    @Test
    public void logoutTest() throws Exception {
        String username = "bencisneros";
        String email = "bcis2@byu.edu";
        String password = "abcd";

        UserData user = new UserData(username, email, password);

        RegisterService registerService = new RegisterService();
        AuthData authData = registerService.register(user);

        LogoutService logoutService = new LogoutService();
        logoutService.logout(authData.authToken());

        AuthDataAccessMemory authDAO = new AuthDataAccessMemory();
        var authMemory = authDAO.getAuthMap();

        assertFalse(authMemory.containsValue(authData));
    }

    @Test
    public void unauthorizedLogoutTest() {
        String username = "bencisneros";
        String email = "bcis2@byu.edu";
        String password = "abcd";

        UserData user = new UserData(username, email, password);

        RegisterService registerService = new RegisterService();
        LogoutService logoutService = new LogoutService();

        try {
            registerService.register(user);
            logoutService.logout("badAuthToken");
        } catch (Exception e) {
            assertEquals("401 Error: unauthorized", e.getMessage());
        }
    }

    @Test
    public void createGameTest() throws Exception{
        String username = "bencisneros";
        String email = "bcis2@byu.edu";
        String password = "abcd";

        UserData user = new UserData(username, email, password);
        var registerService = new RegisterService();
        String authToken = registerService.register(user).authToken();

        CreateGameService createGameService = new CreateGameService();
        var gameData = createGameService.createGame("game1", authToken);

        var gameDataAccessMemory = new GameDataAccessMemory();
        var gameMap = gameDataAccessMemory.getGameMap();

        assertTrue(gameMap.containsValue(gameData));
    }

    @Test
    public void unauthorizedCreateGameTest() {
        String username = "bencisneros";
        String email = "bcis2@byu.edu";
        String password = "abcd";

        UserData user = new UserData(username, email, password);

        RegisterService registerService = new RegisterService();
        CreateGameService createGameService = new CreateGameService();


        try {
            registerService.register(user);
            createGameService.createGame("badAuthToken", "game");
        } catch (Exception e) {
            assertEquals("401 Error: unauthorized", e.getMessage());
        }
    }

    @Test
    public void listGamesTest() throws Exception{
        String username = "bencisneros";
        String email = "bcis2@byu.edu";
        String password = "abcd";

        UserData user = new UserData(username, email, password);

        RegisterService registerService = new RegisterService();
        ListGamesService listGamesService = new ListGamesService();
        CreateGameService createGameService = new CreateGameService();

        AuthData authData = registerService.register(user);
        createGameService.createGame("game1", authData.authToken());
        createGameService.createGame("game2", authData.authToken());
        createGameService.createGame("game3", authData.authToken());

        assertEquals(3, listGamesService.listGames(authData.authToken()).length);
    }
}
