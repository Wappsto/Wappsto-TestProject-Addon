package wappsto.iot.rpc;

import wappsto.iot.rpc.model.*;

public interface ResultStrategy {
    void execute(ResponseData data);
}
