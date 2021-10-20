package wappsto.iot.rpc;

import wappsto.iot.*;

import java.io.*;

/**
 * Responsible for mediating messages between the server connection
 * and the simulated Iot network
 */
public class RpcClient implements IoTClient {
    private final Connection conn;
    private RpcParser parser;

    /**
     * Instantiate client with a connection
     * @param conn connection to the server
     */
    public RpcClient(Connection conn) {
        this.conn = conn;
    }

    /**
     * Starts the connection and sets the parser for incoming data
     * @param parser parses incoming JSON-RPC into commands
     * @throws Exception
     */
    @Override
    public void start(RpcParser parser) throws Exception {
        this.parser = parser;
        conn.start(this::incoming, this::error);
    }

    /**
     * Send message to the server
     * @param message
     */
    @Override
    public void send(String message) {
        try {
            conn.send(message);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Close the connection to the server
     */
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
        }
    }
}
