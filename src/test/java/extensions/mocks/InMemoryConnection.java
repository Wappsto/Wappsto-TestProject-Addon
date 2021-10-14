package extensions.mocks;

import wappsto.iot.*;

import java.io.*;

public class InMemoryConnection implements Connection {

    public String lastReceived;
    public boolean isConnected;

    @Override
    public void start(Callback messageCallback, Callback errorCallback) {
        isConnected = true;
    }

    @Override
    public void disconnect() throws IOException {
        isConnected = false;
    }

    @Override
    public void send(String message) throws IOException {
        lastReceived = message;
    }

    public boolean wasReceived(String message) {
        return lastReceived.contains(message);
    }
}