package wappsto.session.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {
    @JsonProperty("email")
    public String username;
    @JsonProperty("meta")
    public UserMeta meta;
}
