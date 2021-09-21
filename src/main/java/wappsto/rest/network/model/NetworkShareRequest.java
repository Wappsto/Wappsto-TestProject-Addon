package wappsto.rest.network.model;

import com.fasterxml.jackson.annotation.*;

import java.util.*;

public class NetworkShareRequest {

    @JsonProperty NetworkShareMeta meta;
    @JsonProperty Collection<NetworkShareRestriction> restriction;

    public NetworkShareRequest(String id) {
        meta = new NetworkShareMeta(id);
        restriction = new LinkedList<>();
        restriction.add(new NetworkShareRestriction());
    }
}
