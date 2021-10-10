package actions.session;

import actions.*;
import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.*;
import io.testproject.java.sdk.v2.addons.helpers.*;
import io.testproject.java.sdk.v2.enums.*;
import io.testproject.java.sdk.v2.exceptions.*;
import org.openqa.selenium.*;
import wappsto.api.session.model.*;

import static actions.Utils.*;

@Action(name = "Create a new logged in user")
public class LogInWithNewUser
    extends ActionWithAdminSession implements WebAction
{
    @Parameter(
        description = "Application URL",
        defaultValue = Defaults.APPLICATION_URL
    )
    public String appUrl;

    @Parameter(description = "Username")
    public String username;

    @Parameter(description = "Password")
    public String password;

    @Override
    public ExecutionResult execute(WebAddonHelper helper)
        throws FailureException
    {
        WebDriver browser = helper.getDriver();

        AdminCredentials adminCredentials = new AdminCredentials(
            adminUsername,
            adminPassword
        );
        Credentials userCredentials = new Credentials(
            username,
            password
        );

        String sessionId = new CreateNewUserController(
            adminCredentials,
            userCredentials,
            serviceUrl
        ).execute();

        browser.get(appUrl);
        logIn(browser, sessionId);
        return ExecutionResult.PASSED;
    }
}
