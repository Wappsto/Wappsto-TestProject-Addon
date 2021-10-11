package wappsto.iot.network.model;

import java.util.*;

public class ControlValue {
    public UUID reportState;
    public String value;
    public ControlValue(UUID reportState, String value) {
        this.reportState = reportState;
        this.value = value;
    }
}
