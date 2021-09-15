package iot;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import wappsto.iot.model.WappstoCerts;
import org.junit.jupiter.api.Test;
import wappsto.iot.WappstoRPCClient;
import wappsto.rest.exceptions.HttpException;
import wappsto.rest.iot.NetworkService;
import wappsto.rest.iot.model.NetworkCreatorResponse;

import static org.junit.jupiter.api.Assertions.*;
import static util.Utils.*;

public class WappstoRPCClientTest {
    private final static String serviceUrl = "https://qa.wappsto.com/services/2.1";
    private final String sslAddress = "qa.wappsto.com";
    private final int sslPort = 53005;

    @BeforeAll
    public static void setup() throws Exception {
        try {
            admin().delete(defaultUser().username);
        } catch (HttpException ignore) {
        }
    }

    @Test
    public void connects_via_ssl() throws Exception {
        NetworkCreatorResponse networkCreatorResponse = new NetworkService(
            createNewUserSession(serviceUrl)
        ).create();

        WappstoCerts certs = new WappstoCerts(
            networkCreatorResponse.ca,
            networkCreatorResponse.certificate,
            networkCreatorResponse.privateKey
        );
        WappstoRPCClient client = new WappstoRPCClient(sslAddress, certs, sslPort);
        assertTrue(client.connected());
    }
}
