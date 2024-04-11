package ui;

import com.google.gson.Gson;
import exception.ResponseException;
import model.Auth;
import model.Game;
import model.User;
import server.ServerFacade;
import server.requests.CreateGameRequest;
import server.requests.LoginRequest;
import server.responses.CreateGameSuccessResponse;
import ui.websocket.NotificationHandler;
import ui.websocket.WebSocketFacade;

import static ui.EscapeSequences.*;

import java.util.*;

public class ChessClient {
    private String playerName = null;
    private final ServerFacade server;
    private Integer gameID = null;
    private final String serverUrl;
    private String authToken = null;
    private final Map<Integer, Game> list = new HashMap<>();
    private final NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    public State state = State.LOGGED_OUT;
    public boolean isJoined = false;

    public ChessClient(String serverUrl, NotificationHandler notificationHandler) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
    }

    public String eval(String input) {
        try {
            var tokens = input.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "create" -> createGame(params);
                case "list" -> listGames();
                case "logout" -> logout();
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) throws ResponseException {
        if (params.length >= 2) {
            state = State.LOGGED_IN;
            playerName = params[0];
            var password = params[1];
            var login = new LoginRequest(playerName, password);
            Auth auth = server.login(login);
            authToken = auth.authToken();
            return String.format("You logged in as %s.", playerName);
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    public String register(String... params) throws ResponseException {
        if (params.length >= 3) {
            state = State.LOGGED_IN;
            playerName = params[0];
            var password = params[1];
            var email = params[2];
            User user = new User(playerName, password, email);
            Auth auth = server.register(user);
            authToken = auth.authToken();
            return String.format("You registered as %s.", playerName);
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }

    public String createGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 1) {
            var gameName = String.join(" ", params);
            CreateGameRequest game = new CreateGameRequest();
            game.setGameName(gameName);
            CreateGameSuccessResponse response = server.createGame(game);
            gameID = response.getGameID();
            String output = String.format("You created the game with gameName: %s", gameName);
            return output;
        }
        throw new ResponseException(400, "Expected: <NAME>");
    }

    public String joinGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length >= 2) {
            try {
                var identifier = Integer.parseInt(params[0]);
                var color = params[1];
                String playerColor = color.toUpperCase();
                var game = getGame(identifier);

                if (game != null) {
                    server.joinGame(game.gameID(), playerColor);
                    isJoined = true;
                    ws = new WebSocketFacade(serverUrl, notificationHandler);
                    if (playerColor.equals("WHITE")) {
                        ws.joinPlayer(authToken, game.gameID(), chess.ChessGame.TeamColor.WHITE);
                    }
                    else {
                        ws.joinPlayer(authToken, game.gameID(), chess.ChessGame.TeamColor.BLACK);
                    }
                    ChessGame.run(playerColor);
                    return String.format("\nYou are now playing on game: %s", game.gameName());
                }
            } catch (NumberFormatException ignored) {
            }
        }
        throw new ResponseException(400, "Expected: <ID> [WHITE|BLACK|<empty>]");
    }

    public String observeGame(String... params) throws ResponseException {
        assertSignedIn();
        if (params.length == 1) {
            try {
                var id = Integer.parseInt(params[0]);
                var game = getGame(id);
                if (game != null) {
                    server.joinGame(game.gameID(), null);
                    ChessGame.run("WHITE");
                    return String.format("\nYou are now observing on game: %s", game.gameName());
                }
            } catch (NumberFormatException ignored) {
            }
        }
        throw new ResponseException(400, "Expected: <ID>");
    }

    public String listGames() throws ResponseException {
        assertSignedIn();
        var games = server.listGames();
        var result = new StringBuilder();
        if (games == null) {
            return "No games";
        }
        int count = 1;

        for (var game : games) {
            String name = game.gameName();
            String whiteUsername = game.whiteUsername();
            String blackUsername = game.blackUsername();
            if(whiteUsername == null) {
                whiteUsername = "No player";
            }

            if(blackUsername == null) {
                blackUsername = "No player";
            }

            result.append(SET_TEXT_COLOR_BLUE).append(count).append(". ").append(SET_TEXT_COLOR_MAGENTA).append(" Game Name: ").
                    append(SET_TEXT_COLOR_BLUE).append(name).append(SET_TEXT_COLOR_MAGENTA).append(" White User: ").append(SET_TEXT_COLOR_BLUE).
                    append(whiteUsername).append(SET_TEXT_COLOR_MAGENTA).append(" Black User: ").append(SET_TEXT_COLOR_BLUE).append(blackUsername).append("\n");
            list.put(count, game);
            count++;
        }

        return result.toString();
    }

    public String logout() throws ResponseException {
        assertSignedIn();
        state = State.LOGGED_OUT;
        server.logout();
        return String.format("%s left the game", playerName);
    }

    private Game getGame(int identifier) throws ResponseException {
        if (server.listGames() == null) {
            return null;
        }

        for (Integer key : list.keySet()) {
            if (key == identifier) {
                Game game = list.get(key);
                return game;
            }
        }
        return null;
    }

    public String help() {
        if (state == State.LOGGED_OUT) {
            return """
                    - register <username> <password> <email> - to create an account
                    - login <username> <password> - to play chess
                    - quit - playing chess
                    - help - with possible commands
                    """;
        } else {
            if (isJoined) {
                return """
                    - redraw - redraws chess board
                    - leave - current game
                    - makeMove - input move
                    - resign - restarts game
                    - highlight - legal moves
                    - help - with possible commands
                    """;
            }
            else {
                return """
                        - create <NAME> - a game
                        - list - games
                        - join <ID> [WHITE|BLACK|<empty>] - a game
                        - observe <ID> - a game
                        - logout - when you are done
                        - quit - playing chess
                        - help - with possible commands
                        """;
            }
        }
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.LOGGED_OUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }
}
