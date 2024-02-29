package server;
import handlers.Handlers;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        // No need to create an instance of Handlers since the methods are static
        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", Handlers::clearApp);
        Spark.post("/user", Handlers::register);
        Spark.post("/session", Handlers::login);
        Spark.delete("/session", Handlers::logout);
        Spark.get("/game", Handlers::listGames);
        Spark.post("/game", Handlers::createGame);
        Spark.put("/game", Handlers::joinGame);

        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
