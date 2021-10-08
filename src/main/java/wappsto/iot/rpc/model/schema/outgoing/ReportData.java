package wappsto.iot.rpc.model.schema.outgoing;

import com.fasterxml.jackson.annotation.*;
import wappsto.iot.rpc.model.schema.*;

public class ReportData extends Data {
    @JsonProperty public String data;

    public ReportData(String data) {
        this.data = data;
    }
}
