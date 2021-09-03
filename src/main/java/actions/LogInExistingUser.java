package actions;

import io.testproject.java.annotations.v2.Action;
import io.testproject.java.annotations.v2.Parameter;
import io.testproject.java.sdk.v2.addons.WebAction;
import io.testproject.java.sdk.v2.addons.helpers.WebAddonHelper;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import io.testproject.java.sdk.v2.exceptions.FailureException;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import wappsto.rest.exceptions.HttpException;
import wappsto.rest.model.Credentials;
import wappsto.rest.session.UserSession;

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

        UserSession session;
        try {
            session = createNewUserSession();
        } catch (Exception e) {
            throw new FailureException("Error logging in: " + e.getMessage());
        }
        browser.navigate().to(appUrl);
        logIn(browser, session);
        return ExecutionResult.PASSED;
    }

    private UserSession createNewUserSession() throws Exception {
        UserSession session;
        session = new UserSession(
            new Credentials(
                username,
                password
            ),
            serviceUrl
        );
        return session;
    }

    private void logIn(WebDriver browser, UserSession session) {
        browser.manage().addCookie(
            new Cookie("sessionID", session.getId())
        );
    }
}
