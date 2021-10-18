package wappsto.iot.network.model;

import com.fasterxml.jackson.annotation.*;

public class NumberSchema {
    @JsonProperty public float min;
    @JsonProperty public float max;
    @JsonProperty public float step;
    @JsonProperty public String unit;

    public NumberSchema(float min, float max, float step, String type) {
        this.min = min;
        this.max = max;
        this.step = step;
        this.unit = type;
    }

    public NumberSchema(){}
}
