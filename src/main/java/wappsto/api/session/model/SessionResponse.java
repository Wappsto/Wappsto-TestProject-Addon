package wappsto.api.session.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionResponse {
    @JsonProperty("meta")
    public SessionMeta sessionMeta;
}
