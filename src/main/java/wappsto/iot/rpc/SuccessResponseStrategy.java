package wappsto.iot.rpc;

import wappsto.iot.rpc.model.*;

public interface SuccessResponseStrategy {
    void execute(SuccessResponseToServer response);
}
