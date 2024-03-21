package clientTests;

import dataAccess.*;
import exception.ResponseException;
import model.Auth;
import model.User;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import server.requests.CreateGameRequest;
import server.requests.LoginRequest;
import server.requests.RegisterRequest;
import server.responses.CreateGameSuccessResponse;
import service.AuthService;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    private static AuthDAO authDAO;
    private static GameDAO gameDAO;
    private static UserDAO userDAO;


    @BeforeAll
    public static void init() throws DataAccessException {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(String.format("http://localhost:%d", port));
        authDAO = new SQLAuthDAO();
        gameDAO = new SQLGameDAO();
        userDAO = new SQLUserDAO();

    }

    @BeforeEach
    public void clear() throws DataAccessException {
        authDAO.clearAuths();
        gameDAO.clearGames();
        userDAO.clearUsers();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @Test
    void positiveRegisterTest() throws ResponseException {
        User req = new User("juan", "something", "random@email");
        Auth auth = facade.register(req);
        String actual = auth.username();
        String expected = "juan";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void negativeRegisterTest() throws ResponseException {
        User req = new User("juan", "something", "random@email");
        facade.register(req);

        Assertions.assertThrows(exception.ResponseException.class, () -> {
            facade.register(req);
        });
    }

    @Test
    void positiveLoginTest() throws ResponseException {
        User req = new User("juan", "something", "random@email");
        facade.register(req);
        LoginRequest login = new LoginRequest("juan", "something");
        Auth auth = facade.login(login);
        String actual = auth.username();
        String expected = "juan";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void negativeLoginTest() throws ResponseException {
        LoginRequest login = new LoginRequest("juan", "something");

        Assertions.assertThrows(exception.ResponseException.class, () -> {
            facade.login(login);
        });
    }

    @Test
    void positiveCreateGameTest() throws ResponseException {
        User user = new User("juan", "something", "random@email");
        facade.register(user);
        CreateGameRequest req = new CreateGameRequest();
        req.setGameName("testGame");
        CreateGameSuccessResponse actual = facade.createGame(req);

        Assertions.assertNotNull(actual);
    }
    @Test
    void negativeCreateGameTest() throws ResponseException {
        CreateGameRequest req = new CreateGameRequest();
        req.setGameName("testGame");

        Assertions.assertThrows(exception.ResponseException.class, () -> {
            facade.createGame(req);
        });
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
