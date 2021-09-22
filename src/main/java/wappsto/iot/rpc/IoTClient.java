package wappsto.iot.rpc;

import wappsto.iot.model.schema.network.*;

import java.io.*;

public interface IoTClient {
    void send(String message) throws IOException;

    void publish(NetworkSchema schema);
}
