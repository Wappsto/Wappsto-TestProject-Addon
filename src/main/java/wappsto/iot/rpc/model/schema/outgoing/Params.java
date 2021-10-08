package wappsto.iot.rpc.model.schema.outgoing;

import com.fasterxml.jackson.annotation.*;

public class Params {
    @JsonProperty public String url;
    @JsonProperty public Data data;

    public Params(String url, Data data) {
        this.url = url;
        this.data = data;
    }
}
