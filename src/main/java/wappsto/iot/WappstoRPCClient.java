package wappsto.iot;

public class WappstoRPCClient {
    private Connection conn;
    private JsonRPCParser parser;

    public WappstoRPCClient(Connection conn) throws InterruptedException {
        this.conn = conn;
        conn.setIncomingCallback(content -> handleIncoming(content));

    }

    private void handleIncoming(String rpc) {
        System.out.println(rpc);
    }

}
