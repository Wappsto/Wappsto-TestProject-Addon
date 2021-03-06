package extensions.mocks;

import wappsto.iot.*;

import java.io.*;

public class InMemoryConnection implements Connection {

    public String lastReceived;
    public boolean isConnected;
    public boolean refuseConnection;

    public InMemoryConnection() {
        isConnected = false;
        refuseConnection = false;
    }

    @Override
    public void start(Callback messageCallback, Callback errorCallback)
        throws Exception
    {
        if (refuseConnection) throw new Exception("Connection refused");
        else isConnected = true;
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