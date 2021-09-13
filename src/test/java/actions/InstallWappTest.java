package actions;

import org.junit.jupiter.api.*;
import wappsto.rest.exceptions.HttpException;
import wappsto.rest.session.Wapp;
import wappsto.rest.session.core.User;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static util.Env.*;
import static util.Utils.*;

public class InstallWappTest {

    @BeforeAll
    public static void setup() throws Exception {
        try {
            admin().delete(defaultUser().username);
        } catch (HttpException ignore) {
        }
    }

    @Nested
    public class fails_to_install {
        @Test
        public void invalid_wapp_name() throws Exception {
            String sessionId = createNewSession().id;
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
        public void installs_wapp_by_name() throws Exception {
            InstallWapp action = createNewAction(
                env().get(API_ROOT),
                "Historical Data"
            );
            User session = new User.Builder(
                admin(),
                env().get(API_ROOT)
            ).withCredentials(defaultUser())
                .create();

            logInBrowser(session.id, env().get(APP_URL));
            runner().run(action);

            Wapp wapp = new Wapp(session);
            assert wapp.fetchInstalled().size() == 1
                : "Incorrect number of wapps";
        }
    }

    private User createNewSession() throws Exception {
        return new User.Builder(
            admin(),
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
    public void tearDown() throws Exception {
        try {
            admin().delete(defaultUser().username);
        } catch (HttpException ignore) {
        }
        resetRunner();
    }
}
