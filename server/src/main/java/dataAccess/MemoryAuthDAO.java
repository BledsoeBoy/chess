package dataAccess;

import handlers.requests.RegisterRequest;
import model.Auth;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
        private static final HashMap<String, Auth> auths = new HashMap<>();
        public void clearAuths() {
            auths.clear();
        }
        public void deleteAuth(String authToken) {
            auths.remove(authToken);
        }

        public Auth getAuth(String authToken) {
            return auths.get(authToken);
        }

        public Auth createAuth(String req) {
            var authToken = UUID.randomUUID().toString();
            var auth = new Auth(authToken, req);
            auths.put(authToken, auth);
            return auth;
        }
}
