package wappsto.rest.session.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    @JsonProperty("email")
    public String username;
}
