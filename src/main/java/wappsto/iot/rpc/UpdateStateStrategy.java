package wappsto.iot.rpc;

import wappsto.iot.rpc.model.*;

public interface UpdateStateStrategy {
    void execute(StateData command);
}
