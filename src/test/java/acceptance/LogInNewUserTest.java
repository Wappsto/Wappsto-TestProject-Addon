package acceptance;

import actions.session.*;
import extensions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import util.Env;
import wappsto.rest.request.exceptions.HttpException;
import wappsto.session.*;

import java.util.concurrent.ExecutionException;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static util.Env.*;
import static util.Utils.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AdminInjector.class)
public class LogInNewUserTest {

    @BeforeAll
    public static void setup(Admin admin) throws Exception {
        try {
            admin.delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }

    @BeforeEach
    public void reset() throws Exception {
        resetRunner();
    }

    @Test
    public void logs_in_new_user() throws Exception {
        LogInWithNewUser action = createAction(
            env().get(Env.ADMIN_USERNAME),
            env().get(Env.ADMIN_PASSWORD),
            defaultUser().username,
            defaultUser().password,
            env().get(API_ROOT),
            env().get(APP_URL));

        runner().run(action);
        WebDriver browser = runner().getDriver();
        assert loggedIn(browser) : "Browser not logged in";
    }

    private boolean loggedIn(WebDriver browser) {
        browser.navigate().refresh();
        try {
            new WebDriverWait(browser, 5)
                .until(visibilityOf(browser.
                    findElement(By.xpath(String.format(
                        "//span[text()='%s']",
                        defaultUser().username))
                    )
                ));
        } catch (Exception e) {
            return false;
        }
        return true;
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
                env().get(API_ROOT),
                env().get(APP_URL)
            );

            assertThrows(ExecutionException.class, () -> runner().run(action));
        }

        @Test
        public void with_invalid_service_url() {
            LogInWithNewUser action = createAction(
                env().get(ADMIN_USERNAME),
                env().get(ADMIN_PASSWORD),
                defaultUser().username,
                defaultUser().password,
                "123",
                env().get(APP_URL)
            );

            assertThrows(ExecutionException.class, () -> runner().run(action));
        }

        @Test
        public void with_invalid_user_credentials() {
            LogInWithNewUser action = createAction(
                env().get(ADMIN_USERNAME),
                env().get(ADMIN_PASSWORD),
                "123",
                defaultUser().password,
                env().get(Env.API_ROOT),
                env().get(APP_URL)
            );

            assertThrows(ExecutionException.class, () -> runner().run(action));
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
    public void tearDown(Admin admin) throws Exception {
        try {
            admin.delete(defaultUser().username);
        } catch (HttpException ignored) {
        }
    }
}
