package actions;

import org.junit.jupiter.api.*;
import util.Env;
import wappsto.rest.request.exceptions.HttpException;
import wappsto.rest.session.model.Credentials;

import java.util.concurrent.ExecutionException;

import static util.Env.*;
import static util.Utils.*;
import static org.junit.jupiter.api.Assertions.*;

public class DeleteUserTest {
    private static String serviceUrl;


    @BeforeAll
    public static void setup() throws Exception {
        serviceUrl = env().get(API_ROOT);
        try {
            admin().delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }

    @BeforeEach
    public void reset() throws Exception {
         resetRunner();
    }

    @Test
    public void deletes_user() throws Exception {
        admin().register(new Credentials(
            defaultUser().username,
            defaultUser().password
        ));

        DeleteUser action = createAction(
            serviceUrl,
            defaultUser().username,
            env().get(Env.ADMIN_USERNAME),
            env().get(Env.ADMIN_PASSWORD)
            );

        runner().run(action);

        assertThrows(HttpException.class,
            () -> admin().fetchUser(defaultUser().username),
            "Not Found");
    }

    @Nested
    public class fails_to_delete_user {
        @Test
        public void when_admin_login_is_invalid() throws Exception {
            admin().register(defaultUser());

            DeleteUser action = createAction(
                env().get(API_ROOT),
                defaultUser().username,
                "123",
                "123"
            );

            assertThrows(ExecutionException.class, () -> runner().run(action));
        }

        @Test
        public void when_service_url_is_invalid() throws Exception {
            admin().register(defaultUser());

            DeleteUser action = createAction(
                "invalid_root",
                defaultUser().username,
                env().get(ADMIN_USERNAME),
                env().get(ADMIN_PASSWORD)
            );

            assertThrows(ExecutionException.class, () -> runner().run(action));
        }
    }

    private DeleteUser createAction(
        String serviceUrl,
        String username,
        String adminUsername,
        String adminPassword
    ) {
        DeleteUser action = new DeleteUser();

        action.serviceUrl = serviceUrl;
        action.username = username;
        action.adminUsername = adminUsername;
        action.adminPassword = adminPassword;
        return action;
    }

    @AfterEach
    public void tearDown() throws Exception {
        resetRunner();
        try {
            admin().delete(defaultUser().username);
        } catch (Exception ignored) {
        }
    }
}
