package wappsto.rest.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteUserRequest {
    @JsonProperty
    public String username;

    public DeleteUserRequest(String username) {
        this.username = username;
    }
}
