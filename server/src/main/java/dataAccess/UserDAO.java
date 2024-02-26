package dataAccess;

import model.User;

public interface UserDAO {
    User getUser(String username) throws DataAccessException;

    void createUser(User u) throws DataAccessException;
}
