package wappsto.iot.rpc.model.schema.network;

import com.fasterxml.jackson.annotation.*;

public class Network {
    @JsonProperty public String url;
    @JsonProperty public Data data;

    public Network(String url, Data data) {
        this.url = url;
        this.data = data;
    }
}
