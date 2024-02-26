package dataAccess;

import handlers.RegisterRequest;
import model.User;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    final private HashMap<String, User> users = new HashMap<>();
    public User getUser(String username) {
        return users.get(username);
    }

    public void createUser(RegisterRequest req) throws DataAccessException {
        var user = new User(req.username(), req.password(), req.email());

        users.put(user.username(), user);
    }
}
