package dataAccess;

import com.google.gson.Gson;
import handlers.requests.RegisterRequest;
import model.Auth;
import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Types.NULL;

public interface UserDAO {
    User getUser(String username) throws DataAccessException;
    void createUser(RegisterRequest req) throws DataAccessException;
    void clearUsers() throws DataAccessException;
}
