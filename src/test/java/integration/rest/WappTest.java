package integration.rest;

import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.api.rest.request.exceptions.*;
import wappsto.api.session.*;
import wappsto.api.wapps.*;

import static org.junit.jupiter.api.Assertions.*;
import static util.Env.*;
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
