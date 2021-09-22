package iot;

import org.junit.jupiter.api.*;
import wappsto.iot.rpc.model.schema.network.*;
import wappsto.rest.network.model.*;

import java.util.*;

import static iot.Utils.defaultNetwork;

public class VirtualIotNetworkTest {
    private static Params schema;

    @BeforeAll
    public static void setup() {
        CreatorResponse creatorMock = new CreatorResponse();
        creatorMock.network = new NetworkMeta();
        creatorMock.network.id = UUID.randomUUID().toString();
        schema = defaultNetwork(creatorMock);
    }

    @Nested
    public class reports_its_state {
        @Test
        public void on_startup() {

        }
    }
}
