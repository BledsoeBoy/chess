package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import handlers.requests.CreateGameRequest;
import handlers.requests.JoinGameRequest;
import handlers.requests.LoginRequest;
import handlers.requests.RegisterRequest;
import handlers.responses.CreateGameSuccessResponse;
import handlers.responses.Responses;
import model.Auth;
import model.Game;
import model.User;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.Map;

public class Handlers {
    private Handlers() {
        // Private constructor to prevent instantiation
    }

    public static Object clearApp(Request req, Response res) throws DataAccessException {
        var authService = new AuthService();
        authService.clearApp();
        res.status(200);
        return new Gson().toJson(null);
    }

    public static Object register(Request req, Response res) throws DataAccessException {
        try {
            var user = new Gson().fromJson(req.body(), User.class);
            var request = new RegisterRequest(user.username(), user.password(), user.email());
            var myServer = new UserService();
            var auth = myServer.register(request);

            if (user.username() == null || user.password() == null || user.email() == null) {
                res.status(400);
                var response = new Responses("Error: bad request");
                return new Gson().toJson(response);
            }
            // Creating the response JSON and error handling
            if (auth == null) {
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

    public static Object login(Request req, Response res) throws DataAccessException {
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

    public static Object logout(Request req, Response res) throws DataAccessException {
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

    public static Object listGames(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");

        var myService = new GameService();
        var list = myService.listGames(authToken);

        if (list == null) {
            res.status(401); // Unauthorized
            var response = new Responses("Error: unauthorized");
            return new Gson().toJson(response);
        }
        else {
            res.status(200);
            return new Gson().toJson(Map.of("games", list.toArray()));
        }
    }

    public static Object joinGame(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        var authDao = new MemoryAuthDAO();
        Auth auth = authDao.getAuth(authToken);
        if (auth == null) {
            res.status(401); // Unauthorized
            var response = new Responses("Error: unauthorized");
            return new Gson().toJson(response);
        }


        JoinGameRequest request = new Gson().fromJson(req.body(), JoinGameRequest.class);

        Integer gameID = request.getGameId();
        String playerColor = request.getPlayerColor();

        var gameDAO = new MemoryGameDAO();
        Game game = gameDAO.getGame(gameID);

        if (gameID == null || game == null) {
            res.status(400); // Unauthorized
            var response = new Responses("Error: bad request");
            return new Gson().toJson(response);
        }

        var myService = new GameService();
        int errorCode = myService.joinGame(authToken, playerColor, gameID);


        if (errorCode == -2) {
            res.status(403); // Unauthorized
            var response = new Responses("Error: already taken");
            return new Gson().toJson(response);
        }
        else {
            res.status(200); // Unauthorized
            var response = new Responses("");
            return new Gson().toJson(response);
        }
    }

    public static Object createGame(Request req, Response res) {
        try {
            // Check if the request has the required authorization header
            String authToken = req.headers("authorization");
            var authDao = new MemoryAuthDAO();
            Auth auth = authDao.getAuth(authToken);

            if (auth == null) {
                res.status(401); // Unauthorized
                var response = new Responses("Error: unauthorized");
                return new Gson().toJson(response);
            }

            // Deserialize the request body to extract the game name
            CreateGameRequest createGameRequest = new Gson().fromJson(req.body(), CreateGameRequest.class);

            String gameName = createGameRequest.getGameName();

            // Assuming createGame method returns a game ID
            var gameService = new GameService();
            int gameId = gameService.createGame(gameName);
            if (gameId == 0) {
                res.status(400); // Bad Request
                var response = new Responses("Error: bad request");
                return new Gson().toJson(response);
            }

            // Return success response
            res.status(200);
            var response = new CreateGameSuccessResponse(gameId);
            return new Gson().toJson(response);

        } catch (Exception e) {
            // Handle other exceptions
            res.status(500); // Internal Server Error
            var response = new Responses("Error: description");
            return new Gson().toJson(response);
        }
    }
}
