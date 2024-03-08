package model;

import com.google.gson.Gson;

public record User(String username, String password, String email) {
    public User setUsername(String username) {
        return new User(username, this.password, this.email);
    }
    public String toString() {
        return new Gson().toJson(this);
    }
}
