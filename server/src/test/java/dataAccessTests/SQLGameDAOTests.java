package dataAccessTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.SQLGameDAO;
import model.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

public class SQLGameDAOTests {
    SQLGameDAO sqlGameDAO;
    @BeforeEach
    void setup() throws DataAccessException {
        sqlGameDAO = new SQLGameDAO();
        sqlGameDAO.clearGames();
    }
    @Test
    void clearGamesTest() throws DataAccessException {
        Game game = new Game(1, null, null, "testGame", new ChessGame());
        sqlGameDAO.createGame(game);

        sqlGameDAO.clearGames();

        Game expected = null;
        Game actual =  sqlGameDAO.getGame(game.gameID());

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void positiveCreateGameTest() throws DataAccessException {
        Game game = new Game(1, null, null, "testGame", new ChessGame());
        int actual = sqlGameDAO.createGame(game);

        int expected = 1;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void negativeCreateGameTest() throws DataAccessException {
        Game game = new Game(1, null, null, "testGame", new ChessGame());

        Assertions.assertThrows(DataAccessException.class, () -> {
            sqlGameDAO.createGame(game);
            sqlGameDAO.createGame(game);
        });
    }

    @Test
    void positiveUpdateGame() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        Game game = new Game(1, null, null, "testGame", chessGame);
        sqlGameDAO.createGame(game);

        sqlGameDAO.updateGame("WHITE", game, "juan");

        Game actualGame = sqlGameDAO.getGame(1);

        String expected = "juan";
        String actual = actualGame.whiteUsername();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void negativeUpdateGame() throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        Game game = new Game(1, null, null, "testGame", chessGame);
        sqlGameDAO.createGame(game);

        sqlGameDAO.updateGame("boy", game, "juan");

        Game actualGame = sqlGameDAO.getGame(1);

        String expected = null;
        String actual = actualGame.whiteUsername();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void positiveGetGameTest() throws DataAccessException {
        Game game = new Game(1, null, null, "testGame", new ChessGame());
        sqlGameDAO.createGame(game);

        Game actualGame = sqlGameDAO.getGame(1);

        String actual = actualGame.gameName();
        String expected = "testGame";

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void negativeGetGameTest() throws DataAccessException {
        Game game = new Game(1, null, null, "testGame", new ChessGame());
        sqlGameDAO.createGame(game);

        Game actual = sqlGameDAO.getGame(3);

        Game expected = null;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void positiveListGamesTest() throws DataAccessException {
        Game game = new Game(1, null, null, "testGame", new ChessGame());
        sqlGameDAO.createGame(game);
        Game game2 = new Game(2, null, "boi", "secondGame", new ChessGame());
        sqlGameDAO.createGame(game2);

        Collection<Game> games = sqlGameDAO.listGames();

        int actualSize = games.size();
        int expectedSize = 2;

        Assertions.assertEquals(expectedSize, actualSize);
    }

    @Test
    void negativeListGamesTest() throws DataAccessException {
        sqlGameDAO.clearGames();
        Collection<Game> games = sqlGameDAO.listGames();

        int actualSize = games.size();
        int expectedSize = 0;

        Assertions.assertEquals(expectedSize, actualSize);
    }
}
