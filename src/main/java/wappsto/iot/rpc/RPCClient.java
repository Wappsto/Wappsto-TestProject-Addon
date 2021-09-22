package wappsto.iot.rpc;

import com.fasterxml.jackson.databind.*;
import wappsto.iot.model.schema.*;
import wappsto.iot.model.schema.network.*;
import wappsto.iot.ssl.*;

import java.io.*;

public class RPCClient implements IoTClient {
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



    @Override
    public void send(String message) throws IOException {
        conn.send(message);
    }

    @Override
    public void publish(NetworkSchema schema) {

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
