package clientTests;

import dataAccess.*;
import exception.ResponseException;
import model.Auth;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import server.requests.LoginRequest;
import service.AuthService;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(String.format("http://localhost:%d", port));
    }

    @BeforeEach
    public void clear() throws DataAccessException {
        AuthService service = new AuthService(new MemoryAuthDAO(), new MemoryGameDAO(), new MemoryUserDAO());
        service.clearApp();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    void positiveTestlogin() throws ResponseException {
        LoginRequest login = new LoginRequest("billy", "passwordforBilly")
        Auth auth = facade.login(login);
        String actual = auth.username();
        String expected = "billy";

    }

    @Test
    void register() {
    }

    @Test
    void createGame() {
    }

    @Test
    void joinGame() {
    }

    @Test
    void logout() {
    }

    @Test
    void listGames() {
    }
}
