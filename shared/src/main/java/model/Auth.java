package model;

import com.google.gson.Gson;

import java.util.UUID;

public record Auth(String authToken, String username) {

    public Auth setAuthToken(String authToken) {
        return new Auth(authToken, this.username);
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
