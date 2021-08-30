package wappsto.rest.model;
import com.fasterxml.jackson.annotation.JsonProperty;

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
