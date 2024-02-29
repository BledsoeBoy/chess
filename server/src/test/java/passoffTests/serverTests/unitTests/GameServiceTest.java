package passoffTests.serverTests.unitTests;

import dataAccess.DataAccessException;
import dataAccess.MemoryGameDAO;
import handlers.requests.RegisterRequest;
import model.Auth;
import model.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.GameService;
import service.UserService;

import java.util.Arrays;
import java.util.List;

public class GameServiceTest {
    @Test
    void positiveCreateGame() throws DataAccessException {
        var myObject = new GameService();
        String gameName = "someName";

        Integer gameId = myObject.createGame(gameName);
        var gameDAO = new MemoryGameDAO();
        var game = gameDAO.getGame(gameId);

        var actual = game.gameName();

        String expected = gameName;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void negativeCreateGame() throws DataAccessException {
        var myObject = new GameService();
        String gameName = null;

        var createdGameId = myObject.createGame(gameName);

        // Additional assertion to ensure that the game with a null name is not stored in the DAO
        var gameDAO = new MemoryGameDAO();
        var retrievedGame = gameDAO.getGame(createdGameId);

        Assertions.assertNull(retrievedGame, "No game should be stored in the DAO for invalid input");
    }

    @Test
    void positiveJoinGame() throws DataAccessException {
        var myObject = new UserService();

        String username = "sam";
        String password = "bobby";
        String email = "ruffy@gmail.com";

        RegisterRequest parameter1 = new RegisterRequest(username, password, email);

        Auth auth = myObject.register(parameter1);

        // Check if auth is not null before proceeding
        if (auth != null) {
            var gameService = new GameService();
            String gameName = "someName";

            Integer gameId = gameService.createGame(gameName);

            var gameDAO = new MemoryGameDAO();
            gameDAO.getGame(gameId);

            String playerColor = "WHITE";

            int actual = gameService.joinGame(auth.authToken(), playerColor, gameId);

            int expected = -5;

            Assertions.assertEquals(expected, actual);
        } else {
            // Handle the case where auth is null (e.g., log an error or fail the test)
            Assertions.fail("User registration failed. Auth is null.");
        }
    }

    @Test
    void negativeJoinGame() throws DataAccessException {
        var myObject = new UserService();

        // Register a user
        String username = "sam";
        String password = "bobby";
        String email = "sam@gmail.com";
        RegisterRequest registrationRequest = new RegisterRequest(username, password, email);
        Auth auth = myObject.register(registrationRequest);

        // Check if auth is not null before proceeding
        if (auth != null) {
            // Create a game
            var gameService = new GameService();
            String gameName = "someGame";
            Integer gameId = gameService.createGame(gameName);

            // Attempt to join the game with invalid parameters
            String invalidColor = "INVALID_COLOR";
            int actual = gameService.joinGame(auth.authToken(), invalidColor, gameId);

            // Ensure the join operation returns the expected error code
            int expectedErrorCode = -2;
            Assertions.assertEquals(expectedErrorCode, actual);
        } else {
            // Enhance the failure message with details about the registration request
            Assertions.fail("User registration failed. Auth is null. Registration request: " + registrationRequest);
        }
    }

    @Test
    void positivelistGames() throws DataAccessException {
        var userService = new UserService();

        String username = "sam";
        String password = "bobby";
        String email = "ruffy@gmail.com";

        RegisterRequest parameter1 = new RegisterRequest(username, password, email);
        Auth auth = userService.register(parameter1);

        var myObject = new GameService();
        String gameName = "firstGame";

        myObject.createGame(gameName);

        String gameName2 = "secondGame";

        myObject.createGame(gameName2);

        var authToken = auth.authToken();

        var actual = myObject.listGames(authToken);

        List<Game> expected = Arrays.asList(
                new Game(1, null, null, "firstGame", null),
                new Game(2, null, null, "secondGame", null)
        );

        Assertions.assertEquals(expected.size(), actual.size());
    }
    @Test
    void negativelistGames() throws DataAccessException {
        var userService = new UserService();

        String username = "sam";
        String password = "bobby";
        String email = "ruffy@gmail.com";

        RegisterRequest parameter1 = new RegisterRequest(username, password, email);
        Auth auth = userService.register(parameter1);

        var myObject = new GameService();
        String gameName = "firstGame";

        myObject.createGame(gameName);
        var actual = myObject.listGames("madeUpToken");



        Assertions.assertNull(actual);
    }
}
