package server;
import com.google.gson.Gson;
import dataAccess.*;
import handlers.Handlers;
import service.AuthService;
import service.GameService;
import service.UserService;
import spark.*;
import dataAccess.SQLAuthDAO;
public class Server {
    public static int run(int desiredPort) {
        try {
            Spark.port(desiredPort);

            Spark.staticFiles.location("web");

            AuthDAO authDAO = new SQLAuthDAO();
            GameDAO gameDAO = new SQLGameDAO();
            UserDAO userDAO = new MemoryUserDAO();
            AuthService authService = new AuthService(authDAO, gameDAO, userDAO);
            UserService userService = new UserService(authDAO, userDAO);
            GameService gameService = new GameService(authDAO, gameDAO);
            Gson gson = new Gson();

            Handlers handlers = new Handlers(authService, userService, gameService, authDAO, gameDAO, gson);

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
        catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
            return -1;
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
