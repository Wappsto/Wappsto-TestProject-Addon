package wappsto.iot.network.model;

import com.fasterxml.jackson.annotation.*;

public class NumberSchema {
    @JsonProperty public int min;
    @JsonProperty public int max;
    @JsonProperty public int step;
    @JsonProperty public String unit;

    public NumberSchema(int min, int max, int step, String type) {
        this.min = min;
        this.max = max;
        this.step = step;
        this.unit = type;
    }

    public NumberSchema(){}
}
