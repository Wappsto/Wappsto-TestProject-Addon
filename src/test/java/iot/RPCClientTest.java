package iot;

import org.junit.jupiter.api.*;
import wappsto.iot.rpc.*;
import wappsto.iot.rpc.model.*;
import wappsto.iot.ssl.*;
import wappsto.iot.ssl.model.*;
import wappsto.rest.network.*;
import wappsto.rest.network.model.*;
import wappsto.rest.request.exceptions.*;
import wappsto.rest.session.*;

import java.io.*;

import static iot.Utils.defaultNetwork;
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
        newNetworkCreator();
        WappstoCerts certs = new WappstoCerts(creatorResponse);

        connection = new SSLConnection(
            "qa.wappsto.com",
            53005,
            certs
        );
    }

    private void newNetworkCreator() throws Exception {
        session = createNewUserSession(serviceUrl);
        creatorResponse = new NetworkService(session).getCreator();
    }

    @Test
    public void sends_its_own_structure_to_the_server() throws Exception {
        RPCRequest jsonRPCRequest = createVirtualIoTDevice();
        RPCClient client = new RPCClient(connection, jsonRPCRequest);
        NetworkService networkService = new NetworkService(session);
        assertNotNull(
            networkService.fetch(creatorResponse.network.id).id
        );
    }

    @Test
    @Disabled
    public void reports_state_change_to_server() throws IOException {
        RPCRequest jsonRPCRequest = createVirtualIoTDevice();
        RPCClient client = new RPCClient(connection, jsonRPCRequest);

    }

    private RPCRequest createVirtualIoTDevice() {
        Params params = new Params(
            "/network",
            defaultNetwork(creatorResponse)
        );

        RPCRequest jsonRPCRequest = new RPCRequest(
            params,
            Methods.POST
        );
        return jsonRPCRequest;
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
