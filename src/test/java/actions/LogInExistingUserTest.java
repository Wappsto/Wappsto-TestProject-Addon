package actions;

import org.junit.jupiter.api.*;
import util.Config;
import wappsto.rest.exceptions.HttpException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import static util.Utils.*;
import static org.junit.jupiter.api.Assertions.*;

public class LogInExistingUserTest {
    private static Config testConfig;

    @BeforeAll
    public static void setup() throws IOException {
        testConfig = new Config();
    }

    @BeforeEach
    public void reset() throws Exception {
        resetRunner();
    }

    @Test
    public void logs_in_user() throws Exception {
        admin().register(defaultUser());

        LogInExistingUser action = createAction(
            testConfig.API_ROOT,
            testConfig.APP_URL,
            defaultUser().username,
            defaultUser().password);

        runner().run(action);
    }

    @Nested
    public class fails_to_log_in {
        @Test
        public void with_invalid_credentials() throws Exception {
            LogInExistingUser action = createAction(
                testConfig.API_ROOT,
                testConfig.APP_URL,
                "123",
                "123"
            );

            assertThrows(ExecutionException.class, () -> {
                runner().run(action);
            });
        }

        @Test
        public void with_invalid_service_url() throws Exception {
            admin().register(defaultUser());

            LogInExistingUser action = createAction(
                "123",
                testConfig.APP_URL,
                defaultUser().username,
                defaultUser().password
            );

            assertThrows(ExecutionException.class, () -> {
                runner().run(action);
            });
        }
    }

    private LogInExistingUser createAction(
        String serviceUrl,
        String appUrl,
        String username,
        String password
    ) {
        LogInExistingUser action = new LogInExistingUser();
        action.serviceUrl = serviceUrl;
        action.appUrl = appUrl;
        action.username = username;
        action.password = password;
        return action;
    }

    @AfterEach
    public void tearDown() throws Exception {
        try {
            admin().delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }
}
