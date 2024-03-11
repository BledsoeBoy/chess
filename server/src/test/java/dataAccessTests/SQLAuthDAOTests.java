package dataAccessTests;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import model.Auth;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SQLAuthDAOTests {
    SQLAuthDAO sqlAuthDAO;
    @BeforeEach
    void setup() throws DataAccessException {
        sqlAuthDAO = new SQLAuthDAO();
        sqlAuthDAO.clearAuths();
    }
    @Test
    void clearAuthsTest() throws DataAccessException {
        Auth auth = new Auth("someToken", "fakeusername");
        sqlAuthDAO.createAuth(auth);

        sqlAuthDAO.clearAuths();

        Auth expected = null;
        Auth actual =  sqlAuthDAO.getAuth("someToken");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void positiveDeleteAuthTest() throws DataAccessException {
        Auth auth = new Auth("someToken", "fakeusername");
        sqlAuthDAO.createAuth(auth);
        sqlAuthDAO.deleteAuth("someToken");

        Auth expected = null;
        Auth actual =  sqlAuthDAO.getAuth("someToken");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void negativeDeleteAuthTest() throws DataAccessException {
        Auth auth = new Auth("someToken", "fakeusername");
        sqlAuthDAO.createAuth(auth);

        sqlAuthDAO.deleteAuth("wrongToken");

        Auth expected = auth;
        Auth actual =  sqlAuthDAO.getAuth("someToken");

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void positiveGetAuthTest() throws DataAccessException {
        Auth auth = new Auth("someToken", "fakeUsername");
        sqlAuthDAO.createAuth(auth);

        Auth actual = sqlAuthDAO.getAuth("someToken");

        Auth expected = auth;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void negativeGetAuthTest() throws DataAccessException {
        Auth auth = new Auth("someToken", "fakeUsername");
        sqlAuthDAO.createAuth(auth);

        Auth actual = sqlAuthDAO.getAuth("differentToken");

        Auth expected = null;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void positiveCreateAuthTest() throws DataAccessException{
        Auth auth = new Auth("someToken", "fakeUsername");
        Auth actual = sqlAuthDAO.createAuth(auth);

        Auth expected = auth;

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void negativeCreateAuthTest() throws DataAccessException{
        Auth auth = new Auth("someToken", "fakeUsername");

        Assertions.assertThrows(DataAccessException.class, () -> {
            sqlAuthDAO.createAuth(auth);
            sqlAuthDAO.createAuth(auth);
        });
    }
}
