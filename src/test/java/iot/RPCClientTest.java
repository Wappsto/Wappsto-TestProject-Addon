package iot;

import wappsto.iot.rpc.model.request.*;
import org.junit.jupiter.api.*;
import wappsto.iot.rpc.*;
import wappsto.iot.rpc.model.request.Number;
import wappsto.iot.ssl.*;
import wappsto.iot.ssl.model.*;
import wappsto.rest.network.*;
import wappsto.rest.network.model.*;
import wappsto.rest.request.exceptions.*;
import wappsto.rest.session.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static util.Env.API_ROOT;
import static util.Env.env;
import static util.Utils.*;

public class RPCClientTest {
    private static String serviceUrl;
    private SSLConnection connection;
    private User session;
    private CreatorResponse creatorResponse;


    @BeforeAll
    public static void cleanSlate() throws Exception {
        serviceUrl = env().get(API_ROOT);
        try {
            admin().delete(defaultUser().username);
        } catch (HttpException ignored){
        }
    }

    @BeforeEach
    public void newClient() throws Exception {
        session = createNewUserSession(serviceUrl);

        creatorResponse = new NetworkService(session)
            .getCreator();

        WappstoCerts certs = new WappstoCerts(creatorResponse);

        connection = new SSLConnection(
            "qa.wappsto.com",
            53005,
            certs
        );
    }

    @Test
    public void sends_its_own_structure_to_the_server() throws Exception {
        VirtualNetwork network = createVirtualIoTDevice();
        RPCClient client = new RPCClient(connection, network);
        NetworkService networkService = new NetworkService(session);
        assertNotNull(
            networkService.fetch(creatorResponse.network.id).id
        );
    }

    private VirtualNetwork createVirtualIoTDevice() {
        Value value = new Value(
            "On/off",
            "rw",
            new Number(
                0,
                1,
                1,
                "Boolean"
            )
        );
        LinkedList<Value> values = new LinkedList<>();
        values.add(value);

        Device device = new Device(
            "Switch",
            values
        );

        LinkedList<Device> devices = new LinkedList<>();
        devices.add(device);
        RPCData data = new RPCData(
            "On/off switch",
            devices,
            creatorResponse.network.id
        );

        Params params = new Params(
            "/network",
            data
        );

        VirtualNetwork network = new VirtualNetwork(
            params,
            "POST",
            "thingy_1"
        );
        return network;
    }

    @AfterEach
    public void tearDown() throws Exception {
        connection.disconnect();
        try {
            admin().delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }
}
