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

@Action(name = "Log in an existing user")
public class LogInExistingUser implements WebAction {
    @Parameter(description = "Username")
    public String username;

    @Parameter(description = "Password")
    public String password;

    @Parameter(
        description = "Application URL",
        defaultValue = Defaults.APPLICATION_URL
    )
    public String appUrl;

    @Parameter(
        description = "Service API URL",
        defaultValue = Defaults.SERVICE_URL
    )
    public String serviceUrl;


    @Override
    public ExecutionResult execute(
        WebAddonHelper helper
    ) throws FailureException {
        WebDriver browser = helper.getDriver();

        String sessionId = new Controller(
            new Credentials(username, password),
            serviceUrl
        ).execute();

        browser.navigate().to(appUrl);
        logIn(browser, sessionId);
        return ExecutionResult.PASSED;
    }

    public static class Controller {
        private final User user;

        public Controller(
            Credentials credentials,
            String target
        ) throws FailureException {
            this(
                createRestSession(credentials, target)
            );
        }

        public Controller(User user) {
            this.user = user;
        }

        public String execute() {
            return user.getId();
        }
    }
}
