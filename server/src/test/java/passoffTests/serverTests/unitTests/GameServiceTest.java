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

        String username = "sam";
        String password = "bobby";
        String email = "ruffy@gmail.com";

        RegisterRequest parameter1 = new RegisterRequest(username, password, email);

        String username2 = "flake";
        String password2 = "weirdpassword";
        String email2 = "noemail@gmail.com";

        RegisterRequest parameter2 = new RegisterRequest(username2, password2, email2);

        Auth auth = myObject.register(parameter1);
        Auth auth2 = myObject.register(parameter2);

        var gameService = new GameService();
        String gameName = "someName";

        Integer gameId = gameService.createGame(gameName);

        var gameDAO = new MemoryGameDAO();
        gameDAO.getGame(gameId);

        String playerColor = "WHITE";

        int value = (auth != null) ? gameService.joinGame(auth.authToken(), playerColor, gameId) : 0;

        int actual = (auth2 != null) ? gameService.joinGame(auth2.authToken(), playerColor, gameId) : 0;
        int expected = -2;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void positivelistGames() throws DataAccessException {
        var userService = new UserService();

        String username = "sam";
        String password = "bobby";
        String email = "ruffy@gmail.com";

        RegisterRequest parameter1 = new RegisterRequest(username, password, email);
        Auth auth = userService.register(parameter1);

        // Check if auth and authToken are not null before proceeding
        if (auth != null && auth.authToken() != null) {
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
        } else {
            // Handle the case where auth or authToken is null (e.g., log an error or fail the test)
            Assertions.fail("User registration failed. Auth or AuthToken is null.");
        }
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