package wappsto.iot.rpc.model.schema.network;

import com.fasterxml.jackson.annotation.*;

public class Number {
    @JsonProperty public int min;
    @JsonProperty public int max;
    @JsonProperty public int step;
    @JsonProperty public String unit;

    public Number(int min, int max, int step, String unit) {
        this.min = min;
        this.max = max;
        this.step = step;
        this.unit = unit;
    }
}
