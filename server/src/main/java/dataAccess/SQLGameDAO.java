package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.Game;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGameDAO implements GameDAO{
    public SQLGameDAO() throws DataAccessException {
        configureDatabase();
    }

    public void clearGames() throws DataAccessException {
        var statement = "TRUNCATE game";
        executeUpdate(statement);
    }
    public int createGame(Game game) throws DataAccessException {
        ChessGame chessGame = game.game();
        var statement = "INSERT INTO game (whiteUsername, blackUsername, gameName, chessGame) VALUES (?, ?, ?, ?)";
        var chessGameJson = new Gson().toJson(chessGame);
        var id = executeUpdate(statement, game.whiteUsername(), game.blackUsername(), game.gameName(), chessGameJson);
        return id;
    }
    public void updateGame(String playerColor, Game game, String username) throws DataAccessException {
        if (playerColor.equals("WHITE")) {
            var statement = "UPDATE game SET whiteUsername=? WHERE gameID=?";
            executeUpdate(statement, username, game.gameID());
        } else if (playerColor.equals("BLACK")) {
            var statement = "UPDATE game SET blackUsername=? WHERE gameID=?";
            executeUpdate(statement, username, game.gameID());
        }
    }
    public Game getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM game WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readGame(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
            return null;
    }
    public Collection<Game> listGames() throws DataAccessException {
        var result = new ArrayList<Game>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    private Game readGame(ResultSet rs) throws SQLException {
        var id = rs.getInt("gameID");
        var whiteUsername = rs.getString("whiteUsername");
        var blackUsername = rs.getString("blackUsername");
        var gameName = rs.getString("gameName");
        var chessGameString = rs.getString("chessGame");
        var chessGame = new Gson().fromJson(chessGameString, ChessGame.class);
        Game response = new Game(id, whiteUsername, blackUsername, gameName, chessGame);
        return response;
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
    CREATE TABLE IF NOT EXISTS game (
      `gameID` int NOT NULL AUTO_INCREMENT,
      `whiteUsername` varchar(256),
      `blackUsername` varchar(256),
      `gameName` varchar(256) NOT NULL,
      `chessGame` TEXT NOT NULL,
      PRIMARY KEY (`gameID`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
    """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
