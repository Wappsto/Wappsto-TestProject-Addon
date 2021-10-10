package wappsto.api.network.model;

import com.fasterxml.jackson.annotation.*;

public class NetworkShareMethod {
    @JsonProperty public boolean retrieve;

    public NetworkShareMethod() {
        retrieve = true;
    }
}
