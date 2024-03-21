package clientTests;

import dataAccess.*;
import exception.ResponseException;
import model.Auth;
import model.Game;
import model.User;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import server.requests.CreateGameRequest;
import server.requests.LoginRequest;
import server.responses.CreateGameSuccessResponse;


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
    void positiveJoinGameTest() throws ResponseException {
        User user = new User("juan", "something", "random@email");
        facade.register(user);
        CreateGameRequest req = new CreateGameRequest();
        req.setGameName("testGame");
        CreateGameSuccessResponse res = facade.createGame(req);

        facade.joinGame(res.getGameID(), "WHITE");

        Game[] games = facade.listGames();

        boolean playerIsIn = false;
        for (var game : games) {
            if (game.whiteUsername().equals("juan")) {
                playerIsIn = true;
                break;
            }
        }

        Assertions.assertTrue(playerIsIn);
    }

    @Test
    void negativeJoinGameTest() throws ResponseException {
        User user = new User("juan", "something", "random@email");
        facade.register(user);

        Assertions.assertThrows(exception.ResponseException.class, () -> {
            facade.joinGame(4, "WHITE");
        });
    }

    @Test
    void positiveLogoutTest() throws ResponseException {
        User user = new User("juan", "something", "random@email");
        facade.register(user);

        facade.logout();
        Assertions.assertThrows(exception.ResponseException.class, () -> {
            facade.listGames();
        });
    }

    @Test
    void negativeLogoutTest() throws ResponseException {
        Assertions.assertThrows(exception.ResponseException.class, () -> {
            facade.logout();
        });
    }

    @Test
    void positiveListGamesTest() throws ResponseException {
        User user = new User("juan", "something", "random@email");
        facade.register(user);
        CreateGameRequest req = new CreateGameRequest();
        req.setGameName("testGame");
        facade.createGame(req);
        var games = facade.listGames();
        int actualSize = games.length;
        int expectedSize = 1;

        Assertions.assertEquals(expectedSize, actualSize);
    }
    @Test
    void negativeListGamesTest() throws ResponseException {
        Assertions.assertThrows(exception.ResponseException.class, () -> {
            facade.listGames();
        });
    }
}
