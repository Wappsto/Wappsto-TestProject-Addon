package wappsto.iot.rpc.model;

import java.util.*;

public class ControlStateData {
    public String id;
    public UUID state;
    public String data;

    public ControlStateData(String id, UUID state, String data) {
        this.id = id;
        this.state = state;
        this.data = data;
    }
}
