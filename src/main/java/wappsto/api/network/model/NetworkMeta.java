package wappsto.api.network.model;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NetworkMeta {
    @JsonProperty
    public String id;

    @JsonProperty
    public List<String> device;
}
