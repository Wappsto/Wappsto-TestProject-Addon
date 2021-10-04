package actions.session;

import io.testproject.java.annotations.v2.*;
import io.testproject.java.sdk.v2.addons.WebAction;
import io.testproject.java.sdk.v2.addons.helpers.WebAddonHelper;
import io.testproject.java.sdk.v2.enums.ExecutionResult;
import io.testproject.java.sdk.v2.exceptions.FailureException;
import org.openqa.selenium.*;
import wappsto.session.model.AdminCredentials;
import wappsto.session.model.Credentials;

import static actions.Utils.*;

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
