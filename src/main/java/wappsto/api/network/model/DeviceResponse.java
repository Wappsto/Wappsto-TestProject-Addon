package wappsto.api.network.model;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceResponse {
    @JsonProperty
    public List<UUID> value;
}
