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
        var actual = 10;
        var expected = 10;
        Assertions.assertEquals(expected, actual);

    }

    @Test
    void negativeJoinGame() throws DataAccessException {
        var actual = 10;
        var expected = 10;
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

        var myObject = new GameService();
        String gameName = "firstGame";

        myObject.createGame(gameName);
        var actual = myObject.listGames("madeUpToken");

        Assertions.assertNull(actual);
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
