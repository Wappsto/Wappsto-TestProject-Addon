package iot;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import wappsto.iot.ssl.SSLConnection;
import wappsto.iot.ssl.model.WappstoCerts;
import org.junit.jupiter.api.Test;
import wappsto.rest.request.exceptions.HttpException;
import wappsto.rest.network.RestNetworkService;
import wappsto.network.model.CreatorResponse;

import static org.junit.jupiter.api.Assertions.*;
import static util.Env.API_ROOT;
import static util.Env.env;
import static util.Utils.*;

public class SSLConnectionTest {
    private static String serviceUrl;
    private final String sslAddress = "qa.wappsto.com";
    private final int sslPort = 53005;

    @BeforeAll
    public static void setup() throws Exception {
        serviceUrl = env().get(API_ROOT);
        try {
            admin().delete(defaultUser().username);
        } catch (HttpException ignore) {
        }
    }

    @Test
    public void connects_via_ssl() throws Exception {
        CreatorResponse creatorResponse = new RestNetworkService(
            createNewUserSession(serviceUrl)
        ).getCreator();

        WappstoCerts certs = new WappstoCerts(
            creatorResponse.ca,
            creatorResponse.certificate,
            creatorResponse.privateKey
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
