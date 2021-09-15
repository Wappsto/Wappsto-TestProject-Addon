package wappsto.iot;

public class WappstoRPCClient {
    private Connection conn;
    private JsonRPCParser parser;

    public WappstoRPCClient(Connection conn)  {
        this.conn = conn;
        conn.setIncomingCallback(new Callback() {
            @Override
            public void parse(String content) {
                handleIncoming(content);
            }
        });

    }

    private void handleIncoming(String rpc) {
    }

}
