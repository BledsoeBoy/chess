package handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import model.User;
import service.UserService;
import spark.*;
public class Handlers {
    public Object register(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), User.class);
        var request = new RegisterRequest(user.username(), user.password(), user.email());
        var myServer = new UserService();
        var auth = myServer.register(request);
        return new Gson().toJson(auth);
    }
}
