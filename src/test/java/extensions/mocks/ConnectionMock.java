package extensions.mocks;

import wappsto.iot.rpc.*;

import java.io.*;

public class ConnectionMock implements Connection {

    @Override
    public void start(Callback messageCallback, Callback errorCallback) {

    }

    @Override
    public void disconnect() throws IOException {

    }

    @Override
    public void send(String message) throws IOException {

    }
}