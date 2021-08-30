package wappsto.rest.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SessionId {
    @JsonProperty("id")
    public String id;
}
