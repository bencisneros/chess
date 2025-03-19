package client;

import org.junit.jupiter.api.*;
import server.Server;
import service.ClearService;


public class ServerFacadeTests {

    private static Server server;

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
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

}