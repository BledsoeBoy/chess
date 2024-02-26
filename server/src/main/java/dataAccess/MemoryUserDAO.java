package dataAccess;

import model.User;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    final private HashMap<String, User> users = new HashMap<>();
    public User getUser(String username) {
        return users.get(username);
    }

    public void createUser(User user) {
        user = new User(user.username(), user.password(), user.email());

        users.put(user.username(), user);
    }
}
