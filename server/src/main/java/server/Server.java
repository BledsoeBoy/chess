package server;
import com.google.gson.Gson;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import handlers.Handlers;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        AuthService authService = new AuthService();
        UserService userService = new UserService();
        GameService gameService = new GameService();
        MemoryAuthDAO authDao = new MemoryAuthDAO();
        MemoryGameDAO gameDao = new MemoryGameDAO();
        Gson gson = new Gson();

        Handlers handlers = new Handlers(authService, userService, gameService, authDao, gameDao, gson);

        // No need to create an instance of Handlers since the methods are static
        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", handlers::clearApp);
        Spark.post("/user", handlers::register);
        Spark.post("/session", handlers::login);
        Spark.delete("/session", handlers::logout);
        Spark.get("/game", handlers::listGames);
        Spark.post("/game", handlers::createGame);
        Spark.put("/game", handlers::joinGame);

        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
