package wappsto.iot;

import wappsto.iot.exceptions.InvalidMessage;

import java.io.IOException;

public class WappstoRPCClient {
    private Connection conn;
    private JsonRPCParser parser;

    public WappstoRPCClient(Connection conn) throws InterruptedException {
        this.conn = conn;
        conn.start(
            this::handleIncoming,
            this::handleError
        );

    }

    private void handleIncoming(String rpc) {
        System.out.println(rpc);
    }

    private void handleError(String error) {
        try {
            conn.disconnect();
            conn.start(this::handleIncoming, this::handleError);
        } catch (Exception e) {
            System.out.println("Failed to reestablish connection.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

}
