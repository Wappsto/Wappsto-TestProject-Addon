package wappsto.api.network.model;

import com.fasterxml.jackson.annotation.*;

public class CreatorRequest {
    @JsonProperty("manufacturer_as_owner")
    boolean manufacturerAsOwner;
    public CreatorRequest(boolean manufacturerAsOwner) {
        this.manufacturerAsOwner = manufacturerAsOwner;
    }
}
