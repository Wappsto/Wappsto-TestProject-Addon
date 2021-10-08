package wappsto.iot.network.model;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.rpc.model.schema.*;

import java.text.*;
import java.util.*;

import static wappsto.Util.*;

public class StateSchema {
    @JsonProperty public String data;
    @JsonProperty public String type;
    @JsonProperty public String timestamp;
    @JsonProperty public Meta meta;

    public StateSchema(String type) {
        data = "";
        this.type = type;
        SimpleDateFormat sdf = new SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
        );
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        timestamp = getCurrentTimestamp();
        meta = new Meta("State");
    }
    public StateSchema(){}
}
