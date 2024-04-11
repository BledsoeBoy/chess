package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dataAccess.*;
import server.requests.CreateGameRequest;
import server.websocket.WebSocketHandler;
import server.requests.JoinGameRequest;
import server.requests.LoginRequest;
import server.requests.RegisterRequest;
import server.responses.CreateGameSuccessResponse;
import server.responses.Responses;
import model.Auth;
import model.Game;
import model.User;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

import java.util.Collection;
import java.util.Map;

public class Handlers {
    private final AuthService authService;
    private final UserService userService;
    private final GameService gameService;
    private final AuthDAO authDao;
    private final GameDAO gameDao;
    private final Gson gson;
    public Handlers(AuthService authService, UserService userService, GameService gameService, AuthDAO authDao, GameDAO gameDao, Gson gson) {
        this.authService = authService;
        this.userService = userService;
        this.gameService = gameService;
        this.authDao = authDao;
        this.gameDao = gameDao;
        this.gson = gson;
    }

    public Object clearApp(Request req, Response res) throws DataAccessException {
        authService.clearApp();
        res.status(200);
        return gson.toJson(null);
    }

    public Object register(Request req, Response res) throws DataAccessException {
        try {
            var user = gson.fromJson(req.body(), User.class);
            var request = new RegisterRequest(user.username(), user.password(), user.email());
            var auth = userService.register(request);

            if (user.username() == null || user.password() == null || user.email() == null) {
                res.status(400);
                var response = new Responses("Error: bad request");
                return gson.toJson(response);
            }
            // Creating the response JSON and error handling
            if (auth == null) {
                res.status(403);
                var response = new Responses("Error: already taken");
                return gson.toJson(response);
            } else {
                res.status(200);
                res.type("application/json");
                return gson.toJson(auth);
            }
        } catch (JsonSyntaxException e) {
            // Handle the exception (e.g., log the error, return an error response)
            res.status(400); // Bad Request
            var response = new Responses("Error: bad request");
            return gson.toJson(response);
        }
    }

    public Object login(Request req, Response res) throws DataAccessException {
        var user = gson.fromJson(req.body(), User.class);
        var request = new LoginRequest(user.username(), user.password());
        var auth = userService.login(request.username(), request.password());

        if (auth == null) {
            res.status(401);
            var response = new Responses("Error: unauthorized");
            return gson.toJson(response);
        }

        return gson.toJson(auth);
    }

    public Object logout(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        boolean authorized = true;

        if (authToken != null) {
            authorized = userService.logout(authToken);
            if (authorized) {
                res.status(200);
            }
            else {
                res.status(401);
                var response = new Responses("Error: unauthorized;");
                return gson.toJson(response);
            }
        } else {
            // Handle the case where the header is not present or has an invalid value
            res.status(500);
            var response = new Responses("Error: description");
            return gson.toJson(response);
        }

        return "";
    }

    public Object listGames(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");

        Collection<Game> list = gameService.listGames(authToken);


        if (list == null) {
            res.status(401); // Unauthorized
            var response = new Responses("Error: unauthorized");
            return gson.toJson(response);
        }
        else {
            res.status(200);
            return gson.toJson(Map.of("games", list.toArray()));
        }
    }

    public Object joinGame(Request req, Response res) throws DataAccessException {
        String authToken = req.headers("authorization");
        Auth auth = authDao.getAuth(authToken);
        if (auth == null) {
            res.status(401); // Unauthorized
            var response = new Responses("Error: unauthorized");
            return gson.toJson(response);
        }

        JoinGameRequest request = gson.fromJson(req.body(), JoinGameRequest.class);
        Integer gameID = request.getGameId();
        String playerColor = request.getPlayerColor();

        if (gameID == null) {
            res.status(400);
            var response = new Responses("Error: bad request");
            return gson.toJson(response);
        }

        Game game = gameDao.getGame(gameID);

        if (game == null) {
            res.status(400);
            var response = new Responses("Error: bad request");
            return gson.toJson(response);
        }

        int errorCode = gameService.joinGame(authToken, playerColor, gameID);


        if (errorCode == -2) {
            res.status(403);
            var response = new Responses("Error: already taken");
            return gson.toJson(response);
        }
        else {
            res.status(200); // Unauthorized
            var response = new Responses("");
            return gson.toJson(response);
        }
    }

    public Object createGame(Request req, Response res) {
        try {
            // Check if the request has the required authorization header
            String authToken = req.headers("authorization");
            Auth auth = authDao.getAuth(authToken);

            if (auth == null) {
                res.status(401); // Unauthorized
                var response = new Responses("Error: unauthorized");
                return gson.toJson(response);
            }

            // Deserialize the request body to extract the game name
            CreateGameRequest createGameRequest = gson.fromJson(req.body(), CreateGameRequest.class);

            String gameName = createGameRequest.getGameName();

            // Assuming createGame method returns a game ID
            int gameId = gameService.createGame(gameName);
            if (gameId == 0) {
                res.status(400); // Bad Request
                var response = new Responses("Error: bad request");
                return gson.toJson(response);
            }

            // Return success response
            res.status(200);
            var response = new CreateGameSuccessResponse(gameId);
            return gson.toJson(response);

        } catch (Exception e) {
            // Handle other exceptions
            res.status(500); // Internal Server Error
            var response = new Responses("Error: " + e.getMessage());
            return gson.toJson(response);
        }
    }
}
