package actions.admin;

import actions.LogInAsAdmin;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.ExecutionException;

import static org.junit.Assume.*;
import static util.Env.*;
import static util.Utils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;



public class LogInAdminTest {

    @BeforeEach
    public void reset() throws Exception {
        resetRunner();
    }

    @Test
    public void logs_in_as_admin() throws Exception {
        assumeTrue(!isCiTest());

        LogInAsAdmin action = createAction(
            env().get(ADMIN_PANEL),
            env().get(ADMIN_USERNAME),
            env().get(ADMIN_PASSWORD),
            env().get(API_ROOT)
        );

        runner().run(action);

        WebDriver browser = runner().getDriver();
        assert loggedIn(browser) : "Browser not logged in";

    }

    private boolean isCiTest() {
        return env().get("CI_TEST").equals("true");
    }

    @Nested
    public class fails_to_log_in {
        @Test
        public void with_invalid_credentials() {
            LogInAsAdmin action = createAction(
                env().get(ADMIN_PANEL),
                "123",
                "123",
                env().get(API_ROOT)
            );

            assertThrows(ExecutionException.class, () -> runner().run(action));
        }

        @Test
        public void with_invalid_service_url() {
            LogInAsAdmin action = createAction(
                env().get(ADMIN_PANEL),
                env().get(ADMIN_USERNAME),
                env().get(ADMIN_PASSWORD),
                "123"
            );

            assertThrows(ExecutionException.class, () -> runner().run(action));
        }
    }

    private LogInAsAdmin createAction(
        String adminPanel,
        String adminUsername,
        String adminPassword,
        String serviceUrl
    ) {
        LogInAsAdmin action = new LogInAsAdmin();
        action.adminPanel = adminPanel;
        action.adminUsername = adminUsername;
        action.adminPassword = adminPassword;
        action.serviceUrl = serviceUrl;
        return action;
    }

    private boolean loggedIn(WebDriver browser) {
        browser.navigate().refresh();
        try {
            new WebDriverWait(browser, 5)
                .until(visibilityOf(browser
                    .findElement(By.xpath("//span[@aria-label='logout']")))
                );
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
