package dataAccess;

import handlers.RegisterRequest;
import model.Auth;
import model.User;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO {
        final private HashMap<String, Auth> auths = new HashMap<>();
        public Auth getAuth(String authToken) {
            return auths.get(authToken);
        }

        public Auth createAuth(RegisterRequest req) throws DataAccessException {
            var auth = new Auth(UUID.randomUUID().toString(), req.username());
            auths.put(req.username(), auth);
            return auth;
        }
}
