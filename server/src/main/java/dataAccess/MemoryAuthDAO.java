package dataAccess;

import model.Auth;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
        private static final Map<String, Auth> auths = new HashMap<>();
        public void clearAuths() {
            auths.clear();
        }
        public void deleteAuth(String authToken) {
            auths.remove(authToken);
        }

        public Auth getAuth(String authToken) {
            return auths.get(authToken);
        }

        public Auth createAuth(Auth auth) {
            var authentication = new Auth(auth.authToken(), auth.username());
            auths.put(auth.authToken(), authentication);
            return authentication;
        }
}
