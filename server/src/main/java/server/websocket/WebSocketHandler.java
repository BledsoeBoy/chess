package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.GameDAO;
import exception.ResponseException;
import model.Auth;
import model.Game;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.Server;
import webSocketMessages.serverMessages.Error;
import webSocketMessages.serverMessages.LoadGame;
import webSocketMessages.serverMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException, ResponseException {
        UserGameCommand action = new Gson().fromJson(message, UserGameCommand.class);
        switch (action.getCommandType()) {
            case JOIN_PLAYER -> { joinPlayer(new Gson().fromJson(message, JoinPlayer.class), session);
            }
            case JOIN_OBSERVER -> { joinObserver(new Gson().fromJson(message, JoinObserver.class), session);
            }
            case MAKE_MOVE -> { makeMove(new Gson().fromJson(message, MakeMove.class), session);
            }
            case LEAVE -> { leave(new Gson().fromJson(message, Leave.class), session);
            }
            case RESIGN -> { resign(new Gson().fromJson(message, Resign.class), session);
            }
            default -> errorMessage(new Gson().fromJson(message, Error.class));
        }
    }

    private void errorMessage(Error error) throws IOException {
        error.errorMessage = "Error";
        connections.broadcast("", error);
    }

    private void joinPlayer(JoinPlayer joinPlayer, Session session) throws IOException, DataAccessException, ResponseException {
        try {
            String authToken=joinPlayer.getAuthString();
            Auth auth=authDAO.getAuth(authToken);
            connections.add(auth.username(), session);

            Integer gameID=joinPlayer.gameID;
            Game game=gameDAO.getGame(gameID);
            ChessGame chessGame=game.game();
            var loadGame=new LoadGame(chessGame);
            session.getRemote().sendString(new Gson().toJson(loadGame));

            var message=String.format("%s has joined the game on %s team", auth.username(), joinPlayer.playerColor);
            var notification=new Notification(message);
            connections.broadcast(auth.username(), notification);
        } catch (Exception ex) {
            var errorMessage = new Error("Error");
            String authToken = joinPlayer.getAuthString();
            Auth auth = authDAO.getAuth(authToken);
            connections.broadcast(auth.username(), errorMessage);
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private void joinObserver(JoinObserver joinObserver, Session session) throws IOException, DataAccessException {
        Auth auth = authDAO.getAuth(joinObserver.getAuthString());
        connections.add(auth.username(), session);

        var message = String.format("%s has joined the game as an observer", auth.username());
        var notification = new Notification(message);
        connections.broadcast(auth.username(), notification);
    }

    public void makeMove(MakeMove makeMove, Session session) throws ResponseException {
        try {
            Auth auth = authDAO.getAuth(makeMove.getAuthString());
            var message = String.format("%s has joined the game as an observer", auth.username());

            var notification = new Notification(message);
            connections.broadcast("", notification);
        } catch (Exception ex) {
            var Errormessage = new Error("Error");
            //broadcast
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leave(Leave leave, Session session) throws ResponseException {
        try {
            Auth auth = authDAO.getAuth(leave.getAuthString());
            var message = String.format("%s has joined the game as an observer", auth.username());
            var notification = new Notification(message);
            connections.broadcast("", notification);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void resign(Resign resign, Session session) throws ResponseException {
        try {
            Auth auth = authDAO.getAuth(resign.getAuthString());
            var message = String.format("%s has joined the game as an observer", auth.username());
            var notification = new Notification(message);
            connections.broadcast("", notification);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}