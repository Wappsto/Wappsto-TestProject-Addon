package wappsto.iot.rpc;

import java.io.*;

public interface IoTClient {
    void send(String message) throws IOException;
}
