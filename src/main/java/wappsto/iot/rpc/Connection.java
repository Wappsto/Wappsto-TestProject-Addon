package wappsto.iot.rpc;

import java.io.IOException;

public interface Connection {
    void start(Callback messageCallback, Callback errorCallback);
    void disconnect() throws IOException;
    void send(String s) throws IOException;
}
