package wappsto.iot;

import wappsto.iot.rpc.*;

public interface IoTClient {
    void start(JsonRPCParser parser);

    void send(String message);

    void stop();
}
