package wappsto.rest.network.model;

import com.fasterxml.jackson.annotation.*;

import static wappsto.Util.*;

public class UpdateStateRequest {
    @JsonProperty String data;
    @JsonProperty String timestamp;
    public UpdateStateRequest(String data) {
        this.data = data;
        this.timestamp = getCurrentTimestamp();
    }
}
