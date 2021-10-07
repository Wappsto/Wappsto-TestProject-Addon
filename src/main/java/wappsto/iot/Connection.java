package wappsto.iot;

import java.io.*;

public interface Connection {
    void start(Callback messageCallback, Callback errorCallback);
    void disconnect() throws IOException;
    void send(String message) throws IOException;
}
