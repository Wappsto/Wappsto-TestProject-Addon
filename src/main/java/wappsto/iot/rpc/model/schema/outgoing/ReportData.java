package wappsto.iot.rpc.model.schema.outgoing;

import com.fasterxml.jackson.annotation.*;

public class ReportData extends Data {
    @JsonProperty public String data;

    public ReportData(String data) {
        this.data = data;
    }
}
