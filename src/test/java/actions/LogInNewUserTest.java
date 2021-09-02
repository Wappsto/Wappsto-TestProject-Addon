package actions;

import org.junit.jupiter.api.*;
import util.Config;
import wappsto.rest.exceptions.HttpException;

import java.util.concurrent.ExecutionException;

import static util.Utils.*;
import static org.junit.jupiter.api.Assertions.*;

public class LogInNewUserTest {
    private static Config testConfig;

    @BeforeAll
    public static void setup() throws Exception {
        testConfig = new Config();
        try {
            admin().delete(defaultUser().username);
        } catch (HttpException e) {
            if (!e.getMessage().equals("Not Found")) {
                throw new Exception(e.getMessage());
            }
        }
    }

    @BeforeEach
    public void reset() throws Exception {
        resetRunner();
    }

    @Test
    public void logs_in_new_user() throws Exception {
        LogInWithNewUser action = createAction(
            testConfig.ADMIN_USERNAME,
            testConfig.ADMIN_PASSWORD,
            defaultUser().username,
            defaultUser().password,
            testConfig.API_ROOT,
            testConfig.APP_URL);

        runner().run(action);
    }

    @Nested
    public class fails_to_register_new_user {
        @Test
        public void with_invalid_admin_credentials() {
            LogInWithNewUser action = createAction(
                "123",
                "123",
                defaultUser().username,
                defaultUser().password,
                testConfig.API_ROOT,
                testConfig.APP_URL
            );

            assertThrows(ExecutionException.class, () -> {
                runner().run(action);
            });
        }

        @Test
        public void with_invalid_service_url() {
            LogInWithNewUser action = createAction(
                testConfig.ADMIN_USERNAME,
                testConfig.ADMIN_PASSWORD,
                defaultUser().username,
                defaultUser().password,
                "123",
                testConfig.APP_URL
            );

            assertThrows(ExecutionException.class, () -> {
                runner().run(action);
            });
        }

        @Test
        public void with_invalid_user_credentials() {
            LogInWithNewUser action = createAction(
                testConfig.ADMIN_USERNAME,
                testConfig.ADMIN_PASSWORD,
                "123",
                defaultUser().password,
                testConfig.API_ROOT,
                testConfig.APP_URL
            );

            assertThrows(ExecutionException.class, () -> {
                runner().run(action);
            });
        }
    }

    private LogInWithNewUser createAction(
        String adminUsername,
        String adminPassword,
        String username,
        String password,
        String serviceUrl,
        String appUrl
    ) {
        LogInWithNewUser action = new LogInWithNewUser();

        action.adminPassword = adminPassword;
        action.adminUsername = adminUsername;
        action.username = username;
        action.password = password;
        action.serviceUrl = serviceUrl;
        action.appUrl = appUrl;

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
