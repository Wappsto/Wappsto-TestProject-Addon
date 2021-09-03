package actions;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.WebAction;
import io.testproject.java.sdk.v2.addons.helpers.WebAddonHelper;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import io.testproject.java.sdk.v2.exceptions.FailureException;
import org.openqa.selenium.*;
import wappsto.rest.model.*;
import wappsto.rest.session.*;

@Action(name = "Create a new logged in user")
public class LogInWithNewUser
    extends ActionWithAdminSession implements WebAction
{
    @Parameter(
        description = "Application URL",
        defaultValue = "https://qa.wappsto.com/"
    )
    public String appUrl;

    @Parameter(description = "Username")
    public String username;

    @Parameter(description = "Password")
    public String password;

    @Override
    public ExecutionResult execute(
        WebAddonHelper helper
    ) throws FailureException {

        WebDriver browser = helper.getDriver();

        AdminCredentials adminCredentials = new AdminCredentials(
            adminUsername,
            adminPassword
        );
        Credentials userCredentials = new Credentials(
            username,
            password
        );

        AdminSession admin;
        UserSession session;
        try {
            admin = new AdminSession(
                adminCredentials,
                serviceUrl);
        }
        catch (Exception e) {
            throw new FailureException(
                "Failed to create admin session: " + e.getMessage()
            );
        }

        try {
            session = new UserSessionBuilder(admin, serviceUrl)
                .withCredentials(userCredentials)
                .create();
        } catch (Exception e) {
            throw new FailureException(
                "Failed to register user: " + e.getMessage()
            );
        }

        browser.get(appUrl);
        logIn(browser, session);
        return ExecutionResult.PASSED;
    }

    private void logIn(WebDriver browser, UserSession session) {
        browser.manage().addCookie(new Cookie(
            "sessionID", session.getId()
        ));
    }
}
