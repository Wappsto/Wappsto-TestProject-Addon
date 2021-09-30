package wappsto.network.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NetworkMeta {
    @JsonProperty
    public String id;
}
