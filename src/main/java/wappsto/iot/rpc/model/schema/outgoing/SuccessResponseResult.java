package wappsto.iot.rpc.model.schema.outgoing;

import com.fasterxml.jackson.annotation.*;

public class SuccessResponseResult {
    @JsonProperty public boolean success;
    public SuccessResponseResult(boolean success) {
        this.success = success;
    }
}
