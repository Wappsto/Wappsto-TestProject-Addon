package wappsto.api.network.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateDeviceResponse {
    @JsonProperty
    public CreateDeviceMeta meta;
}
