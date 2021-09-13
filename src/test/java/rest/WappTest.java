package rest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wappsto.rest.exceptions.HttpException;
import wappsto.rest.session.Wapp;
import wappsto.rest.session.Wapps;
import wappsto.rest.session.core.User;

import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Nested
    public class installs_wapp {
        @Test
        public void from_enum() throws Exception {
            User session = createNewUserSession(serviceUrl);
            Wapp wapp = new Wapp(session);

            wapp.install(Wapps.HISTORICAL_DATA);
            assert wapp.fetchInstalled().size() == 1
                : "Incorrect number of wapps found";
        }

        @Test
        public void from_name() throws Exception {
            User session = createNewUserSession(serviceUrl);
            Wapp wapp = new Wapp(session);

            wapp.install("Historical Data");
            assert wapp.fetchInstalled().size() == 1
                : "Incorrect number of wapps found";
        }
    }

    @Test
    public void fails_to_install_wapp_from_invalid_name() throws Exception {
        User session = createNewUserSession(serviceUrl);

        assertThrows(
            IllegalStateException.class,
            () -> new Wapp(session).install(""),
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
