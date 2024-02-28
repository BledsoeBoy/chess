package dataAccess;

import handlers.requests.RegisterRequest;
import model.User;

public interface UserDAO {
    User getUser(String username) throws DataAccessException;
    void createUser(RegisterRequest req) throws DataAccessException;
    void clearUsers() throws DataAccessException;

}
