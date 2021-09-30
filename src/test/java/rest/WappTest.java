package rest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wappsto.rest.request.exceptions.HttpException;
import wappsto.rest.wapps.WappService;
import wappsto.rest.session.RestUser;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.Env.API_ROOT;
import static util.Env.env;
import static util.Utils.*;

public class WappTest {
    private static String serviceUrl;

    @BeforeAll
    public static void setup() throws Exception {
        serviceUrl = env().get(API_ROOT);

        try {
            admin().delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }

    @Test
    public void fetches_all_wapps() throws Exception {
        RestUser session = createNewUserSession(serviceUrl);
        WappService wapp = new WappService(session);

        assertTrue(wapp.fetchAllFromStore().size() >= 1);
    }

    @Nested
    public class installs_wapp {

        @Test
        public void from_name() throws Exception {
            RestUser session = createNewUserSession(serviceUrl);
            WappService wapp = new WappService(session);

            wapp.install("Historical Data");
            assert wapp.fetchInstalled().size() == 1
                : "Incorrect number of wapps found";
        }
    }

    @Test
    public void fails_to_install_wapp_from_invalid_name() throws Exception {
        RestUser session = createNewUserSession(serviceUrl);

        assertThrows(
            Exception.class,
            () -> new WappService(session).install(""),
            "Wapp not found"
        );
    }

    @AfterEach
    public void tearDown() throws Exception {
        try {
            admin().delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }
}
