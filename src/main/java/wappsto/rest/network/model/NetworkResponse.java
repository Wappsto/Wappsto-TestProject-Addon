package wappsto.rest.network.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NetworkResponse {
    @JsonProperty ("meta") public Network network;
}