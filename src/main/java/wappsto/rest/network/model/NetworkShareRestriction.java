package wappsto.rest.network.model;

import com.fasterxml.jackson.annotation.*;

public class NetworkShareRestriction {
    @JsonProperty public NetworkShareMethod method;

    public NetworkShareRestriction() {
        method = new NetworkShareMethod();
    }
}
