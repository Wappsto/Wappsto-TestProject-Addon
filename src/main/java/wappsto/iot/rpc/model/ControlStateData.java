package wappsto.iot.rpc.model;

import java.util.*;

public class ControlStateData {
    public String id;
    public UUID state;
    public String data;

    public ControlStateData(UUID state, String data) {
        this.state = state;
        this.data = data;
    }
}
