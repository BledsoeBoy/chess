package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.Auth;
import model.Game;
import model.User;
import server.requests.CreateGameRequest;
import server.requests.JoinGameRequest;
import server.requests.LoginRequest;
import server.responses.CreateGameSuccessResponse;

import java.io.*;
import java.net.*;

public class ServerFacade {
    private final String serverUrl;
    private String authToken;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public Auth login(LoginRequest login) throws ResponseException {
        var path = "/session";
        Auth auth = this.makeRequest("POST", path, login, Auth.class);
        authToken = auth.authToken();
        return auth;
    }

    public Auth register(User user) throws ResponseException {
        var path = "/user";
        Auth auth = this.makeRequest("POST", path, user, Auth.class);
        authToken = auth.authToken();
        return auth;
    }

    public CreateGameSuccessResponse createGame(CreateGameRequest gameName) throws ResponseException {
        var path = "/game";
        return this.makeRequest("POST", path, gameName, CreateGameSuccessResponse.class);
    }

    public void joinGame(int id, String playerColor) throws ResponseException {
        var path = "/game";
        JoinGameRequest req = new JoinGameRequest(playerColor, id);
        this.makeRequest("PUT", path, req, null);
    }


    public void logout() throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null);
    }

    public Game[] listGames() throws ResponseException {
        var path = "/game";
        record ListGameResponse(Game[] games) {
        }

        var response = this.makeRequest("GET", path, null, ListGameResponse.class);
        return response.games();
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if (authToken != null) {
                http.addRequestProperty("authorization", authToken);
            }

            writeBody(request, http);

            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
