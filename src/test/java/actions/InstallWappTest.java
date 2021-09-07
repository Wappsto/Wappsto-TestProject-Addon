package actions;

import io.testproject.java.sdk.v2.drivers.WebDriver;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Cookie;
import util.Config;
import wappsto.rest.exceptions.HttpException;
import wappsto.rest.session.UserSession;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static util.Utils.*;

public class InstallWappTest {
    private static Config testConfig;

    @BeforeAll
    public static void setup() throws Exception {
        try {
            admin().delete(defaultUser().username);
        } catch (HttpException ignore) {
        }

        testConfig = new Config();
    }

    @Nested
    public class fails_to_install {
        @Test
        public void invalid_wapp_name() throws Exception {
            String sessionId = createNewSession()
                .getId();
            InstallWapp action = createNewAction(
                testConfig.API_ROOT,
                ""
            );

            logInBrowser(sessionId, testConfig.APP_URL);

            assertThrows(ExecutionException.class, () -> {
                runner().run(action);
            });
        }

        @Test
        public void when_browser_is_not_logged_in() {
            InstallWapp action = createNewAction(
                testConfig.API_ROOT,
                "Historical Data"
            );

            assertThrows(ExecutionException.class, () -> {
               runner().run(action);
            });
        }
    }

    @Nested
    public class when_browser_is_logged_in {
        @Test
        public void installs_wapp_by_name() throws Exception {
            InstallWapp action = createNewAction(
                testConfig.API_ROOT,
                "Historical Data"
            );
            UserSession session = new UserSession.Builder(
                admin(),
                testConfig.API_ROOT
            ).withCredentials(defaultUser())
                .create();

            logInBrowser(session.getId(), testConfig.APP_URL);
            runner().run(action);

            assert session.fetchWapps().size() == 1
                : "Incorrect number of wapps";
        }
    }

    private UserSession createNewSession() throws Exception {
        return new UserSession.Builder(
            admin(),
            testConfig.API_ROOT
        ).withCredentials(defaultUser())
            .create();
    }

    private InstallWapp createNewAction(String API_ROOT, String nameOfWapp) {
        InstallWapp action = new InstallWapp();
        action.serviceUrl = API_ROOT;
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
