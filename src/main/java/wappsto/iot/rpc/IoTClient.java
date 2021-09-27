package wappsto.iot.rpc;

import java.io.*;

public interface IoTClient {
    void start(JsonRPCParser parser);

    void send(String message) throws IOException;

    void stop();
}
