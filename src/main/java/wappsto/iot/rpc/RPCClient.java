package wappsto.iot.rpc;

import com.fasterxml.jackson.databind.*;
import wappsto.iot.rpc.model.request.*;
import wappsto.iot.ssl.*;

import java.io.*;

import static javax.ws.rs.client.Entity.json;

public class RPCClient {
    private VirtualNetwork network;
    private Connection conn;
    private JsonRPCParser parser;

    public RPCClient(Connection conn) {
        this.conn = conn;
        conn.start(this::incoming, this::error);
    }

    public RPCClient(SSLConnection connection, VirtualNetwork network)
        throws IOException
    {
        this(connection);
        this.network = network;
        send(new ObjectMapper().writeValueAsString(network));
    }

    public void send(String message) throws IOException {
        conn.send(message);
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
