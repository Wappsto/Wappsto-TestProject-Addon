package wappsto.iot.rpc;

import wappsto.iot.rpc.model.from.server.*;

public interface Command {
    void execute(JsonRpcMessage command);
}
