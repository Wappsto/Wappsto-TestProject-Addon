package extensions.mocks;

import wappsto.iot.*;

import java.io.*;

public class InMemoryConnection implements Connection {

    public String lastReceived;

    @Override
    public void start(Callback messageCallback, Callback errorCallback) {

    }

    @Override
    public void disconnect() throws IOException {

    }

    @Override
    public void send(String message) throws IOException {
        lastReceived = message;
    }
}