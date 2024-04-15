import server.Server;
public class Main {
    public static void main(String[] args) {
        int port = 8080;
        var server = new Server();

        server.run(port);
    }
}