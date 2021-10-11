package wappsto.iot.rpc.model;

import java.util.*;

public class StateData {
    public String id;
    public UUID state;
    public String data;

    public StateData(UUID state, String data) {
        this.state = state;
        this.data = data;
    }
}
