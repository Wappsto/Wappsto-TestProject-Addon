package iot;

import org.junit.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import wappsto.iot.*;
import wappsto.iot.rpc.*;
import wappsto.iot.model.schema.network.*;
import wappsto.rest.network.model.*;

import java.io.*;
import java.util.*;

import static iot.Utils.defaultNetwork;
import static org.junit.jupiter.api.Assertions.*;

@Disabled
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
        public void on_startup() {

            VirtualIoTNetwork network = new VirtualIoTNetwork(schema, client);
            assertTrue(client.receivedSchema);
            assertEquals("Startup", client.state);
        }

        public void on_state_change() {
            VirtualIoTNetwork network = new VirtualIoTNetwork(schema, client);
            //network.update(key, value);
        }
    }

    private class IoTClientMock implements IoTClient {

        public boolean receivedSchema = false;
        public String state;

        @Override
        public void send(String message) throws IOException {
            state = message;
        }

        @Override
        public void publish(NetworkSchema schema) {
            receivedSchema = true;
        }
    }
}
