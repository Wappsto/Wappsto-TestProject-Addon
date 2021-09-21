package wappsto.iot.rpc.model.request;

import java.text.*;
import java.util.*;

public class State {
    public String data;
    public String type;
    public String timestamp;
    public Meta meta;
    public State(String type) {
        data = "";
        this.type = type;
        timestamp = new SimpleDateFormat().format(new Date());
        meta = new Meta("State");
    }
}
