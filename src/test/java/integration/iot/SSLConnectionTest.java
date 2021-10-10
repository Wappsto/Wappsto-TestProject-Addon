package integration.iot;

import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.network.model.*;
import wappsto.api.rest.network.*;
import wappsto.api.rest.request.exceptions.*;
import wappsto.api.session.*;
import wappsto.iot.ssl.*;
import wappsto.iot.ssl.model.*;

import static org.junit.jupiter.api.Assertions.*;
import static util.Env.*;
import static util.Utils.*;

@ExtendWith(AdminInjector.class)
public class SSLConnectionTest {
    private static String serviceUrl;
    private final String sslAddress = env().get(SOCKET_URL);
    private final int sslPort = Integer.parseInt(env().get(SOCKET_PORT));

    @BeforeAll
    public static void setup(Admin admin) throws Exception {
        serviceUrl = env().get(API_ROOT);
        try {
            admin.delete(defaultUser().username);
        } catch (HttpException ignore) {
        }
    }

    @Test
    public void connects_via_ssl(Admin admin) throws Exception {
        CreatorResponse creatorResponse = new RestNetworkService(
            createNewUserSession(serviceUrl, admin)
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
    public void tearDown(Admin admin) throws Exception{
        try {
            admin.delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }
}
