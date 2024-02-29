package dataAccess;

import handlers.requests.RegisterRequest;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    private static final Map<String, User> users = new HashMap<>();
    public void clearUsers() {
        users.clear();
    }
    public User getUser(String username) {
        return users.get(username);
    }

    public void createUser(RegisterRequest req) throws DataAccessException {
        var user = new User(req.username(), req.password(), req.email());

        users.put(user.username(), user);
    }
}
