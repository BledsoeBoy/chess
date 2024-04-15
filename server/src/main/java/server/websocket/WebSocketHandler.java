package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataAccess.*;
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
import java.util.Objects;
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
            default -> errorMessage(new Gson().fromJson(message, Error.class), session);
        }
    }

    private void errorMessage(Error error, Session session) throws IOException {
        error.errorMessage = "Error";
        session.getRemote().sendString(new Gson().toJson(error));
    }

    private void joinPlayer(JoinPlayer joinPlayer, Session session) throws IOException, DataAccessException, ResponseException {
        try {
            String authToken = joinPlayer.getAuthString();
            Auth auth = authDAO.getAuth(authToken);

            Integer gameID=joinPlayer.gameID;
            Game game=gameDAO.getGame(gameID);
            if ((joinPlayer.playerColor == ChessGame.TeamColor.WHITE) && !Objects.equals(auth.username(), game.whiteUsername())) {
                var errorMessage = new Error("Error");
                session.getRemote().sendString(new Gson().toJson(errorMessage));
            } else if ((joinPlayer.playerColor == ChessGame.TeamColor.BLACK) && !Objects.equals(auth.username(), game.blackUsername())) {
                var errorMessage = new Error("Error");
                session.getRemote().sendString(new Gson().toJson(errorMessage));
            }
            else {
                ChessGame chessGame=game.game();
                var loadGame=new LoadGame(chessGame);
                session.getRemote().sendString(new Gson().toJson(loadGame));
                connections.add(auth.username(), joinPlayer.gameID, session);

                var message=String.format("%s has joined the game on %s team", auth.username(), joinPlayer.playerColor);
                var notification=new Notification(message);
                connections.broadcast(auth.username(), joinPlayer.gameID, notification);
            }
        } catch (Exception ex) {
            var errorMessage = new Error("Error");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private void joinObserver(JoinObserver joinObserver, Session session) throws IOException, DataAccessException, ResponseException {
        try {
            String authToken = joinObserver.getAuthString();
            Auth auth = authDAO.getAuth(authToken);
            connections.add(auth.username(), joinObserver.gameID, session);

            Integer gameID = joinObserver.gameID;
            Game game = gameDAO.getGame(gameID);
            ChessGame chessGame = game.game();
            var loadGame = new LoadGame(chessGame);
            session.getRemote().sendString(new Gson().toJson(loadGame));

            var message=String.format("%s has joined the game as an observer", auth.username());
            var notification=new Notification(message);
            connections.broadcast(auth.username(), joinObserver.gameID, notification);
        } catch (Exception ex) {
            var errorMessage = new Error("Error");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void makeMove(MakeMove makeMove, Session session) throws ResponseException, IOException {
        try {
            String authToken = makeMove.getAuthString();
            Auth auth = authDAO.getAuth(authToken);

            Integer gameID = makeMove.gameID;
            Game game = gameDAO.getGame(gameID);
            ChessGame chessGame = game.game();
            var loadGame = new LoadGame(chessGame);
            session.getRemote().sendString(new Gson().toJson(loadGame));

            var message=String.format("%s has joined the game as an observer", auth.username());
            var notification=new Notification(message);
            connections.broadcast(auth.username(), makeMove.gameID, notification);
        } catch (Exception ex) {
            var errorMessage = new Error("Error");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leave(Leave leave, Session session) throws ResponseException, IOException {
        try {
            String authToken = leave.getAuthString();
            Auth auth = authDAO.getAuth(authToken);

            Integer gameID = leave.gameID;
            Game game = gameDAO.getGame(gameID);
            if (Objects.equals(auth.username(), game.whiteUsername())) {
                gameDAO.updateGame("WHITE", game, null);
            } else if (Objects.equals(auth.username(), game.blackUsername())) {
                gameDAO.updateGame("BLACK", game, null);
            }

            var message=String.format("%s has left the game", auth.username());
            var notification=new Notification(message);
            connections.broadcast(auth.username(), leave.gameID, notification);
            connections.remove(auth.username());
        } catch (Exception ex) {
            var errorMessage = new Error("Error");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void resign(Resign resign, Session session) throws ResponseException, IOException {
        try {
            String authToken = resign.getAuthString();
            Auth auth = authDAO.getAuth(authToken);

            Integer gameID = resign.gameID;
            Game game = gameDAO.getGame(gameID);
            if (Objects.equals(auth.username(), game.whiteUsername())) {
                gameDAO.updateGame("WHITE", game, null);
            } else if (Objects.equals(auth.username(), game.blackUsername())) {
                gameDAO.updateGame("BLACK", game, null);
            }

            var message=String.format("%s has left the game", auth.username());
            var notification=new Notification(message);
            connections.broadcast(auth.username(), resign.gameID, notification);
            connections.remove(auth.username());
        } catch (Exception ex) {
            var errorMessage = new Error("Error");
            session.getRemote().sendString(new Gson().toJson(errorMessage));
            throw new ResponseException(500, ex.getMessage());
        }
    }
}