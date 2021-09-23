package wappsto.iot.rpc;

import com.fasterxml.jackson.databind.*;
import wappsto.iot.rpc.model.*;
import wappsto.iot.ssl.*;

import java.io.*;

public class RPCClient implements IoTClient {
    private final ObjectMapper mapper;
    private final Connection conn;

    public RPCClient(Connection conn) {
        this.conn = conn;
        this.mapper = new ObjectMapper();
        conn.start(this::incoming, this::error);
    }

    public RPCClient(SSLConnection connection, RPCRequest jsonRPCRequest)
        throws IOException
    {
        this(connection);
        String self = new ObjectMapper().writeValueAsString(jsonRPCRequest);
        System.out.println(self);
        send(self);
    }



    @Override
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
