package wappsto.rest.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionResponse {
    @JsonProperty("meta")
    public SessionId sessionId;
}
