package wappsto.iot.rpc;

import java.io.*;

public class RPCClient implements IoTClient {
    private final Connection conn;
    private JsonRPCParser parser;

    public RPCClient(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void start(JsonRPCParser parser) {
        this.parser = parser;
        conn.start(this::incoming, this::error);
    }

    @Override
    public void send(String message) throws IOException {
        conn.send(message);
    }

    @Override
    public void stop() {
        try {
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void incoming(String data) {
        parser.parse(data);
    }

    private void error(String error) {
        try {
            conn.disconnect();
            conn.start(this::incoming, this::error);
        } catch (Exception e) {
            System.out.println("Failed to reestablish connection.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
