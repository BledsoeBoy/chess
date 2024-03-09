package passoffTests.serverTests.dataAccessTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import model.Game;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SQLUserDAOTests {
    SQLUserDAO sqlUserDAO;
    @BeforeEach
    void setup() throws DataAccessException {
        sqlUserDAO = new SQLUserDAO();
        sqlUserDAO.clearUsers();
    }
    @Test
    void clearGamesTest() throws DataAccessException {
        User user = new User("billyBob", "somethingEpic", "billyBob@email.com");
        sqlUserDAO.createUser(user);

        sqlUserDAO.clearGames();

        Game expected = null;
        Game actual =  sqlGameDAO.getGame(game.gameID());

        Assertions.assertEquals(expected, actual);
    }
}
