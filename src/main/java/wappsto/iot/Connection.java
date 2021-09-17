package wappsto.iot;

import java.io.IOException;

public interface Connection {
    void start(Callback messageCallback, Callback errorCallback) throws InterruptedException;
    void disconnect() throws IOException;
    void send(String s) throws IOException;
}
