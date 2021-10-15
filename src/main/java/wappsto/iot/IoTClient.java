package wappsto.iot;

import wappsto.iot.rpc.*;

public interface IoTClient {
    void start(RpcParser parser) throws Exception;

    void send(String message);

    void stop();
}
