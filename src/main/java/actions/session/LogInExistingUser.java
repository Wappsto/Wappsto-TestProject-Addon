package actions.session;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.WebAction;
import io.testproject.java.sdk.v2.addons.helpers.WebAddonHelper;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import io.testproject.java.sdk.v2.exceptions.FailureException;
import org.openqa.selenium.WebDriver;
import wappsto.session.model.Credentials;

import static actions.Utils.*;

@Action(name = "Log in an existing user")
public class LogInExistingUser implements WebAction {
    @Parameter(description = "Username")
    public String username;

    @Parameter(description = "Password")
    public String password;

    @Parameter(description = "Application URL")
    public String appUrl;

    @Parameter(description = "Application API URL")
    public String serviceUrl;


    @Override
    public ExecutionResult execute(
        WebAddonHelper helper
    ) throws FailureException {
        WebDriver browser = helper.getDriver();

        String sessionId;
        try {
            sessionId = new LogInExistingUserController(
                new Credentials(username, password),
                serviceUrl
            ).execute();
        } catch (Exception e) {
            throw new FailureException("Error logging in: " + e.getMessage());
        }
        browser.navigate().to(appUrl);
        logIn(browser, sessionId);
        return ExecutionResult.PASSED;
    }
}
