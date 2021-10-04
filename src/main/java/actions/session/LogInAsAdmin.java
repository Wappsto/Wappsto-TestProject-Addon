package actions.session;

import actions.*;
import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.WebAction;
import io.testproject.java.sdk.v2.addons.helpers.WebAddonHelper;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import io.testproject.java.sdk.v2.exceptions.FailureException;
import org.openqa.selenium.*;
import wappsto.session.*;
import wappsto.session.model.AdminCredentials;
import wappsto.rest.session.RestAdmin;

import static actions.Utils.createRestAdminSession;

@Action(name = "Log in as admin")
public class LogInAsAdmin extends ActionWithAdminSession implements WebAction {

    @Parameter(description = "Admin Panel URL")
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
