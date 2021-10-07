package wappsto.network.model;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.network.model.*;
import wappsto.iot.ssl.model.*;

public class NetworkInstance {
    @JsonProperty public WappstoCerts certs;
    @JsonProperty public NetworkSchema schema;

    public NetworkInstance(WappstoCerts wappstoCerts, NetworkSchema schema) {
        this.certs = wappstoCerts;
        this.schema = schema;
    }
    public NetworkInstance(){};
}
