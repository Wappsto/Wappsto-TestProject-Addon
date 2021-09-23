package wappsto.iot.rpc.model;

import com.fasterxml.jackson.annotation.*;

public class ReportData extends Data {
    @JsonProperty public String data;

    public ReportData(String data) {
        this.data = data;
    }
}
