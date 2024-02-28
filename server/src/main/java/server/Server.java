package server;
import handlers.Handlers;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        var handlers = new Handlers();
        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", handlers::clearApp);
        Spark.post("/user", handlers::register);
        Spark.post("/session", handlers::login);
        Spark.delete("/session", handlers::logout);
//        Spark.get("/game", this::listGames);
        Spark.post("/game", handlers::createGame);
//        Spark.put("/game", this::joinGame);

        Spark.init();
        Spark.awaitInitialization();
        return Spark.port();
    }


    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
