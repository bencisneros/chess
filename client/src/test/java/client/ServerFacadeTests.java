package client;

import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import service.ClearService;
import ui.ResponseException;
import ui.ServerFacade;
import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private ServerFacade serverFacade;
    private final String SERVER_URL = "http://localhost:8080";

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void clear() throws Exception{
        ClearService clearService = new ClearService();
        clearService.clear();
        serverFacade = new ServerFacade(SERVER_URL);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void testFacadeRegister() throws Exception {

        String username = "username";
        String email = "email";
        String password = "password";
        UserData user = new UserData(username, email, password);
        AuthData authData = serverFacade.register(user);
        assertEquals(username, authData.username());
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    public void badRegisterTest() throws Exception {
        String username = "username";
        String email = "email";
        String password = "password";
        UserData user = new UserData(username, email, password);
        serverFacade.register(user);
        assertThrows(Exception.class, () -> {
            serverFacade.register(user);
        });
    }

    @Test
    public void loginTest() throws Exception{
        String username = "username";
        String email = "email";
        String password = "password";
        UserData user = new UserData(username, email, password);
        serverFacade.register(user);
        AuthData authData = serverFacade.login(user);
        assertTrue(authData.authToken().length() > 10);
    }

    @Test
    public void badLoginTest(){
        String username = "username";
        String email = "email";
        String password = "password";
        UserData user = new UserData(username, email, password);
        assertThrows(Exception.class, () -> {
            serverFacade.login(user);
        });
    }

    @Test
    public void logoutTest() throws Exception{
        String username = "username";
        String email = "email";
        String password = "password";
        UserData user = new UserData(username, email, password);
        AuthData authData = serverFacade.register(user);
        serverFacade.logout(authData);
        assertThrows(Exception.class, () -> {
            serverFacade.logout(authData);
        });
    }

    @Test
    public void badLogoutTest() throws Exception{
        String username = "username";
        String email = "email";
        String password = "password";
        UserData user = new UserData(username, email, password);
        serverFacade.register(user);
        var fakeAuth = new AuthData("bad auth token", username);
        assertThrows(Exception.class, () -> {
            serverFacade.logout(fakeAuth);
        });
    }

    @Test
    public void createGameTest() throws Exception{
        String username = "username";
        String email = "email";
        String password = "password";
        UserData user = new UserData(username, email, password);
        var authData = serverFacade.register(user);
        GameData gameData = serverFacade.createGame(authData, "game");
        assertTrue(gameData.gameID() > 0);
    }

    @Test
    public void badCreateGameTest() throws Exception{
        String username = "username";
        String email = "email";
        String password = "password";
        UserData user = new UserData(username, email, password);
        serverFacade.register(user);
        var fakeAuth = new AuthData("bad auth token", username);
        assertThrows(Exception.class, () -> {
            serverFacade.createGame(fakeAuth, "game");
        });
    }

    @Test
    public void listGamesTest() throws Exception{
        String username = "username";
        String email = "email";
        String password = "password";
        UserData user = new UserData(username, email, password);
        AuthData authData = serverFacade.register(user);
        serverFacade.createGame(authData, "game1");
        serverFacade.createGame(authData, "game2");
        serverFacade.createGame(authData, "game3");
        var list = serverFacade.listGames(authData);

        assertEquals(3, list.length);
    }

    @Test
    public void badListGamesTest() throws Exception{
        String username = "username";
        String email = "email";
        String password = "password";
        UserData user = new UserData(username, email, password);
        AuthData authData = serverFacade.register(user);
        serverFacade.createGame(authData, "game1");
        serverFacade.createGame(authData, "game2");
        serverFacade.createGame(authData, "game3");
        var fakeAuth = new AuthData("bad auth token", username);
        assertThrows(Exception.class, () -> {
            serverFacade.listGames(fakeAuth);
        });
    }
}