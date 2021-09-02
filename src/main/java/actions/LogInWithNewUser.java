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

        WebDriver driver = helper.getDriver();
        UserSession session;
        try {
            AdminCredentials adminCredentials = new AdminCredentials(
                adminUsername,
                adminPassword);
            Credentials userCredentials = new Credentials(
                username,
                password
            );
            AdminSession adminSession = new AdminSession(
                adminCredentials,
                serviceUrl);
            session = new UserSessionBuilder(adminSession, serviceUrl)
                .withCredentials(userCredentials)
                .create();

        } catch (Exception e) {
            e.printStackTrace();
            return ExecutionResult.FAILED;
        }

        driver.get(appUrl);
        driver.manage().addCookie(new Cookie(
            "sessionID", session.getId()
        ));

        return ExecutionResult.PASSED;
    }
}
