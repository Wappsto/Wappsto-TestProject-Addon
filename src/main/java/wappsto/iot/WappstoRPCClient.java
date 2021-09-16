package wappsto.iot;

import wappsto.iot.exceptions.InvalidMessage;

public class WappstoRPCClient {
    private Connection conn;
    private JsonRPCParser parser;
    public String msg = "";
    public String error = "";

    public WappstoRPCClient(Connection conn) throws InterruptedException {
        this.conn = conn;
        conn.start(
            this::handleIncoming,
            this::handleError
        );

    }

    private void handleIncoming(String rpc) {
        msg = rpc;
    }

    private void handleError(String error)  {
        this.error = error;
    }

}
