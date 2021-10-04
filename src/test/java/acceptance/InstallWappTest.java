package acceptance;

import actions.wapps.*;
import extensions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import wappsto.rest.request.exceptions.HttpException;
import wappsto.rest.session.*;
import wappsto.rest.wapps.RestWappService;
import wappsto.session.*;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static util.Env.*;
import static util.Utils.*;

@ExtendWith(AdminInjector.class)
public class InstallWappTest {

    @BeforeAll
    public static void setup(Admin admin) throws Exception {
        try {
            admin.delete(defaultUser().username);
        } catch (HttpException ignore) {
        }
    }

    @Nested
    public class fails_to_install {
        @Test
        public void invalid_wapp_name(Admin admin) throws Exception {
            String sessionId = createNewSession(admin).getId();
            InstallWapp action = createNewAction(
                env().get(API_ROOT),
                ""
            );

            logInBrowser(sessionId, env().get(APP_URL));

            assertThrows(ExecutionException.class, () -> runner().run(action));
        }

        @Test
        public void when_browser_is_not_logged_in() {
            InstallWapp action = createNewAction(
                env().get(API_ROOT),
                "Historical Data"
            );

            assertThrows(ExecutionException.class, () -> runner().run(action));
        }
    }

    @Nested
    public class when_browser_is_logged_in {
        @Test
        public void installs_wapp_by_name(Admin admin) throws Exception {
            InstallWapp action = createNewAction(
                env().get(API_ROOT),
                "Historical Data"
            );
            RestUser session = createNewSession(admin);

            logInBrowser(session.getId(), env().get(APP_URL));
            runner().run(action);

            RestWappService wapp = new RestWappService(session);
            assert wapp.fetchInstalled().size() == 1
                : "Incorrect number of wapps";
        }
    }

    private RestUser createNewSession(Admin admin) throws Exception {
        return new RestUser.Builder(
            admin,
            env().get(API_ROOT)
        ).withCredentials(defaultUser())
            .create();
    }

    private InstallWapp createNewAction(String serviceUrl, String nameOfWapp) {
        InstallWapp action = new InstallWapp();
        action.serviceUrl = serviceUrl;
        action.nameOfWapp = nameOfWapp;

        return action;
    }

    @AfterEach
    public void tearDown(Admin admin) throws Exception {
        setup(admin);
        resetRunner();
    }
}
