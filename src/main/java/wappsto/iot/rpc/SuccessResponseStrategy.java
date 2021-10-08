package wappsto.iot.rpc;

import wappsto.iot.rpc.model.schema.outgoing.*;

public interface SuccessResponseStrategy {
    void execute(RpcOutgoingResult response);
}
