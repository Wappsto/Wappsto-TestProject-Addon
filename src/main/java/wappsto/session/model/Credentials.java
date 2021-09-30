package wappsto.session.model;

import com.fasterxml.jackson.annotation.*;

public class Credentials {
    @JsonProperty("username")
    public String username;
    @JsonProperty("password")
    public String password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
