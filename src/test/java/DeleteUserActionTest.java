import actions.DeleteUser;
import org.junit.jupiter.api.*;
import test.Config;
import wappsto.rest.exceptions.HttpException;
import wappsto.rest.model.Credentials;

import java.io.IOException;

import static test.Utils.*;

public class DeleteUserActionTest {
    private static Config testConfig;
    private static String serviceUrl;


    @BeforeAll
    public static void setup() throws Exception {
        testConfig = new Config();
        serviceUrl = testConfig.API_ROOT;
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
    public void deletes_user() throws Exception {
        DeleteUser action = new DeleteUser();

        admin().register(new Credentials(
            defaultUser().username,
            defaultUser().password
        ));

        action.serviceUrl = serviceUrl;
        action.username = defaultUser().username;
        action.adminPassword = testConfig.ADMIN_PASSWORD;
        action.adminUsername = testConfig.ADMIN_USERNAME;

        runner().run(action);
    }
    @AfterEach
    public void tearDown() throws Exception {
        resetRunner();
    }
}
