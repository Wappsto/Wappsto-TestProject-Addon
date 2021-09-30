package wappsto.iot.rpc;

import wappsto.iot.ssl.*;
import wappsto.iot.ssl.model.*;
import wappsto.network.model.*;

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
    public void send(String message) {
        try {
            conn.send(message);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void stop() {
        try {
            conn.disconnect();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
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

    public static class Builder {
        private WappstoCerts certs;
        private SSLConnection connection;

        public Builder(CreatorResponse creator) {
            certs = new WappstoCerts(creator);
        }

        public Builder connectingTo(String address, int port) throws Exception {
            connection = new SSLConnection(address, port, certs);
            return this;
        }

        public RPCClient build() {
            if(connection == null) {
                throw new RuntimeException("Connection cannot be null");
            }
            return new RPCClient(connection);
        }
    }
}
