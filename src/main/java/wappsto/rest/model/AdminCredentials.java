package wappsto.rest.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties
public class AdminCredentials extends Credentials {

    @JsonProperty("admin")
    public boolean admin = true;

    public AdminCredentials(String username, String password) {
        super(username, password);

    }
}
