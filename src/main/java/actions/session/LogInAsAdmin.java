package actions.session;

import actions.*;
import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.openqa.selenium.*;
import wappsto.session.*;
import wappsto.session.model.*;

import static actions.Utils.*;

@Action(name = "Log in as admin")
public class LogInAsAdmin extends ActionWithAdminSession implements WebAction {

    @Parameter(
        description = "Admin Panel URL",
        defaultValue = Defaults.ADMIN_PANEL_URL
    )
    public String adminPanel;

    @Override
    public ExecutionResult execute(
        WebAddonHelper helper
    ) throws FailureException {

        String sessionId = new Controller(
            new AdminCredentials(
                adminUsername,
                adminPassword
            ),
            serviceUrl
        ).execute();


        WebDriver browser = helper.getDriver();
        browser.navigate().to(adminPanel);

        storeSession(sessionId, (JavascriptExecutor) browser);

        return ExecutionResult.PASSED;
    }

    private void storeSession(String sessionId, JavascriptExecutor driver) {
        driver.executeScript(
            String.format(
                "window.localStorage.setItem('%s', '%s')", "sessionID",
                sessionId
            )
        );
    }

    public static class Controller {
        private final Admin admin;

        public Controller(AdminCredentials credentials, String target)
            throws FailureException
        {
            this(createRestAdminSession(credentials, target));
        }

        public Controller(Admin admin) {

            this.admin = admin;
        }

        public String execute() {
            return admin.getId();
        }
    }
}
