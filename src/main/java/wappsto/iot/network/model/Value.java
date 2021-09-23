package wappsto.iot.network.model;

import java.util.*;

public class Value {
    public UUID reportState;
    public String value;
    public Value(UUID reportState, String value) {
        this.reportState = reportState;
        this.value = value;
    }
}
