package wappsto.iot;

import wappsto.iot.rpc.*;

public interface IoTClient {
    void start(RpcParser parser);

    void send(String message);

    void stop();
}
