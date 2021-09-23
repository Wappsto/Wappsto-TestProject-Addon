package wappsto.iot.rpc;

import wappsto.iot.rpc.model.from.server.*;

import java.util.*;

public class ControlStateData {
    public UUID state;
    public String data;

    public ControlStateData(JsonRPCRequestFromServer request) {
        this.state = request.params.data.meta.id;
        this.data = request.params.data.data;
    }
}
