package unit.iot;

import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.iot.network.*;
import wappsto.iot.network.model.*;
import wappsto.iot.rpc.*;
import wappsto.iot.rpc.model.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(NetworkSchemaInjector.class)
public class VirtualIotNetworkTest {
    private IoTClientMock client;


    @BeforeEach
    public void instantiateClient() {
        client = new IoTClientMock();
    }

    @Nested
    public class reports_its_state {
        @Test
        public void on_startup(NetworkSchema schema) {

            VirtualIoTNetwork network = new VirtualIoTNetwork(schema, client);
            assertTrue(client.state.contains(schema.meta.id.toString()));
        }

        @Test
        public void on_state_change(NetworkSchema schema) {
            VirtualIoTNetwork network = new VirtualIoTNetwork(schema, client);

            UUID state = schema.device.get(0)
                .value.get(0)
                .state.get(1)
                .meta.id;


            ControlStateData request = new ControlStateData(state, "1");
            network.update(request);
            assertTrue(client.state.contains("\"data\":\"1\""));
        }
    }

    private static class IoTClientMock implements IoTClient {

        public String state;

        @Override
        public void start(JsonRPCParser parser) {

        }

        @Override
        public void send(String message) {
            state = message;
        }

        @Override
        public void stop() {

        }
    }
}
