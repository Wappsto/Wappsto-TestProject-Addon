package unit.actions.iot;

import actions.iot.*;
import org.junit.jupiter.api.*;
import wappsto.iot.network.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;

import java.io.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class RunSimpleRPCClientControllerTest {
    @Test
    public void creates_virtual_iot_network() {
        Connection connection = new ConnectionMock();
        UUID networkId = UUID.randomUUID();
        RunSimpleRPCClient.Controller controller = new RunSimpleRPCClient
            .Controller(
                "Test",
                networkId.toString(),
                new NumberSchema(0, 1, 1, "Boolean"),
                connection
            );
        VirtualIoTNetwork network = controller.execute();
        assertEquals(networkId, network.schema.meta.id);
    }

    private class ConnectionMock implements Connection {

        @Override
        public void start(Callback messageCallback, Callback errorCallback) {

        }

        @Override
        public void disconnect() throws IOException {

        }

        @Override
        public void send(String message) throws IOException {

        }
    }
}
