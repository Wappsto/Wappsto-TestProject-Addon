package wappsto.api.network.model;

import com.fasterxml.jackson.annotation.*;

public class NetworkShareMeta {
    @JsonProperty public String id;

    public NetworkShareMeta(String id) {
        this.id = id;
    }
}
