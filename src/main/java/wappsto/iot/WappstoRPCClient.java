package wappsto.iot;

public class WappstoRPCClient {
    private Connection conn;
    private JsonRPCParser parser;

    public WappstoRPCClient(Connection conn) throws InterruptedException {
        this.conn = conn;
        conn.start(this::incoming, this::error);
    }

    private void incoming(String rpc) {
        System.out.println(rpc);
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
