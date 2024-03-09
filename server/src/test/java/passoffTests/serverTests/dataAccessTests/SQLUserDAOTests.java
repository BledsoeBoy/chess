package passoffTests.serverTests.dataAccessTests;

import chess.ChessGame;
import dataAccess.DataAccessException;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;
import handlers.requests.RegisterRequest;
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
        RegisterRequest regReq = new RegisterRequest("billyBob", "somethingEpic", "billyBob@email.com");
        sqlUserDAO.createUser(regReq);

        sqlUserDAO.clearUsers();

        User expected = null;
        User actual =  sqlUserDAO.getUser("billyBob");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void positiveGetUserTest() throws DataAccessException {
        RegisterRequest regReq = new RegisterRequest("billyBob", "somethingEpic", "billyBob@email.com");
        sqlUserDAO.createUser(regReq);

        String expected = "billyBob";
        User actualUser =  sqlUserDAO.getUser("billyBob");
        String actual = actualUser.username();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void negativeGetUserTest() throws DataAccessException {
        RegisterRequest regReq = new RegisterRequest("billyBob", "somethingEpic", "billyBob@email.com");
        sqlUserDAO.createUser(regReq);

        User expected = null;
        User actual =  sqlUserDAO.getUser("ricky");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void positiveCreateUserTest() throws DataAccessException {
        RegisterRequest regReq = new RegisterRequest("billyBob", "somethingEpic", "billyBob@email.com");
        sqlUserDAO.createUser(regReq);

        String expected = "billyBob";
        User actualUser =  sqlUserDAO.getUser("billyBob");
        String actual = actualUser.username();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void negativeCreateUserTest() throws DataAccessException {
        RegisterRequest regReq = new RegisterRequest("billyBob", null, "billyBob@email.com");

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            sqlUserDAO.createUser(regReq);
        });
    }
}
