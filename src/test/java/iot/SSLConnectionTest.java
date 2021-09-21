package iot;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import wappsto.iot.ssl.SSLConnection;
import wappsto.iot.ssl.model.WappstoCerts;
import org.junit.jupiter.api.Test;
import wappsto.rest.request.exceptions.HttpException;
import wappsto.rest.network.NetworkService;
import wappsto.rest.network.model.NetworkCreatorResponse;

import static org.junit.jupiter.api.Assertions.*;
import static util.Utils.*;

public class SSLConnectionTest {
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
        SSLConnection connection = new SSLConnection(sslAddress, sslPort, certs);
        assertTrue(connection.connected());
    }

    @AfterEach
    public void tearDown() throws Exception{
        try {
            admin().delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }
}
