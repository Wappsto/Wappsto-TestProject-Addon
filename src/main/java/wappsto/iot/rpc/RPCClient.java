package wappsto.iot.rpc;

import com.fasterxml.jackson.databind.*;
import wappsto.iot.rpc.model.schema.*;
import wappsto.iot.ssl.*;

import java.io.*;

public class RPCClient {
    private Connection conn;

    public RPCClient(Connection conn) {
        this.conn = conn;
        conn.start(this::incoming, this::error);
    }

    public RPCClient(SSLConnection connection, JsonRPCRequest jsonRPCRequest)
        throws IOException
    {
        this(connection);
        send(new ObjectMapper().writeValueAsString(jsonRPCRequest));
    }



    public void send(String message) throws IOException {
        conn.send(message);
    }

    private void incoming(String rpc) {
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
