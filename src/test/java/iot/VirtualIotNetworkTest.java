package iot;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import wappsto.iot.network.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;
import wappsto.rest.network.model.*;

import java.util.*;

import static iot.Utils.defaultNetwork;
import static org.junit.jupiter.api.Assertions.*;

public class VirtualIotNetworkTest {
    private static NetworkSchema schema;
    private IoTClientMock client;

    @BeforeAll
    public static void setup() {
        CreatorResponse creatorMock = new CreatorResponse();
        creatorMock.network = new NetworkMeta();
        creatorMock.network.id = UUID.randomUUID().toString();
        schema = defaultNetwork(creatorMock);
    }

    @BeforeEach
    public void instantiateClient() {
        client = new IoTClientMock();
    }

    @Nested
    public class reports_its_state {
        @Test
        public void on_startup() throws Exception {

            VirtualIoTNetwork network = new VirtualIoTNetwork(schema, client);
            assertTrue(client.state.contains(schema.meta.id.toString()));
        }

        @Test
        public void on_state_change() throws Exception {
            VirtualIoTNetwork network = new VirtualIoTNetwork(schema, client);

            UUID state = schema.device.get(0)
                .value.get(0)
                .state.get(1)
                .meta.id;
            network.update(state, "1");
            assertTrue(client.state.contains("\"data\":\"1\""));
        }
    }

    private class IoTClientMock implements IoTClient {

        public String state;

        @Override
        public void send(String message) {
            state = message;
        }
    }
}
