package wappsto.api.network.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ValueResponse {
    @JsonProperty
    public String name;
}
