package acceptance;

import actions.session.*;
import extensions.injectors.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import wappsto.rest.request.exceptions.HttpException;
import wappsto.session.*;

import java.util.concurrent.ExecutionException;

import static util.Env.*;
import static util.Utils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

@ExtendWith(AdminInjector.class)
public class LogInExistingUserTest {

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
    public void logs_in_user(Admin admin) throws Exception {
        admin.register(defaultUser());

        LogInExistingUser action = createAction(
            env().get(API_ROOT),
            env().get(APP_URL),
            defaultUser().username,
            defaultUser().password);

        runner().run(action);

        assert loggedIn(runner().getDriver()) : "Browser not logged in";
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
    public class fails_to_log_in {
        @Test
        public void with_invalid_credentials() {
            LogInExistingUser action = createAction(
                env().get(API_ROOT),
                env().get(APP_URL),
                "123",
                "123"
            );

            assertThrows(ExecutionException.class, () -> runner().run(action));
        }

        @Test
        public void with_invalid_service_url(Admin admin) throws Exception {
            admin.register(defaultUser());

            LogInExistingUser action = createAction(
                "123",
                env().get(APP_URL),
                defaultUser().username,
                defaultUser().password
            );

            assertThrows(ExecutionException.class, () -> runner().run(action));
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
    public void tearDown(Admin admin) throws Exception {
        setup(admin);
    }
}
