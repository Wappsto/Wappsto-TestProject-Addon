package wappsto.iot.rpc;

import wappsto.iot.rpc.model.*;

public interface ControlState {
    void execute(ControlStateData command);
}
