package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import handlers.requests.CreateGameRequest;
import handlers.requests.LoginRequest;
import handlers.requests.RegisterRequest;
import handlers.responses.Responses;
import model.Auth;
import model.Game;
import model.User;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;
public class Handlers {
    public Object clearApp(Request req, Response res) throws DataAccessException {
        var authService = new AuthService();

        authService.clearApp();

        res.status(200);
        return new Gson().toJson(null);
    }

    public Object register(Request req, Response res) throws DataAccessException {
        try {
            var user = new Gson().fromJson(req.body(), User.class);

            var request = new RegisterRequest(user.username(), user.password(), user.email());
            var myServer = new UserService();
            var auth = myServer.register(request);

            // Creating the response JSON and error handling
            if (res.equals(null)) {
                res.status(403);
                var response = new Responses("Error: already taken");
                return new Gson().toJson(response);
            } else {
                res.status(200);
                res.type("application/json");
                return new Gson().toJson(auth);
            }
        } catch (JsonSyntaxException e) {
            // Handle the exception (e.g., log the error, return an error response)
            res.status(400); // Bad Request
            var response = new Responses("Error: bad request");
            return new Gson().toJson(response);
        }
    }

    public Object login(Request req, Response res) throws DataAccessException {
        var user = new Gson().fromJson(req.body(), User.class);
        var request = new LoginRequest(user.username(), user.password());
        var myServer = new UserService();
        var auth = myServer.login(request.username(), request.password());

        if (auth == null) {
            res.status(401);
            var response = new Responses("Error: unauthorized");
            return new Gson().toJson(response);
        }
        return new Gson().toJson(auth);
    }

    public Object logout(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        boolean authorized = true;

        if (authToken != null) {
            var myServer = new UserService();
            authorized = myServer.logout(authToken);
            if (authorized) {
                res.status(200);
            }
            else {
                res.status(401);
                var response = new Responses("Error: unauthorized;");
                return new Gson().toJson(response);
            }
        } else {
            // Handle the case where the header is not present or has an invalid value
            res.status(500);
            var response = new Responses("Error: description");
            return new Gson().toJson(response);
        }

        return "";
    }

    public Object createGame(Request req, Response res) {
        try {
            // Check if the request has the required authorization header
            String authToken = req.headers("authorization");
            if (authToken == null || authToken.isEmpty()) {
                res.status(401); // Unauthorized
                return new Gson().toJson(new ErrorResponse("Error: unauthorized"));
            }

            // Deserialize the request body to extract the game name
            CreateGameRequest createGameRequest = new Gson().fromJson(req.body(), CreateGameRequest.class);

            // Validate the game name
            String gameName = createGameRequest.getGameName();
            if (gameName == null || gameName.isEmpty()) {
                res.status(400); // Bad Request
                return new Gson().toJson(new ErrorResponse("Error: bad request"));
            }

            // Assuming createGame method returns a game ID
            var gameService = new GameService();
            int gameId = gameService.createGame(authToken, gameName);

            // Return success response
            res.status(200);
            return new Gson().toJson(new SuccessResponse(gameId)); // Adjust SuccessResponse based on your needs

        } catch (Exception e) {
            // Handle other exceptions
            res.status(500); // Internal Server Error
            return new Gson().toJson(new ErrorResponse("Error: description"));
        }
    }

    private static class SuccessResponse {
        private int gameID;

        public SuccessResponse(int gameID) {
            this.gameID = gameID;
        }
    }

    private static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }
    }
}
