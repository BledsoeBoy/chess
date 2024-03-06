package dataAccess;

import model.Auth;

public interface AuthDAO {
    void clearAuths() throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    Auth getAuth(String authToken) throws DataAccessException;
    Auth createAuth(String req) throws DataAccessException;
}
