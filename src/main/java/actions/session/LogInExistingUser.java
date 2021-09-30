package actions.session;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.WebAction;
import io.testproject.java.sdk.v2.addons.helpers.WebAddonHelper;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import io.testproject.java.sdk.v2.exceptions.FailureException;
import org.openqa.selenium.WebDriver;
import wappsto.rest.session.*;
import wappsto.session.*;
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
            sessionId = new UserController(
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

    public static class UserController {
        private User user;

        public UserController(
            Credentials credentials,
            String target
        ) throws Exception {
            this(
                new RestUser(credentials, target)
            );
        }

        public UserController(User user) {
            this.user = user;
        }

        public String execute() {
            return user.getId();
        }
    }
}