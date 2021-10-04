package integration.rest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.*;
import extensions.*;
import wappsto.rest.request.exceptions.HttpException;
import wappsto.rest.wapps.RestWappService;
import wappsto.rest.session.RestUser;
import wappsto.session.*;
import wappsto.wapps.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static util.Env.API_ROOT;
import static util.Env.env;
import static util.Utils.*;

@ExtendWith(AdminInjector.class)
@ExtendWith(WappServiceInjector.class)
public class WappTest {
    private static String serviceUrl;

    @BeforeAll
    public static void setup(Admin admin) throws Exception {
        serviceUrl = env().get(API_ROOT);

        try {
            admin.delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }

    @Test
    public void fetches_all_wapps(WappService service) throws Exception {
        assertTrue(service.fetchAllFromStore().size() >= 1);
    }

    @Nested
    public class installs_wapp {

        @Test
        public void from_name(WappService service) throws Exception {
            service.install("Historical Data");
            assert service.fetchInstalled().size() == 1
                : "Incorrect number of wapps found";
        }
    }

    @Test
    public void fails_to_install_wapp_from_invalid_name(WappService service) throws Exception {
        assertThrows(
            Exception.class,
            () -> service.install(""),
            "Wapp not found"
        );
    }

    @AfterEach
    public void tearDown(Admin admin) throws Exception {
        try {
            admin.delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }
}
